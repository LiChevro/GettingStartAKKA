package sample.inbox;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 功能：通过Inbox发送信息
 */
public class InboxTest extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    public enum Msg{
        WORKING,DONE,CLOSE;
    }
    @Override
    public void onReceive(Object o) throws Throwable {
        if(o == Msg.WORKING){
            log.info("I am working.");
        }else if (o == Msg.DONE){
            log.info("I am done");
        }else if(o == Msg.CLOSE){
            log.info("I am closed");
            getSender().tell(Msg.CLOSE,getSelf());  //getSender()方法返回一个ActorRef对象告诉发送者我要关闭了
            getContext().stop(getSelf());       //关闭自己
        }else{
            unhandled(o);
        }
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load("akka.conf"));
        ActorRef inboxTest = system.actorOf(Props.create(InboxTest.class),"InboxTest");

        Inbox inbox = Inbox.create(system);
        inbox.watch(inboxTest);         //监听一个actor,用以接收消息

        //通过inbox发消息
        inbox.send(inboxTest,Msg.WORKING);
        inbox.send(inboxTest,Msg.DONE);
        inbox.send(inboxTest,Msg.CLOSE);

        while(true){
            try {
                Object receive = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                if (receive == Msg.CLOSE){      //收到inbox的消息
                    System.out.println("inboxTextActor is closing");
                }else if(receive instanceof Terminated){        // 中断和线程同一个概念
                    System.out.println("inboxTextActor is closed");
                    system.shutdown();
                    break;
                }else{
                    System.out.println(receive);
                }
            }catch (TimeoutException e){
                e.printStackTrace();
            }
        }
    }
}
