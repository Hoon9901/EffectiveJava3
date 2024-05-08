package my.code.chapter2.item6;

public class Item6 {
    public static void main(String[] args) {
        String s1 = new String("test"); // 매번 새로운 String 인스턴스가 생성됨
        String s2 = "test"; // JVM 내 문자열 리터럴 저장소를 통해서 재활용

        System.out.println(s1 == s2); // false
        System.out.println(s2 == "test"); // true
        System.out.println(s1 == "test"); // false
        System.out.println(s1.equals(s2)); // true

        Integer sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i; // auto boxing (불필요한 객체 생성)
        }
        System.out.println(sum);
    }
}
