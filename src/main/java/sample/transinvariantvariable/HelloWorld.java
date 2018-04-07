package sample.transinvariantvariable;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

public class HelloWorld extends UntypedActor {

    ActorRef greeter;
    @Override
    public void preStart(){
        //创建greeter Actor
        greeter = getContext().actorOf(Props.create(Greeter.class),"greeter");
        System.out.println("Greeter Actor的路径："+greeter.path());
        greeter.tell(new Message(2, Arrays.asList("2","nihao")),getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        try{
            System.out.println("HelloWorld收到的数据:"+ JSONObject.toJSONString(msg));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
