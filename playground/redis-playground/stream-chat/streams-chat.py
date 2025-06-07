from fastapi import FastAPI, Depends
from starlette.responses import HTMLResponse
from starlette.websockets import WebSocket
from starlette.staticfiles import StaticFiles
import asyncio
import uvloop
import uvicorn
import aioredis
import datetime

HOST = "0.0.0.0"
PORT = 8080
REDIS_HOST = HOST
REDIS_PORT = 6379
STREAM_MAX_LEN = 1000

app = FastAPI()

# 정적 파일 제공 (index.html 포함)
app.mount("/static", StaticFiles(directory="static"), name="static")

# Redis 연결 생성
redis = aioredis.from_url(f"redis://{REDIS_HOST}")

# 메시지 읽기 처리
async def read_message(websocket: WebSocket, join_info: dict):
    connected = True
    is_first = True
    stream_id = '$'
    while connected:
        try:
            count = 1 if is_first else 100

            # 메시지 수신
            results = await redis.xread(
                streams={join_info['room']: stream_id},
                count=count,
                block=100000
            )

            for room, events in results:
                if join_info['room'] != room.decode('utf-8'):
                    continue
                for e_id, e in events:
                    now = datetime.datetime.now()

                    await websocket.send_text(
                        f"{now.strftime('%H:%M')} {e[b'msg'].decode('utf-8')}"
                    )

                    stream_id = e_id
                    if is_first:
                        is_first = False
        except:
            await redis.close()
            connected = False

# 메시지 쓰기 처리
async def write_message(websocket: WebSocket, join_info: dict):
    await notify(join_info, 'joined')

    connected = True
    while connected:
        try:
            data = await websocket.receive_text()

            await redis.xadd(
                join_info['room'],
                {
                    'username': join_info['username'],
                    'msg': data
                },
                id=b'*',
                maxlen=STREAM_MAX_LEN
            )

        except:
            await notify(join_info, 'left')
            await redis.close()
            connected = False

# 입장/퇴장 알림 메시지
async def notify(join_info: dict, action: str):
    await redis.xadd(
        join_info['room'],
        {
            'msg': f"{join_info['username']} has {action}"
        },
        id=b'*',
        maxlen=STREAM_MAX_LEN
    )

# 쿼리 파라미터에서 username, room 추출
async def get_joininfo(username: str = None, room: str = None):
    return {"username": username, "room": room}

# 웹소켓 핸들러
@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket, join_info: dict = Depends(get_joininfo)):
    await websocket.accept()
    asyncio.set_event_loop_policy(uvloop.EventLoopPolicy())
    await asyncio.gather(
        write_message(websocket, join_info),
        read_message(websocket, join_info)
    )

# 애플리케이션 실행
if __name__ == "__main__":
    uvicorn.run(app, host=HOST, port=PORT)
