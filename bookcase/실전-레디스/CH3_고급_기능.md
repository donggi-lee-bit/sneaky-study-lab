# 3. 고급 기능

## 3.1 파이프라인

이전 요청의 응답을 기다리지 않고 새로운 요청을 보낼 수 있는 기능  
여러 명령어를 동시에 처리하여 네트워크 비용을 줄일 수 있음  
- 조건 분기 등을 적용할 수 없어 복잡한 로직 작성이 어려움
- 파이프라인에 쓰기와 읽기 명령이 모두 포함된 경우 쓰기 명령 실행 전
  읽기 명령의 결과를 기다려야 하므로 지연 시간이 길어질 수 있음
- 여러 클라이언트로부터 서로 다른 명령어가 간섭할 수 있어 스크립트의
  원자적 처리를 보장할 수 없음

복잡한 로직 혹은 원자적 처리가 필요할 경우 다음 기술을 사용
- 트랜잭션
- 루아 스크립팅
- 모듈

## 3.2 루아

레디스의 내장 스크립트 언어  
레디스 명령어만으로 처리하기 어려운 조건 분기 등의 명령어를 원자적으로 처리할 수 있음  

### EVAL vs EVALSHA

루아 스크립트 실행 시 스크립트를 레디스에 어떻게 전달하는가의 차이가 있음  
- EVAL : 루아 스크립트 코드를 직접 레디스에 전달하여 실행
- EVALSHA : 스크립트의 SHA1 해시 값을 이용해서 실행 (스크립트는 이미 레디스에 로드되어 있어야함)

1. EVAL 예시
```
EVAL "return ARGV[1]" 0 "hello"

-> "hello"
```

2. EVALSHA 예시
```
# 1. EVAL로 스크립트를 먼저 로드하여 해시를 반환
SCRIPT LOAD "return ARGV[1]"

-> fd8f2b6e60c059c0f13c9a2ea3b71f5c1f8e14a4

# 2. EVALSHA 실행
EVALSHA fd8f2b6e60c059c0f13c9a2ea3b71f5c1f8e14a4 0 "hello"

-> "hello"
```

EVAL 명령어는 매번 루아 스크립트를 레디스 서버로 전송해야하기 때문에 오버헤드가 크다는 단점이 있음  

하지만 스크립트 크기와 네트워크 대역폭 절약 효과가 크지 않을 경우 관리 비용을 절약하기 위해 EVAL 명령어를  
사용하는 것도 하나의 전략이 될 수 있음  

또한 EVALSHA 명령어는 해시값을 가지고 오기 위해 SCRIPT LOAD 명령어를 실행해야 하므로 레디스 서버와의 네트워크 통신 횟수가 증가하는 경향이 있음  

따라서 통신 횟수를 줄이기 위해서 EVAL 명령어를 사용하는 경우도 있음

### 스크립트 정지

```bash
# 스크립트 무한 반복 실행
127.0.0.1:6379> EVAL 'redis.call("SET", KEYS[1], "bar"); while 1 do redis. debug("infinite loop") end' 1 foo

# 레디스 서버 상태 확인
127.0.0.1:6379> PING
(error) BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE.

# 스크립트 강제 종료 시도 (이미 쓰기 연산이 수행되어 데이터셋이 변경되어 종료 불가)
127.0.0.1:6379> script kill
(error) UNKILLABLE Sorry the script already executed write commands against the dataset. You can either wait the script termination or kill the server in a hard way using the SHUTDOWN NOSAVE command.

# 레디스 서버 강제 종료 (데이터 저장 없이 강제 종료)
127.0.0.1:6379> shutdown nosave
```
