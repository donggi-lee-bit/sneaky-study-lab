# Spring Security Filter에서 발생한 예외가 @ControllerAdvice에서 처리될 수 있었던 이유

spring security FilterChain에서 발생한 예외가 예상과 달리 ControllerAdvice에 정의한 핸들 메서드로 예외 처리가 되고 있어서 Filter 에서 발생한 예외 처리 방식에 대해 알아보았고, 다음과 같은 절차로 예외 처리가 되고 있는 것을 확인했다.

- FilterChain에서 예외 발생 시 `ExceptionTranslationFilter` 를 통해 예외를 감지
- ExceptionTranslationFilter에서 예외 처리를 시도
  - `AuthenticationEntryPoint` 를 호출하여 인증 실패를 처리
  - `AccessDeniedHandler` 를 호출하여 권한 부족 예외를 처리
  - 예외 처리가 되지 않을 경우 FilterChain 내에 등록된 나머지 필터에 예외를 전파
- FilterChain의 최상위 필터까지 예외를 처리하지 못할경우 예외가 `DispatcherServlet` 으로 전달
- DispatcherServlet에서 Spring 컨텍스트의 `HandlerExceptionResolver` 로 예외 전달
  - HandlerExceptionResolver 중 `ExceptionHandlerExceptionResolver` 를 통해 예외가 `@ControllerAdvice` 내에 정의된 `@ExceptionHandler` 핸들 메서드가 실행되어 예외가 처리됨
