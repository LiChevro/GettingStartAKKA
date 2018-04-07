package sample.supervision;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        //创建superVisor actor
        ActorRef superVisor = system.actorOf(Props.create(SuperVisor.class),"SuperVisor");
        superVisor.tell(Props.create(RestartActor.class),ActorRef.noSender());   //ActorRef.noSender（）返回一个ActorRef对象，ActorRef对象可以自由地传递各个actor的信息

        //这是akka的路径。restartActor是在SuperVisor中创建的。
        /*工作过程中可能会存在成千上万的actor，可以通过actorSelection方便的选择actor进行消息投递，
        其支持通配符匹配getContext().actorSelection("/user/worker_*").*/
        ActorSelection actorSelection = system.actorSelection("akka://strategy/user/SuperVisor/restartActor");
        for(int i = 0 ; i < 100 ; i ++){
            actorSelection.tell(RestartActor.Msg.RESTART, ActorRef.noSender());
        }
    }
}

/*结果分析：
preStart是restartActor初始化时调用的，他的hashcode是463293128，接着遇到空指针异常，根据自定义策略，会重启改actor。此时会调用preRestart，注意他的hashcode依然是463293128，因为preRestart是在正式启动前在老的actor上调用的。
随后打印出preStart,说明新的actor开始创建了，他的hashcode是1195770342,新的actor实例将替代旧的实例工作，这说明同一个restartActor工作的过程中，未必真的是同一个actor。重启完成之后调用postRestart.

再经历3次（自定义策略配置的）重启以后，达到重启上限，系统将直接关闭该actor。
*/
