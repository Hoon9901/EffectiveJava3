# 아이템2 생성자에 매개변수가 많다면 빌더를 고려하라

## 1. 점층적 생성자 패턴

정적 팩터리와 생성자에는 똑같은 제약이 하나 있다. **선택적 매개변수가 많을 때 적절히 대응하기 어렵다는 점.**

이런 클래스용 생성자 혹은 정적 팩터리는 어떤 모습일까? (매개변수가 많을 때)? 

프로그래머들은 이럴 때 **점층적 생성자 패턴(telescoping constructor pattern)** 을 즐겨 사용했다.

- 점층적 생성자 패턴 : 필수매개 변수만 받는 생성자, 1개 선택적 매개변수, 2개 선택적 매개변수, N개의 선택적 매개변수 형태로 선택 매개변수를 전부다 받는 생성자까지 늘려가는 방식

점층적 생성자 패턴도 쓸 수는 있지만, 매개변수 개수가 많아지면 클라이언트 코드를 작성하거나 읽기 어렵다.

클라어인트가 실수로 매개변수의 순서를 바꿔 건네줘도 컴파일러는 알아채지 못하고, 결국 런타임에 엉뚱한 동작을 하게 된다(아이템 51)

## 2. 자바빈즈 패턴

이번에는 선택 매개변수가 많을 때 활용할 수 있는 두 번째 대안인 자바빈즈 패턴(JavaBeans Patern)을 보겠다. 

- 자바 빈즈 패턴 : 매개변수가 없는 생성자로 객체를 만든 후, 세터(setter) 메소드를 호출해 원하는 매개변수의 값을 설정하는 방식

```java
public class NutritionFacts {
    // 필수
    private int servingSize = -1;
    private int servings = -1;
    // 선택적 매개변수
    private int calories = 0;
    private int fat = 0;
    private int sodium = 0;
    private int carbohydrate = 0;

    public NutritionFacts() {}
    public void setServingSize(int val) { servingSize = val; }
    public void setServings(int val) { ... }
    public void setCalories(int val) { ... }
    public void setFat(int val) { ... }
    public void setSodium(int val) { ... }
    public void setCarbohydrate(int val) { ... }
}
```

코드가 길어지긴 했지만 인스턴스를 만들기 쉽고,  더 읽기 쉬운 코드가 되었다.

**자바빈즈 패턴에서는 객체 하나를 만들려면 메서드를 여러 개 호출해야 하고, 객체가 완전히 생성되기 전까지는 일관성(consistency)이 무너진 상태에 놓이기 된다.**

이처럼 일관성이 무너지는 문제 때문에 **자바빈즈 패턴에서는 클래스를 불변(아이템 17)으로 만들 수 없으며** 스레드 안정성을 얻으려면 프로그래머가 추가 작업을 해줘야만 하낟.

이러한 단점을 완화하고자 생성이 끝난 객체를 수동으로 얼리고(freezing), 얼리기 전에는 사용할 수 없도록 하기도 한다. (하지만 이 방법은 실전에서는 거의 쓰이지 않음)

## 3. 빌더 패턴

점층적 생성자 패턴의 안정성과 자바빈즈 패턴의 가독성을 겸비한 빌더 패턴(Builder Pattern)이다.

클라이언트는 필요한 객체를 만드는 대신, 필수 매개변수만으로 생성자(혹은 정적팩터리)를 호출해 빌더 객체를 얻는다.

빌더는 생성할 클래스안에 정적 멤버 클래스로 만들어두는 게 보통이다.

```java
public class NutritionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int fat;
    private int sodium;
    private int carbohydrate;

    public static class Builder {
        // 필수
        private int servingSize;
        private int servings;
        // 선택적 매개변수 - 기본값으로 초기화
        private int calories = 0;
        private int fat = 0;
        private int sodium = 0;
        private int carbohydrate = 0;
         
        public Builder(int servingSize, int servings){
            this.servingSize = servingSize;
            this.servings = servings;
        }
        public Builder calories(int val) { calories = val; return this }
        public Builder fat(int val) { fat = val; return this }
        public Builder sodium(int val) { sodium = val; return this }
        public Builder carbohydrate(int val) { carbohydrate = val; return this }

        private NutritionFacts(Builder builder) {
            servingSize = builder.servingSize;
            servings = builder.servings;
            calories = builder.calories;
            fat = builder.fat;
            sodium = builder.sodium;
            carbohydrate = builder.carbohydrate;
        }
    }
}
```

`Nutrition` 클래스를 불변이며, 모든 매개변수의 기본값들을 한곳에 모아뒀다.

빌더의 세터 메서드들은 빌더 자신을 반환하기 때문에 연쇄적으로 호출할 수 있다.(플루언트 API 혹은 메서드 체이닝(method chaining))

```java
NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
            .calories(100)
            .sodium(35)
            .carbonhydrate(27)
            .build();
```

빌더 패턴은(파이썬과, 스칼라에 있는) 명명된 선택적 매개변수(named optional parameters)를 흉내 낸 것이다.

- 잘못된 매개변수를 일찍 발견할려면
  
  1. 빌더의 생성자와 메서드에서 입력 매개변수를 검사
  
  2. build 메서드가 호출하는 생성자에서 여러 매개변수에 걸친 불변식을 검사
  
  3. 공격에 대비해 이런 불변식을 보장할려면 빌더로부터 매개변수를 복사한 후 해당 객체 필드들을 검사해야함 (아이템 50)
  
  4. `IllegalArgumentException` 메세지를 던지자.

- 불변(immutalble) : 어떤 변경도 허용하지 않는다. (예: `String` 객체)

- 불변식(invariant) : 변경될 수 있으나, 주어진 조건내에서만 허용
  
  - 리스트의 크기는 반드시 0 이상이어야 하니, 사이즈가 음수가 되면 불변식이 깨진것

**빌더 패턴은 계층적으로 설계된 클래스와 함계 쓰기에 좋다.**



빌더 패턴에 장점만 있는 것은 아니다. 객체를 만들려면, 그에 앞서 빌더부터 만들어야한다.

빌더 생성 비용이 크지는 않지만 성능에 민감한 상황에서는 문제가 될 수 있다.
또한 점층적 생성자 패턴보다는 코드가 장황해서 매개변수가 **4개** 이상은 되어야 값어치를 한다.



### 핵심 정리

**생성자나 정적 팩터리가 처리해야 할 매개변수가 많다면 빌더 패턴을 선택하는게 더 낫다.**

매개변수 중 다수가 필수가 아니거나 같은 타입이면 특히 더 그렇다. 빌더는 점층적 생성자 보다는 클라이언트 코드를 읽고 쓰기가 훨씬 간결하고, 자바빈즈보다 훨씬 안전하다.
