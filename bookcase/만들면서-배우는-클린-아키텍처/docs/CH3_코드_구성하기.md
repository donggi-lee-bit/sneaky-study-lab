# 3. 코드 구성하기

```bash
buckpal
├── domain
│   ├── Account
│   ├── Activity
│   └── AccountRepository
│   └── AccountService
├── persistence
│   └── AccountRepositoryImpl
└── web
    └── AccountController
```

- 웹 계층, 도메인 계층, 영속 계층을 각각 web, domain, persistence 패키지에 위치
- 위 구조가 최적의 구조가 아닌 3가지 이유
  1. 애플리케이션의 기능, 특성을 구분 짓는 패키지 경계가 없음
     - 사용자 관리 기능 추가시 web 패키지에 UserController, domain 패키지에 UserService, UserRepository, ... 와 같이 추가될 것임
     - 서로 연관되지 않은 기능들끼리 혼재하게 됨
  2. 애플리케이션이 어떤 유스케이스를 제공하는지 파악하기가 어려움
     - AccountService, AccountController가 어떤 유스케이스를 구현했는지 추측해야하며, 특정 기능을 찾기 위해서는 서비스 내에 어떤 메서드가 기능하고 있는지 찾아야함
  3. 패키지 구조만으로는 아키텍처를 파악할 수 없음
     - 코어로 향하는 인커밍(incoming) 포트와 코어로부터 어댑터로 향하는 아웃고잉(outgoing) 포트가 코드 속에 숨겨져 있음

<br></br>

**기능으로 구성하기**

```bash
buckpal
└── account
    ├── Account
    ├── AccountController
    └── AccountRepository
    └── AccountRepositoryImpl
    └── SendMoneyService
```

- 코드 최상위에 account 패키지를 추가하고, 계층 패키지를 없앰
- 각 기능을 묶은 새로운 도메인(?), 개념(?)은 account와 같은 레벨의 패키지로 추가됨
- 패키지 외부에서 접근되면 안되는 클래스에 package-private 접근 제어자를 이용해 패키지 간 경계를 강화할 수 있으며, 각 기능 사이의 불필요한 의존성을 방지할 수 있음
- AccountService의 책임을 좁히기 위해 SendMoneyService로 클래스명을 변경

<br></br>

**기능으로 구성된 패키지 방식의 문제점**

- 계층에 의한 패키징 방식보다 더 떨어지는 가시성 문제
  - 어댑터를 나타내는 패키지명이 없고, 인커밍 포트, 아웃커밍 포트를 확인할 수 없음
- 도메인 코드와 영속성 코드 간 의존성을 역전시켜 SendMoneyService가 AccountRepository 인터페이스만 알고 있게 했지만 package-private 접근 제어자를 이용해 도메인 코드가 실수로 영속성 코드에 의존하는 것을 막을 수 없음

<br></br>

**아키텍처적으로 표현력 있는 패키지 구조**

```bash
buckpal
└── account
    ├── adapter
    │   ├── in
    │   │   └── web
    │   │       └── AccountController
    │   ├── out
    │   │   └── persistence
    │   │       ├── AccountPersistenceAdapter
    │   │       └── SpringDataAccountRepository
    ├── domain
    │   ├── Account
    │   └── Activity
    └── application
        └── SendMoneyService
        └── port
            ├── in
            │   └── SendMoneyUseCase
            └── out
                ├── LoadAccountPort
                └── UpdateAccountStatePort
```

- 최상위에 Account 와 관련된 유스케이스를 구현한 모듈임을 나타내는 account 패키지가 존재
- application 패키지에 도메인 모델을 둘러싸는 서비스 계층이 포함하게 됨
  - SendMoneyService 는 인커밍 포트 인터페이스인 SendMoneyUseCase 를 구현하고, 아웃고잉 포트 인터페이스이자 영속성 어댑터에 의해 구현된 LoadAccountPort와 UpdateAccountStatePort를 사용함  
    -> 애플리케이션 계층이 인커밍 / 아웃고잉 어댑터 의존성을 갖지 않음(어댑터와 포트를 통해 통신)
- adapter 패키지에는 애플리케이션 계층의 인커밍 포트를 호출하는 인커밍 어댑터와 아웃고잉 포트에 대한 구현을 제공하는 아웃고잉 어댑터를 포함

<br></br>

**아키텍처-코드 갭(architecture-code gap) 혹은 [모델-코드 갭(model-code gap)](https://www.georgefairbanks.com/software-architecture/model-code-gap/)**

- 아키텍처적으로 표현력 있는 패키지 구조는 아키텍처-코드 갭 혹은 모델-코드 갭을 이야기할 수 있는 구조이다.
- 소프트웨어 개발 프로젝트에서 아키텍처는 소스 코드에 직접적으로 매핑될 수 없는 추상적 개념이라는 사실을 보여줌
  - 패키지 구조가 아키텍처를 반영할 수 없다면 코드는 점점 목표하던 아키텍처로부터 멀어지게 될 것임
