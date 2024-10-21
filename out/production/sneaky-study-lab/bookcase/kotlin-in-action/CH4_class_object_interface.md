# 클래스, 객체, 인터페이스

## 4.1 클래스 계층 정의

### 4.1.2 open, final, abstract 변경자: 기본적으로 final

- **취약한 기반 클래스(fragile base class) 문제**
    - 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 깨져버린 경우
    - 클래스를 상속하는 방법에 대해 정확한 규칙을 제공하지 않는다면 기반 클래스를 작성한 사람의 의도와 다른 방식으로 메서드를 오버라이드할 위험 존재
    - 이펙티브 자바에서는 상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하라라는 조언

      → 하위 클래스에서 오버라이드하게 의도된 클래스와 메서드가 아니라면 모두 `final`로 만들라는 뜻

- **코틀린의 상속**
    - 코틀린에서 클래스의 상속을 허용하려면 클래스 앞에 `open` 변경자를 붙여야 함
    - 오버라이드를 허용하고 싶은 메서드나 프로퍼티 앞에도 `open` 변경자를 붙여야 함
    - 오버라이드하는 메서드의 구현을 하위 클래스에서 오버라이드하지 못하게 금지하려면
        - 오버라이드하는 메서드 앞에 `final` 명시

### 4.1.3 가시성 변경자: 기본적으로 공개

- **코틀린의 가시성 변경자**
    - 기본 가시성
        - 코틀린: `public`
        - 자바: `package-private` (패키지 전용)
        - 코틀린의 패키지 전용 가시성 : `internal`
    - internal
        - 모듈 내부에서만 볼 수 있음 (모듈 → 한꺼번에 컴파일되는 코틀린 파일)
        - 모듈 구현에 대한 진정한 캡슐화를 제공
- **최상위 선언**
    - 자바: 클래스
    - 코틀린: 클래스, 프로퍼티, 함수
    - 코틀린에서의 최상위 선언
        - `private` 가시성을 허용
            - 선언이 들어있는 파일 내부에서만 사용 가능
            - 하위 시스템의 자세한 구현 사항을 외부에 감추고 싶을 때 유용한 방법
- **가시성에 따른 타입 참조**
    - 자신보다 가시성이 더 낮은 타입을 참조하고자 하면 컴파일 에러가 발생
        - e. g. public 함수 안에서 internal 타입 참조 X
- **protected**
    - 자바: 같은 패키지 안에서 접근 가능
    - 코틀린: 어떤 클래스나 그 클래스를 상속한 클래스 안에서만 접근 가능

      → 클래스를 확장한 함수는 그 클래스의 `private`이나 `protected` 멤버에 접근할 수 없음

### 4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

- **자바의 중첩 클래스**
    - 클래스 안에 정의한 클래스는 자동으로 `inner` 클래스
        - 바깥쪽 클래스에 대한 참조를 묵시적으로 포함
    - 중첩 클래스를 `static` 으로 선언 시 바깥쪽 클래스에 대한 참조가 사라짐
- **코틀린의 중첩 클래스**
    - 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없음
    - 바깥쪽 클래스에 대한 참조를 하고 싶다면 `inner` 변경자를 붙여야 함
    - inner 클래스에서 바깥쪽 클래스(e. g. Outer)의 참조에 접근하려면 `this@Outer` 와 같이 써줘야 함

### 4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

- **클래스 계층 정의 시 디폴트 분기 처리**
    - 클래스 계층 정의 시  `when` 식에서 모든 하위 클래스를 처리해줘야 함
    - 코틀린 컴파일러는 `when` 식을 사용할 때 디폴트 분기인 `else` 분기를 덧붙이도록 강제되고 있음 (`else` 분기에서는 반환할 만한 의미 있는 값이 없으므로 예외를 반환)
    - 이후 새로운 하위 클래스가 추가되고, 클래스 처리를 잊어버리더라도 디폴트 분기가 선택되기 때문에 심각한 버그 발생 위험 존재
- **`sealed` 클래스**
    - 상위 클래스에 `sealed` 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있음
    - `sealed` 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 함
    - `sealed` 클래스는 기본적으로 `open` 변경자
    - `when` 식에서 모든 하위 클래스 처리 시 디폴트 분기(`else` 분기)가 필요 없음
    - 이후 새로운 하위 클래스를 추가되면 `when` 식을 고쳐야 정상 작동

      → 버그를 예방할 수 있게 됨

<br></br>

## 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

- **코틀린의 생성자**
  - 주 생성자 (primary): 클래스를 초기화할 때 사용하는 간략한 생성자, 클래스 본문 밖에 정의
  - 부 생성자 (secondary): 클래스 본문 안에 정의
  - 초기화 블록 (initializer block)

### 4.2.1 클래스 초기화: 주 생성자와 초기화 블록

```kotlin
class User(val nickname: String)
```

- 클래스 이름 뒤에 오는 괄호로 둘러싸인 코드를 `주(primary) 생성자`
- 정의 목적
  1. 생성자 파라미터 지정
  2. 생성자 파라미터에 의해 초기화되는 프로퍼티 정의

```kotlin
// 1.
class User constructor(_nickname: String) {
    val nickname: String
    
    init {
        nickname = _nickname
    }
    // val nickname: String = _nickname 와 같이 선언하여 init 키워드 생략 가능
}

// 2. 생성자 파라미터 이름 앞에 val을 추가하면 프로퍼티 정의와 초기화를 간략히 할 수 있음
class User(val nickname: String)

// 3. 생성자 파라미터 디폴트 값 정의
class User(val nickname: String = "hello")
```

- `constructor` 키워드
  - 주 생성자, 부 생성자 정의 시 사용
  - 주 생성자 앞에 별도의 애너테이션이나 가시성 변경자가 없으면 생략 가능
- `init` 키워드
  - 클래스의 객체가 만들어질때 실행될 초기화 코드가 들어감

<br></br>

- **클래스와 기반 클래스(base class) 생성자**
  - 기반 클래스를 초기화하려면 기반 클래스로 생성자 인자를 넘겨야 함
  - 기반 클래스에 생성자 정의를 하지 않는 경우 컴파일러가 디폴트 생성자를 만들어줌

    → 이러한 이유로 기반 클래스의 이름 뒤에는 빈 괄호가 들어감
    ```kotlin
    open class User(val nickname: String) { ... }
    // 기반 클래스에 생성자 인자를 넘겨 초기화
    class KakaoUser(nickname: String) : User(nickname) { ... }
    ```
  - 인터페이스는 생성자가 없음
    - 인터페이스 확장 시 아무 괄호가 없음
    - 상위 클래스, 인터페이스 목록에서 이름 뒤 괄호 유무를 통해 기반 클래스와 인터페이스를 쉽게 구별 가능
- **private 생성자**
  - 클래스를 클래스 외부에서 인스턴스화하지 못하게 막고 싶다면 생성자를 `private` 으로 생성
    
    → private 생성자 호출이 좋은 이유에 대한 설명 `4.4.2 동반 객체: 팩토리 메서드와 정적 멤버가 들어갈 장소`

## 4.3 컴파일러가 생성한 메서드: 데이터 클래스와 클래스 위임

### 4.3.2 데이터 클래스: 모든 클래스가 정의해야 하는 메서드 자동 생성

- 코틀린의 `data` 변경자
  - `data` 라는 변경자를 클래스 앞에 붙이면 `toString`, `equals`, `hashCode` 메서드를 `컴파일러`가 자동으로 생성
  - 이를 데이터 클래스라고 칭함
  - equals와 hashCode는 주 생성자에 나열된 프로퍼티를 고려해 만들어짐
    - 주 생성자 밖에 정의된 프로퍼티는 고려 대상이 아니니 주의
- 데이터 클래스의 불변성
  - 데이터 클래스 프로퍼티를 var 로 사용 가능하나, 모든 프로퍼티를 읽기 전용으로 만들어 불변 클래스로 만드는 것을 권장
  - HashMap 등의 컨테이너에 데이터 클래스 객체를 담을 때 프로퍼티에 변경이 생기면 컨테이너 상태가 잘못될 수 있음
- `copy()` 메서드
  - 객체를 복사하면서 일부 프로퍼티를 변경할 수 있게 해주는 메서드 (코틀린 컴파일러 제공)
  - 데이터 클래스 인스턴스를 불변 객체로 유지할 수 있게 해줌
  - 
   
## 4.4 object 키워드: 클래스 선언과 인스턴스 생성

- **코틀린의 object 키워드**
  - 클래스를 정의함과 동시에 인스턴스를 생성
- **object 키워드를 사용하는 상황**
  - 객체 선언(object declaration)
  - 동반 객체(companion object)
  - 무명 객체(anonymous object)

### 4.4.1 객체 선언: 싱글턴을 쉽게 만들기

- **객체 선언**
  - `클래스 선언`과 그 클래스에 속한 `단일 인스턴스의 선언`을 합친 선언
  - `object` 키워드로 시작하여, 클래스를 정의하고 그 클래스의 인스턴스를 만들어 변수에 저장하는 모든 작업을 한 문장으로 처리
  - 객체 선언 시 생성자 사용 X (주 생성자, 부 생성자 모두)
  - 객체 선언에 클래스, 인터페이스 상속 가능
  - 클래스 안에서 객체 선언을 할 수도 있음
    ```kotlin
    // 클래스 안에서 객체 선언을 하는 경우
    data class Person(val name: String) {
        object NameComparator : Comparator<Person> {
            override fun compare(p1: Person, p2: Person): Int =
                p1.name.compareTo(p2.name)      
        }
    }
    ```

### 4.4.2 동반 객체: 팩토리 메서드와 정적 멤버가 들어갈 장소

- **코틀린의 정적 멤버**
  - 코틀린 클래스 안에는 정적인 멤버가 없음 (`static` 지원 X)
  - static 키워드 대신 패키지 수준의 최상위 함수와 객체 선언을 활용 (최상위 함수 사용을 더 권장)
  - 최상위 함수는 `private` 으로 표시된 클래스 비공개 멤버에 접근할 수 없음
- **코틀린 동반 객체**
  - 클래스 안에 정의된 객체 중 `companion` 표시를 붙이면 그 클래스의 동반 객체로 만들 수 있음
  - 동반 객체의 프로퍼티나 메서드에 접근하기 위해선 객체가 정의된 클래스 이름을 사용해야함 (자바의 정적 메서드 호출과 같음)
  ```kotlin
  class A {
    companion object {
        fun bar() {
            println("hello world")
        }
    }
  }
  
  >> A.bar()
  hello world
  ```
- **private 생성자와 동반 객체**
  - 동반 객체는 정의된 클래스의 모든 `private` 멤버에 접근 가능 (private 생성자 호출 가능)
    ```kotlin
    class A private constructor(
        private val nickname: String
    ) {
        fun getNickname(): String {
          return this.nickname
        }
        
        companion object { 
            fun of(nickname: String): A {
                    return A(nickname = nickname)
            }
        }
    }
    
    fun main() {
    val nickname = "Main World"
    val a = A.of(nickname)
    println(a.getNickname())

    ```

### 4.4.3 동반 객체를 일반 객체처럼 사용

- **일반 객체처럼 사용 가능한 동반 객체**
  - 1.동반 객체에 이름 붙이기
    ```kotlin
    class Person(val name: String) {
      companion object Loader {
        fun fromJSON(jsonText: String): Person = ...
      }
    }

    // 동반 객체에 이름 명시한 경우 사용 예시
    >>> person = Person.Loader.fromJSON("")
    >>> person2 = Person.fromJSON("") // 동반 객체에 명시한 이름을 호출하지 않아도 fromJSON() 호출 가능
    ```
  - 2.동반 객체에서 인터페이스 구현
    - 다양한 타입의 객체를 동일한 방식으로 생성해야할 때 인터페이스를 활용할 수 있음
    ```kotlin
    interface JSONFactory<T> {
      fun fromJSON(jsonText: String): T
    }

    // 동반 객체에서 인터페이스를 구현
    class Person(val name: String) {
      companion object : JSONFactory<Person> {
        override fun fromJSON(jsonText: String): Person = ...
      }
    }
    ```
  - 3.동반 객체 확장
    - 동반 객체 안의 함수를 확장 함수로 만들어 관심사를 분리할 수 있음
    - 동반 객체에 대한 확장 함수 정의 시 원래 클래스에 빈 객체라도 동반 객체 선언이 필요함
    ```kotlin
    // 비즈니스 로직 모듈
    class Person(val firstName: String, val lastName: String) {
      companion object { // 비어있는 동반 객체 선언

      }
    }

    // 확장 함수 선언
    fun Person.Companion.fromJSON(json: String): Person {

    }

    // 동반 객체에 대한 확장 함수 호출 예시
    val p = Person.fromJSON(json)
    ```
  - 자바에서 코틀린 동반 객체 호출
    ```Java
    // 동반 객체에 이름을 붙이지 않았다면 자바에서 Companion 이름으로 참조에 접근 가능
    Person.Companion.fromJSON("...");
    ```