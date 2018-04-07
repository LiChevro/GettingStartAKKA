package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * lambda表达式的使用
 */
public class Example {


    public static void main(String[] args) {
//        lambda1();
        lambda2();
    }

    //1.用lambda表达式替换内部类
    public static void lambda1(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Java8之前，内部类里面有很多代码！");
                    }
                }
        ).start();

        new Thread(()-> System.out.println("Java8,lambda表达式()->{}替换了整个匿名类")).start();
    }

    //使用lambda表达式对列表进行迭代
    public static void lambda2(){
        List features = Arrays.asList("Lambdas","Default Method","Stream API","Date And Time API");
        //Java8之前
        System.out.println("================Before JDK8");
        for (Object feature : features){
            System.out.println(feature);
        }
        //Java8之后
        System.out.println("================After JDK8");
        features.forEach(n-> System.out.println(n));
        //Java8方法的引用，方法引用由：：双冒号操作符标示
        System.out.println("================JDK8新特性，方法的引用");
        features.forEach(System.out::println);
    }

    //Java8 函数式接口Predicate
    public static void lambda3(){
        // 甚至可以用and()、or()和xor()逻辑函数来合并Predicate，
        // 例如要找到所有以J开始，长度为四个字母的名字，你可以合并两个Predicate并传入

    }
    public static void filter(List names, Predicate condition){
        names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
            System.out.println(name + " ");
        });
    }

}
