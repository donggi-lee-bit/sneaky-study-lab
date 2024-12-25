# 테스트 시 Spring Security Context가 필요할 경우

`@WithUserDetails` 

- 테스트 코드에서 Security Context 중 UserDetails 설정이 필요할 경우 사용
- default는 `TestExecutionEvent.TEST_METHOD`로 테스트 메서드가 실행되기 전 동작을 수행
- `BeforeEach` 와 동일한 시점에 동작하지만, TestExecutionEvent가 먼저 실행되고 있음
- BeforeEach 이후에 실행을 기대할 경우 `TestExecutionEvent.TEST_EXECUTION`으로 설정
  - TEST_EXECUTION: 테스트 실행 중 Security Context가 필요한 시점에 설정
