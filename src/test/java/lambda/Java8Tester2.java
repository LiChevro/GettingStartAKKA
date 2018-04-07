package lambda;


public class Java8Tester2 {

   /*
    lambda表达式智能引用标记了final的外层局部变量，
    也就是说不能在lambda内部修改定义在域外的局部变量，否则会编译错误
    */
    final static String salution = " Hello";

    public static void main(String args[]){
            final String name = "Chevro";   //name被lambda表达式但其只能被final修饰
            GreetingService greetingService = message -> {
                //salution = "nihao";    报编译错误
                System.out.println(salution + message+name);
            };
            greetingService.sayMessage(" World ");
    }

    interface GreetingService{
        void sayMessage(String message);
    }
}
