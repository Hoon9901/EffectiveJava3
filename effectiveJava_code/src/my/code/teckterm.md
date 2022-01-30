# 기술 용어
## Java 8 언어 명세
- 타입 type 
  - 인터페이스 interface
  - 클래스 class
  - 배열 array
  - 기본타입 primitive
- 애너테이션 annotation : 인터페이스의 일종
- 열거 타입 enum : 클래스의 일종
- 참조 타입 reference type : 인터페이스, 클래스, 배열
- 객체 object : 클래스의 인스턴스와 배열
- 클래스 멤버 member
  - 필드 field 
  - 메서드 method
  - 멤버 클래스
  - 멤버 인터페이스
- 메서드 시그니처 : 메서드 이름, 입력 매개변수의 타입들로 이뤄짐(반환값의 타입은 포함X)

## 자바 언어 명세와 다른 용어들 (책에서 사용)
- 상속 inheritance 을 서브클래싱 subclassing
- 구현 implement 을 확장 extend
- 패키지 접근 package access 을 패키지-프라이빗 package-private

## 언어 명세가 정의하지 않은 용어들
- 공개 API exported API (줄여서 API)
  - 클래스, 인터페이스, 패키지를 통해 접근할 수 있는
    - 모든 클래스, 인터페이스, 생성자, 멤버, 직렬화된 형태(serialized form)
- 사용자 user : API 를 사용하는 프로그램 작성자(사람)
- 클라이언트 client : API를 사용하는 클래스(코드)
- API 요소 : 클래스, 인터페이스, 생성자, 멤버, 직렬화된 형태를 총칭
