# 1장 오브젝트와 의존관계

1장에서는 스프링이 관심을 갖는 대상인 오브젝트의 설계와 구현, 동작 원리에 대해 집중적으로 알아본다. 이걸 바탕으로 스프링을 이해해보자.

## 사용자 정보를 DB에 넣고 관리하는 DAO

```java
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDao {

    public void add(User user) {
        Connection c = ...

        PreparedStatement ps = ...
        ps.setString(1, user.getId());
        ps.setString(2, user.getName);
        ps.setString(1, user.getPassword);

        ps.executeUpdate();

        ps.close();
        c.close();
    }
    
    public void get(String id) {
        Connection c = ...
        
        // SQL 쿼리 실행 결과를 ResultSet으로 받아서 저장할 오브젝트에 옮겨주고
        // add() 메서드처럼 생성된 Connection, PreparedStatement 등 리소스 작업이 끝나면 닫아주는 로직
    } 
}
```

사용자 정보를 DB에 넣고 관리하는 UserDao 클래스가 있다. 이 클래스가 제대로 동작하는지 확인해보고 싶다. 가장 간단한 방법으로 오브젝트 스스로 자신을 검증하도록 만들어준다.

### 테스트용 main() 메서드

```java
public class UserDao {
    
    public void add(User user) {
        // ...
    }
    
    public void get(String id) {
        // ...
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        
        User user = new User();
        user.setId("hello");
        user.setName("toby");
        user.setPassword("password1234");
        
        userDao.add(user);

        System.out.println(user.getId() + "등록 성공");
        
        // ...
    }
}
```
위처럼 오브젝트 내부에 main() 메서드를 만들어서 실행하면 성공 여부를 확인할 수 있다. 로직을 실행하는데 문제가 없다면 등록 성공이 출력될 것이다. 
<br><br>

`UserDao` 클래스 코드에는 여러 문제가 있다. 어떤 문제일까
- 잘 동작하는 코드를 개선해야 하는 이유는 뭘까
- 코드를 개선했을 때 얻는 장점은 뭘까
- 장점들이 지금, 그리고 미래에 주는 유익은 뭘까
- 객체지향 설계의 원칙과는 무슨 상관이 있을까
- 코드를 개선했을때와 그냥 사용했을때 스프링을 사용하는 개발에서 무슨 차이가 있을까