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

<br></br>

### **Spring Batch 5.0 업데이트로 인한 변경 사항**
- `@EnableBatchProcessing` 를 명시해주지 않아도 됨
- `JobBuilderFactory`, `StepBuilderFactory` deprecated

  → JobRepository를 명시적으로 제공하는 방식 권장 (기존에 Builder에서 JobRepository가 생성되고 설정되고 있었음)