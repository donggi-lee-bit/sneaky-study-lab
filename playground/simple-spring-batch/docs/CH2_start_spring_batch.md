# 스프링 배치 시작

## 스프링 배치 활성화

- **@EnableBatchProcessing**
    - 스프링 배치를 작동하기 위해 선언해야할 어노테이션
    - 스프링 배치의 자동 설정 클래스 실행
    - 빈으로 등록된 모든 Job을 검색하여 초기화와 동시에 Job을 수행함
- **스프링 배치 설정 클래스**
    - BatchAutoConfiguration
        - 스프링 배치가 초기화될 때 자동으로 실행되는 설정 클래스
        - Job을 수행하는 JobLauncherApplicationRunner 클래스를 빈으로 생성
    - SimpleBatchConfiguration
        - JobBuilderFactory, StepBuilderFactory 클래스를 빈으로 생성
        - 스프링 배치의 주요 구성 요소를 생성 (프록시 객체로 생성)
    - BatchConfigurerConfiguration
        - BasicBatchConfigurer
            - SimpleBatchConfiguration에서 생성한 프록시 객체의 실제 대상 객체를 생성하는 설정 클래스
            - 빈으로 의존성 주입을 받아 주요 객체들을 참조해서 사용할 수 있음
        - JpaBatchConfigurer
            - Jpa 관련 객체를 생성하는 설정 클래스
        - 사용자 정의 BatchConfigurer 인터페이스를 구현하여 사용할 수 있음
- **스프링 배치 설정 클래스의 초기화 순서**
    - 1.@EnableBatchProcessing
    - 2.SimpleBatchConfiguration
        - 스프링 배치의 주요 구성 요소들을 프록시 객체로 생성
    - 3.BatchConfigurerConfiguration
        - 위에서 생성한 스프링 배치 주요 구성 요소의 프록시 객체의 실제 대상 객체를 생성
    - 4.BatchAutoConfiguration
        - 스프링 배치 Job을 수행하는 JobLauncherApplicationRunner 빈 생성

### **Spring Batch 5.0 업데이트로 인한 변경 사항**
- `@EnableBatchProcessing` 를 명시해주지 않아도 됨
- `JobBuilderFactory`, `StepBuilderFactory` deprecated

  → JobRepository를 명시적으로 제공하는 방식 권장 (기존에 Builder에서 JobRepository가 생성되고 설정되고 있었음)

<br></br>

## 스프링 배치 시작

### 스프링 배치 메타 데이터

- 스프링 배치의 실행 및 관리를 위한 목적으로 여러 도메인(Job, Step, JobParameters, ...) 정보를 저장, 업데이트, 조회할 수 있는 스키마 제공
- 배치 실행에 대한 성공과 실패 여부 등을 관리함으로서 배치를 운용하면서 리스크 발생 시 빠른 대처가 가능

### 스프링 배치 DB 스키마

- 파일 위치 : `/org/springframework/batch/core/schema.sql`
- DB 유형별 제공

### 스키마 생성 설정

- `spring.batch.jdbc.initialize-schema` 설정
  - `ALWAYS` : 스크립트를 항상 실행, RDBMS 설정이 되어 있을 경우 내장 DB보다 우선적으로 실행
  - `EMBEDDED` : 내장 DB일 때만 실행, 스키마 자동 생성, 기본값
  - `NEVER` : 스크립트 실행 X, 해당 옵션을 사용하여 운영에서 수동으로 스크립트 생성 후 설정하는 것을 권장