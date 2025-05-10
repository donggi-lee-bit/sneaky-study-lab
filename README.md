# Project Structure
```
.
├── README.md
├── bookcase
│   ├── JVM-밑바닥까지-파헤치기
│   │   ├── 12장-자바-메모리-모델과-스레드.md
│   │   ├── 13장-스레드-안전성과-락-최적화.md
│   │   └── CH_11.md
│   ├── Real MySQL 8.0
│   │   └── 4. 아키텍처.md
│   ├── kotlin-in-action
│   │   ├── CH1_introduction_to_kotlin.md
│   │   ├── CH3_function_definition_and_call.md
│   │   └── CH4_class_object_interface.md
│   ├── 도메인-주도-개발-시작하기
│   │   ├── CH3_aggregate.md
│   │   └── CH6_application_layer_presentation_layer.md
│   ├── 만들면서-배우는-클린-아키텍처
│   │   ├── docs
│   │   │   ├── CH1_계층형_아키텍처의_문제.md
│   │   │   ├── CH2_의존성_역전하기.md
│   │   │   ├── CH3_코드_구성하기.md
│   │   │   ├── CH4_유스케이스_구현하기.md
│   │   │   └── images
│   │   │       ├── img.png
│   │   │       ├── img1.png
│   │   │       ├── img3.png
│   │   │       ├── img4.png
│   │   │       ├── img5.png
│   │   │       └── img6.png
│   │   └── hello
│   │       ├── build.gradle.kts
│   │       ├── gradle.properties
│   │       ├── gradlew
│   │       ├── gradlew.bat
│   │       ├── settings.gradle.kts
│   │       └── src
│   │           └── main
│   │               └── kotlin
│   │                   └── donggi
│   │                       └── example
│   │                           ├── ExampleApplication.kt
│   │                           ├── account
│   │                           │   ├── application
│   │                           │   │   ├── port
│   │                           │   │   │   └── in
│   │                           │   │   │       ├── SendMoneyCommand.kt
│   │                           │   │   │       └── SendMoneyUseCase.kt
│   │                           │   │   └── service
│   │                           │   │       └── SendMoneyService.kt
│   │                           │   └── domain
│   │                           │       ├── Account.kt
│   │                           │       ├── Activity.kt
│   │                           │       ├── ActivityWindow.kt
│   │                           │       └── Money.kt
│   │                           └── article
│   │                               ├── controller
│   │                               │   ├── ArticleController.kt
│   │                               │   └── dto
│   │                               │       └── ArticleCreateRequest.kt
│   │                               ├── domain
│   │                               │   ├── Article.kt
│   │                               │   └── ArticleRepository.kt
│   │                               ├── infrastructure
│   │                               │   └── ArticleJpaRepository.kt
│   │                               └── service
│   │                                   └── ArticleService.kt
│   └── 실전-레디스
│       └── CH1_레디스의_시작.md
├── build.gradle.kts
├── daily
│   ├── E20241021 HashMap.md
│   ├── E20241110 Inline.md
│   ├── E20241111 nginx rate limit.md
│   ├── E20241123 Bean을 인스턴스화 할 때 기본 생성자가 필요한 경우.md
│   ├── E20241202 로그인 인증.md
│   ├── E20241218 spring bean lifecycle.md
│   ├── E20241225 테스트 시 Spring Security Context가 필요할 경우.md
│   ├── E20241227 Elasticsearch 소개와 주소 검색이 처리되는 과정.md
│   ├── E20250314 Pinpoint 설치 가이드.md
│   └── E20250402 SQL 트랜잭션과 락의 동작 방식.md
├── gradlew
├── gradlew.bat
├── playground
└── settings.gradle.kts

30 directories, 54 files
```
