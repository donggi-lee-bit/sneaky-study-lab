# MySQL InnoDB Log에 대한 이해 - (1)

MySQL의 기본 스토리지 엔진인 InnoDB는 트랜잭션의 무결성과 영속성을 보장하기 위해 Redo Log를 생성하여 관리한다.  
Redo Log는 WAL(Write-Ahead Log)의 일종으로, WAL의 핵심 기능을 제공한다.  

## WAL(Write-Ahead Log)

WAL은 데이터를 실제로 변경하기 전에 변경 사항을 먼저 로그에 기록하는 방식인데,  
데이터 쓰기 작업에 앞서 변경 사항에 대해 로그 쓰기가 먼저 일어난다.

MySQL, PostgreSQL 등 대부분의 RDBMS에서는 WAL을 다음과 같은 목적으로 사용하고 있다.  
- 데이터 복구 : 시스템 장애 시 WAL을 통해 데이터 복원
- ACID 보장 : ACID 속성 중 특히 Durability 속성 보장

MySQL InnoDB는 WAL 개념을 이용해 Redo Log를 구현하고 있다.  

## Buffer Pool Flush List 처리 방식

트랜잭션 작업 중 데이터 변경이 발생할 경우 연관된 Page는 Dirty Page로 간주되어 flush list에 추가된다.  

이때 flush 동기, 비동기 방식으로 수행하게 되는데,  
일반적으로 비동기 방식으로 flush가 수행된다.  

비동기 flush는 백그라운드에서 Page Cleaner 스레드를 통해 실행하게 되는데,
시스템 환경 변수로 설정한 Dirty Page 개수가 임계점에 도달하거나, 
주기적인 백그라운드 작업 수행 또는 Buffer Pool 공간 확보가 필요할 때 수행하게 된다.  

동기 flush의 경우 새로운 Page를 읽어야 하는 상황에서  
Buffer Pool에 빈 공간이 없을 경우 flush list에 추가된 Dirty Page를 강제로 flush하게 된다.  

동기 flush는 그 외에도 Redo Log 공간이 부족할 경우와 MySQL 종료 시점에도 수행하게 된다.  

## 트랜잭션 롤백 시 동작 방식

MySQL은 트랜잭션 작업 중 데이터 변경 시 Undo Log에 변경 이전 값을 저장한다.  
이 Undo Log를 이용해 트랜잭션 롤백 시 데이터를 원래 상태로 복구하게 된다.  

### Ref

- https://tech.kakao.com/posts/721
