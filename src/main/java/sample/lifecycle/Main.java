package sample.lifecycle;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;

public class Main {
    public static void main(String[] args) {
        //创建ActorSystem。一般来说，一个系统只需要一个ActorSystem。
        //参数1：系统名称。参数2：配置文件
        ActorSystem system = ActorSystem.create("hello", ConfigFactory.load("akka.config"));
        //创建mywork Actor
        ActorRef myWork = system.actorOf(Props.create(MyWork.class),"MyWork");
        //创建监听actor
        ActorRef watchActor = system.actorOf(Props.create(WatchActor.class,myWork),"WatchActor");

        myWork.tell(MyWork.Msg.WORKING,ActorRef.noSender());
        myWork.tell(MyWork.Msg.DONE,ActorRef.noSender());
        myWork.tell(MyWork.Msg.CLOSE,ActorRef.noSender());
        //中断mywork
        myWork.tell(PoisonPill.getInstance(), ActorRef.noSender());

    }

}
