# 아이템 5 자원을 직접 명시하지 말고 의존 객체 주입을 사용하라

많은 클래스가 하나 이상의 자원에 의존한다.
가령 맞춤법 검사기는 사전에 의존하는데, 이런 클래스를 정적 유틸리티 클래스(아이템 4) 로
구현한 모습을 드물지 않게 볼 수 있다.

- 정적 유틸리티를 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다
  ```java
    public class SpellCheck{
        private static final Lexicon dictionary = ...;
        private SpellCheck() { } // 객체 생성 방지
        public static boolean isValid(String word){ }
        public static List<String> suggestions(String typo){}
    }
  ```
- 싱글턴을 잘못 사용한 예 - 유연하지 않고 테스트하기 어렵다
  ```java
    public class SpellChecker{
        private final Lexicon dictionary = ...;
        private SpellChecker(...) {}
        public static SpellChecker INSTANCE = new SpellChecker(...);
        public boolean isValid(String word) { ... }
        public List<String> suggestions(String typo) { ... }
    }
  ```
두 방식 모두 사전을 단 하나만 사용한다고 가정하다는 점에서 그리 홀륭해 보이지 않다.

SpellCheck 가 여러 사전을 사용할 수 있다고 만들어보자.

**사용하는 자원에 따라 동작이 달라지는 클래스에는 정적 유틸리티 클래스나 싱글턴 방식이 적합하지 않다.**

대신 클래스가 여러 자원(dictionary)을 사용해야 한다.
**바로 인스턴스를 생성할 때 생성지에 필요한 자원을 넘겨주는 방식이다.**

- 의존 객체 주입은 유연성과 테스트 용이성을 높여준다.
  ```java
  public class SpellChecker {
    private final Lexicon dictionary;
  
    public SpellChecker(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }
    ...
  }
  ```
dictionary라는 딱 하나의 자원만 사용하지만, 자원이 몇 개든 의존 관계가 어떻든 상관없이 잘 작동한다.
또한 불변(아이템 17)을 보장하여(같은 자원을 사용하는) 클라이언트가 의존 객체들을 안심하고 공유할 수 있다.

### 변형
생성자에 자원 팩터리를 넘겨주는 방식이 있다.
- 팩터리?
  - 호출할 때마다 특정 타입의 인스턴스를 반복해서 만들어주는 객체
  - 즉, 팩터리 메서드 패턴(Factory Method pattern)을 구현한것

자바 8에서 소개한 Supplier<T> 인터페이스가 팩터리를 표현한 완벽한 예.

### 의존 객체 주입 
의존 객체 주입이 유연성과 테스트 용이성을 개선해주지만, 의존성이 수 천개나 되는 큰 프로젝트에서는 코드를 어지럽게
만들기도 한다.

## 핵심 정리
클래스가 내부적으로 하나 이상의 자원에 의존하고, 그 자원이 클래스 동작에 영향을 준다면
싱글턴과 정적 유틸리티 클랫느느 사용하지 않는 것이 좋다. 이 자원들을 클래스가 직접 만들게 해서드 안 된다.
대신 필요한 자원을 (혹은 그 자원을 만들어주는 팩터리를) 생성자에 (혹은 정적 팩터리는 빌더에) 넘겨주자.

의존 객체 주입이라 하는 이 기업은 클래스의 유연성, 재사용성, 테스트 용이성을 기막하게 개선해준다.