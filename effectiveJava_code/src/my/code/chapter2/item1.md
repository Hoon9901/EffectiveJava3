# 아이템1 생성자 대신 정적 팩터리 메소드를 고려하라

## 장점

### 1. 이름을 가질 수 있다

- 객체의 특성을 쉽게 묘사할 수 있다

### 2. 호출될 때 마다 인스턴스를 새로 생성하지 않아도 된다

- 불변 클래스는 인스턴스를 미리 만들어 놓거나
- 새로 생성한 인스터를 캐싱하여 재활용하는 식으로 불필요한 객체 생성을 피할 수 있다.

### 3. 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다

- 반환할 객체의 클래스를 자유롭게 선택할 수 있게 하는 엄청난 유연성을 선물한다.

### 4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다

- 반환타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환하든 상관없다.

### 5. 정적 팩터리 메소드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다

- 이런 유연함은 서비스 제공자 프레임워크(service provider framework)를 만드는 근간
- 대표적인 서비스 제공자 프레임워크 : JDBC

## 단점

### 1. 상속을 할려면 생성자가 필요하니 정적 팩터리 메소드만 제공하면 하위클래스를 만들 수 없다

- 이 제약은 상속보다 컴포지션을 사용(아이템 18)하도록 유도하고
- 불변타입(아이템 17)으로 만들려면 이 제약을 지켜야한다는 점에서 오히려 장점

### 2. 정적 팩터리 메소드는 프로그래머가 찾기 어렵다

- 생성자 처럼 API 설명에 명확히 드러나지 않으니 
- 사용자는 정적 팩터리 메소드 클래스 방식 클래스를 인스턴스화 할 방법을 찾아야함

## 흔히 사용하는 명명 방식

- from : 매개변수를 하나 받아서 해당 타입의 인스턴스를 반환하는 형변환 메서드
  - `Date d = Date.from(instant);`
- of : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
  - `Set<Rank> faceCards = EnumSet.of(JACK, QUEEN, KING);`
- valueOf : from과 of의 더 자세한 버전
  - `BigInteger prime = BigInteger.valueOf(Integer.MAX_VALUE);`
- instance 혹은 getInstance : (매개변수를 받는다면) 매개변수로 명시한 인스턴스를 반환하지만, 같은 인스턴스임을 보장하지 않는다.
  - `StackWalker luke = StackWalker.getInstance(options);`
- create 혹은 newInstance : instance 혹은 getInstance와 같지만, 매번 새로운 인스턴스를 생성해 반환함을 보장한다.
  - `Object newArray = Array.newInstacne(classObject, arrayLength);`
- getType : getInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 사용. "Type" 은 팩터리 메서드가 반환할 객체의 타입이다.
  - `FileStore fs = Files.getFileStore(path);`
- newType : newInstance와 같으나, 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 쓴다. "Type" 은 팩터리 메서드가 반환할 객체의 타입이다.
  - `BufferedReader br = Files.newBufferedReader(path);`
- type : getType과 newType의 간결한 버전
  - `List<Complaint> litany = Collections.list(legacyLitany);`

## 핵심 정리

정적 팩터리 메서드와 public 생성자는 각 쓰임새가 있으니 상대적인 장단점을 이해하고
사용하는것이 좋다. 그렇다고 하더라도 정적 팩터리를 **사용하는게 유리한 경유가** 더 많으므로
무작정 public 생성자를 제공하던 습관이 있다면 고치자