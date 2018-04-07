package lambda;

/**
 * Lambda表达式主要用来定义行内执行的方法类型接口，
 * 例如，一个简单方法接口。在上面的例子中，我们使用各种类型的Lambda表达式来定义MathOperation接口的方法。然后我们定义了sayMessage的执行
 *Lambda表达式免去了使用匿名方法的麻烦，并且给予Java简单但是强大的函数化编程的能力
 */
public class Java8Tester {
    public static void main(String[] args) {
        Java8Tester tester = new Java8Tester();

        //类型声明
        MathOperation addition = (int a,int b) -> a + b;

        //不用类型声明
        MathOperation subtraction = (a,b) -> a - b;

        //大括号中返回语句
        MathOperation multiplication = (int a,int b) -> {return a*b;};

        //没有大括号及返回语句
        MathOperation division = (int a,int b) -> a/b;

        System.out.println("10+5="+tester.operate(10,5,addition));
        System.out.println("10-5="+tester.operate(10,5,subtraction));
        System.out.println("10x5="+tester.operate(10,5,multiplication));
        System.out.println("10/5="+tester.operate(10,5,division));

        //不用括号
        GreetingService greetingService1 = message -> System.out.println("Hello "+message);
        //使用括号
        GreetingService greetingService2 = (message) -> System.out.println("Hello "+message);

        greetingService1.sayMessage("baidu");
        greetingService2.sayMessage("google");

    }

    interface MathOperation {
        int operation(int a, int b);
    }

    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a,int b,MathOperation mathOperation) {
        return  mathOperation.operation(a,b);
    }
}
