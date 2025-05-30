# 레디스의 시작

## 레디스를 사용하기까지

Redis(Remote Dictionary Server)
- BSD(Berkeley Software Distribution) 라이선스의 오픈소스 소프트웨어
- ANSI C로 작성
- In-Memory Key-Value NoSQL로, 다양한 자료형 제공

</br>

## 레디스의 특징
 
### 1. 빠른 속도

- 인메모리 기반으로 디스크 I/O 병목을 피함
- 단일 스레드 이벤트 루프 기반으로 높은 처리량 제공
- 대부분의 연산이 마이크로초 단위로 응답

### 2. 다양한 자료형 지원

- String, List, Set, Sorted Set (ZSet), Hash, Bitmap, HyperLogLog, Stream, Geo
- 각각의 자료형에 맞는 명령어 제공으로 유연한 데이터 처리 가능

### 3. 실시간성이 중요한 애플리케이션에 적합

- 캐시, 세션 저장소, 실시간 랭킹 시스템, 메시지 브로커 등에 활용
- pub/sub 기능으로 실시간 메시징 시스템 구축 가능

### 4. 디스크 병목 회피

- 메모리에 데이터를 저장하여 디스크 접근 병목 없음
- 스냅샷이나 명령어 로그를 이용해 디스크에 데이터를 저장하고,  
  장애 발생 시 이를 통해 복구 가능

### 5. 트레이드오프

- 레디스 메모리는 SSD/HDD보다 비용이 높아 대용량 데이터 저장엔 부적합
- SQL과 같은 직관적인 질의 언어 없음 (키 기반 접근)
- 제한된 트랜잭션 기능 (MULTI/EXEC → 롤백 없음, 격리 수준 낮음)
- 디스크 기반 DB 대비 영속성 측면에서 약함
- 단일 스레드 구조 -> CPU 병렬 처리는 클러스터 구성 필요

</br>

### 레디스 활용

- RDBMS에서 여러 테이블을 조인한 결과가 여러번 사용되는 경우,  
  레디스에 캐싱하면 빠른 속도로 응답할 수 있음
- 예) 소셜 게임 랭킹에서 레디스의 Sorted Set형을 활용

</br>

### 루아를 통한 유연한 처리

- 레디스 명령어를 조합하여 복잡한 로직을 서버 측에서 실행 가능
- 여러 레디스 명령을 원자적으로 실행
- (like RDBMS의 스토어드 프로시져)

</br>

### 싱글 스레드 기반 요청 이벤트 주도 처리
 
Redis는 싱글 스레드 기반이지만, 이벤트 루프를 만들어 많은 요청을 처리할 수 있음
- 싱글 스레드는 동시성 문제 없이 단순한 구조를 유지하면서 예측 가능한 성능을 제공

다만 멀티코어 환경에서는 단일 인스턴스의 한계가 있어, 레디스 클러스터를 구성하거나 여러 인스턴스로 샤딩하여 수평 확장이 필요  

Redis 6.0부터는 클라이언트 I/O를 멀티스레드로 처리할 수 있게 됨
- 요청량이 많은 환경에서 네트워크 처리 병목을 완화할 수 있음
- 하지만 데이터 명령 처리는 여전히 싱글 스레드이며, 모든 경우에 멀티스레드 I/O가 유리한 것은 아님

</br>

### 레디스와 RDBMS 비교

RDBMS의 ACID vs 레디스의 ACID
- RDBMS는 완전한 ACID 트랜잭션을 보장하며, 복잡한 비즈니스 로직과 데이터 무결성에 적합함
- 레디스는 경량 트랜잭션 수준의 ACID를 지원하고, 일관성과 지속성은 상대적으로 약함

레디스는 ACID보단 BASE 모델에 더 가까움  
BASE 모델은 분산 시스템에서 ACID 모델의 대안으로 등장한 개념으로, 느슨한 일관성과 높은 가용성을 추구함  

NoSQL, 레디스, 카산드라, DynamDB와 같은 시스템에서 자주 사용됨  

BASE는 다음 3가지 원칙으로 구성
- B: Basically Available
  - 시스템이 항상 응답하지만, 그 응답이 항상 일관되지 않을 수 있음
- A: Soft State
  - 시스템 상태는 즉시 일관되지 않아도 되며, 시간에 따라 바뀔 수 있음
- SE: Eventual Consistency
  - 시간이 지나면 결국 일관성이 보장됨

레디스의 BASE 모델 특성
- 빠른 속도와 높은 가용성 확보를 위해 일관성 일부를 포기함
- 마스터-레플리카 복제 구조에서는 latency로 인해 데이터 일관성이 즉시 맞춰지지 않을 수 있음
- 장애 발생 시에도 서비스를 중단하지 않고 응답을 계속 제공함  
  -> 일관성 문제는 Eventual Consistency 하게 처리됨

</br>

## 레디스 동작 테스트

```bash
# docker 실행된 상태 기반

# 도커를 이용해 레디스 서버 실행
➜ docker run -d --name redis-server -p 6379:6379 redis 

# redis-cli로 redis-server 접속
➜ docker exec -it redis-server redis-cli

# redis 명령어 테스트
127.0.0.1:6379> set foo bar
OK
127.0.0.1:6379> get foo
"bar"
127.0.0.1:6379> shutdown
not connected>

```

</br>

## 레디스 라이센스 이슈

레디스는 최초 BSD 라이센스로, 누구나 자유롭게 사용/수정/배포가 가능했음  

Redis 7.2.4 이후, Redis를 개발하는 기업인 Redis Inc는 레디스 라이센스를 RSAL + SSPL 조합으로 변경  
- 이로 인해 Redis를 Fork한 Valkey 프로젝트가 등장  

Redis와 Valkey는 2024년 이후 서로 다른 방향으로 파편화되기 시작  
Valkey는 기존 레디스의 오픈 소스 소프트웨어 철학을 이어감  

Redis Inc는 Redis 8부터 AGPLv3를 추가하여 레디스 프로젝트를 다시 오픈 소스로 전환  
- (AGPLv3는 사용 조건이 까다로운 오픈소스 라이센스라고 함)

[https://github.com/redis/redis](https://github.com/redis/redis)  
[https://github.com/valkey-io/valkey](https://github.com/valkey-io/valkey)

## 발표 자료

[Google Drive 링크](https://docs.google.com/presentation/d/1KMIfukNUiUe038M5wevO53MFjVYG2EyDMFoh_Arjbdc/edit?usp=sharing)
