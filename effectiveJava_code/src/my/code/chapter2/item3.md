# 아이템 3 private 생성자나 열거 타입으로 싱글턴임을 보증하라

싱글턴(singleton)이란 인스턴스를 오직 하나만 생성할 수 있는 클래스를 말한다.
**그런데 클래스를 싱글턴으로 만들면 이를 사용하는 클라이언트를 테스트하기가 어려워질 수 있다.**

싱글턴을 만드는 방식은 보통 둘 중 하나다. 
두 방식 모두 생성자는 `private`으로 감춰두고, 유일한 인스턴스에 접근할 수 있는 수단으로 `public static` 멤버를 하나 마련해둔다.
## 1. public static 멤버가 final 필드인 방식
```java
    public class Elvis {
        public static final Elvis INSTANCE = new Elvis();
        private Elvis() { ... }

        public void leaveThebuilding() { ... }
    }
```
`private` 생성자는 `public static final` 필드인 `Elvis.INSTANCE`를 초기화할 때 딱 한 번만 호출된다.

public 이나 protected 생성자가 없으므로 Elvis 클래스가 초기화될 때 만들어지는 인스턴스가 전체 시스템에서
하나뿐임을 보장한다.

클라이언트는 손 쓸 방법이 없다.

예외는 단 한 가지, 권한이 있는 클라이언트는 리플렉션 API(아이템 65)인 
`AccessibleObject.setAccessible을` 사용해 private 생성자를 호출할 수 있다. 

이러한 공격을 방어하려면 생성자를 수정하여 두 번째 객체가 생성되려 할 때 예외를 던지게 하면 된다.

### 장점
1. public static 필드가 final이 절대로 다른 객체를 참조할 수 없다.
2. 간결함

### 리플렉션 API?

## 2. 정적 팩터리 메서드를 public static 멤버로 제공
```java
    public class Elvis {
        private static final Elvis INSTANCE = new Elvis();
        private Elvis() { ... }
        public static Elvis getInstance() { return INSTANCE; }

        public void leaveThebuilding() { ... }
    }
```
Elvis.getInstance는 항상 같은 객체의 참조를 반환하므로 제2의 Elvis 인스턴스란 결코 만들어지지 않는다.
(역시, 리플렉션을 통한 예외는 똑같이 적용)


### 장점
1. API를 바꾸지 않고도 싱글턴이 아니게 변경할 수 있다는 점이다. (스레드별로 다른 인스턴스를 넘겨주게 할 수 있다)
2. 원한다면 정적 팩터리를 제네릭 싱글턴 팩터리로 만들 수 있다.
3. 정적 팩터리의 메서드 참조를 공급자(supplier)로 사용할 수 있다.
   - Elvis::getInstance 를 Supplier<Elvis>로 사용하는 식 (아이템 43,44)

이러한 장점들이 굳이 필요하지 않다면 public 필드 방식이 좋다.

## 싱글턴을 직렬화
둘 중 하나의 방식으로 만든 싱글턴 클래스를 직렬화(12장)하려면, 단순히
Serializable을 구현한다고 선언하는 것만으로는 부족하다.

모든 인스턴스 필드를 일시적(transient)로 선언, readResolve 메서드를 제공한다(아이템 89)

### 위 방법을 하지않으면 문제점

- 직렬화된 인스턴스를 역직렬화할 때 마다 새로운 인스턴스가 만들어진다.
- 정적 팩터리 메서드를 제공한 싱글턴 코드에서 가짜 Elvis가 탄생한다는 뜻

```java
// 싱글턴임을 보장해주는 readResolve 메서드
private Object readResolve(){
        // '진짜' Elvis를 반환, '가짜' Elvis는 가비지 컬렉터에 맡긴다
        return INSTANCE;
}
```



## 3. 원소가 하나인 열거타입을 선언
```java
    private Object readResolve() {
        return INSTANCE;
    }

    public enum Elvis {
        INSTANCE;
        public void leaveTheBuilding() { ... }
    }
```
조금 부자연스러워 보일 수는 있으나 **대부분 상황에서는 원소가 하나뿐인 열거 타입이 싱글턴을 만드는 가장 좋은 방법이다.**

단, 만들려는 싱글턴이 Enum 외의 클래스를 상속해야 한다면 이 방법은 사용할 수 없다.
(열거타입이 다른 인터페이스를 구현하도록 선언할 수는 있다.)
