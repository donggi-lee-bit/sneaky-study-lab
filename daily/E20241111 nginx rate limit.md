## nginx rate limit

- 사내 라벨링 툴이 실행 중인 서버는 동시에 모델 학습에도 사용되고 있음
- 라벨링 툴에 많은 요청으로 인해 모델 학습에 영향을 최소화하면서 라벨링 툴의 기본 응답 성능을 보장하고자 최소한의 요청 수를 제한하여 트래픽을 조절

```bash
# /etc/nginx/nginx.conf

events {
    worker_connections 1024;
}

http {
    limit_req_zone $binary_remote_addr zone=req_limit:10m rate=5r/s;

    include /etc/nginx/conf.d/*.conf;
}
```
- `limit_req_zone $binary_remote_addr zone=req_limit:10m rate=5r/s;`
  - 클라이언트 IP마다 초당 5개의 요청을 허용
  - 클라이언트의 요청을 관리하는 10mb 메모리 영역 정의(10mb == 약 16만 개의 IP 주소)

```bash
# /etc/nginx/conf.d/app.conf

server {
    listen <port>; # 사용자의 요청을 수신할 host 서버의 port
    server_name <server-ip>;

    location / {
        limit_req zone=req_limit burst=10 nodelay;

        proxy_pass http://<container-name>:<port>; # 다중 인스턴스를 운영할 경우 upstream 블록 이름을, 단일 인스턴스일 경우 컨테이너 이름을, 호스트 서버에서 직접 실행될 경우 127.0.0.1:<port>
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
- `limit_req zone=req_limit burst=10 nodelay;`
  - 기본 요청을 초당 5개로 제한
  - `burst=10`: 순간적으로 많은 요청이 몰릴 시 최대 10개 요청까지 대기했다 처리
  - `nodelay`: 초과 요청은 딜레이 없이 빠르게 응답

```bash
# 설정 테스트
nginx -t

# 테스트가 정상적일 경우 설정 적용
nginx -s reload
```
