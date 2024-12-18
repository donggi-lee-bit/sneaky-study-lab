## spring bean lifecycle

`spring bean lifecycle`은 `spring bean`의 생명 주기를 말한다. bean은 `spring ioc container`에 의해 관리되며 생성, 초기화, 소멸 단계를 거치게 된다. ioc container에 의해 bean이 등록되려면 `bean definition`을 정의해야 한다.  
bean definition 정의는 **xml 기반**, @Bean @Configuration 어노테이션을 사용한 **Java Config 기반**, @Component 어노테이션을 사용한 **컴포넌트 스캐닝 기반** 방식이 있다.
bean lifecycle 과정은 다음과 같다.

- bean 정의 로딩
    - xml, java config, component scanning을 통해 bean definition을 읽어옴
    - 이때 bean의 클래스 타입, 프로퍼티, 스코프와 같은 메타데이터를 로드
- bean definition 등록
    - 로드된 bean definition을 `BeanDefinitionRegistry`에 저장
- bean definition 전처리
    - Bean을 인스턴스로 만들기 전 `BeanFactoryPostProcessor`를 통해 Bean 설정 정보를 검토하고 특정 조건에 맞게 변경
    - 이때 application.yml나 외부 파일에 정의된 값을 Bean 설정에 주입
- bean 객체 생성 및 초기화
    - bean definition을 기반으로 Bean 객체 생성
    - 생성한 bean을 DI을 통해 Bean 객체를 주입하는 초기화 작업 수행
- bean 후처리
    - 초기화가 끝난 bean 객체를 `BeanPostProcessor`를 이용해 값을 조작하거나 변경
    - 이때 bean 프록시 객체 생성
- bean 객체 사용
    - 애플리케이션 동작
- bean 소멸 전 콜백 실행
    - 애플리케이션 종료 시점(컨테이너 종료 시점)에 소멸 콜백 호출
    - 연결된 자원을 반납, 정리 작업을 수행한 뒤 모든 bean 소멸(프로그램 종료)
