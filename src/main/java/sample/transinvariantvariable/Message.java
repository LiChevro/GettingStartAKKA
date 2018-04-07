package sample.transinvariantvariable;

import java.util.List;

/**
 * actor中传递不可变对象，当然是为了安全
 */
public final class Message {

    private final int age;
    private final List<String> list;

    public Message(int age, List<String> list) {
        this.age = age;
        this.list = list;
    }

    public int getAge() {
        return age;
    }

    public List<String> getList() {
        return list;
    }
}
