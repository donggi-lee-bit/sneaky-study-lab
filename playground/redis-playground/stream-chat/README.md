# 레디스를 이용한 실시간 채팅

Redis Streams를 이용하여 실시간 채팅 기능을 구현한다.  

Redis Streams를 사용하는 이유
- 하나의 채팅방에 다수의 클라이언트를 대응하기 위해
- 채팅방에 연결되지 않는 동안 수신한 메시지를 나중에 가져오기 위해

### 기술 스택

- 레디스 클라이언트: aioredis
- 웹 프레임워크: FastAPI

### 실행 방법

#### 1. python 가상 환경 실행

```bash
# 가상 환경 생성
python3 -m venv <myenv>

# 가상 환경 활성화
source myenv/bin/activate

# 패키지 설치
pip3 install -r requirements.txt

# 파이썬 서버 실행
python3 streams-chat.py
```


