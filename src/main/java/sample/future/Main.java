package sample.future;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Future:AKKA中的Future和Java中的Future挺像的，
 * 可以将一个Actor的返回结果重新定向到另一个Actor中处理，主actor或者进程需等待actor的返回结果
 */
public class Main {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        //创建printActor
        ActorRef printActor = system.actorOf(Props.create(PrintActor.class),"PrintActor");
        //创建workActor
        ActorRef workActor = system.actorOf(Props.create(WorkerActor.class),"WorkerAcktor");

        try {
            //等future返回
            Future<Object> future = Patterns.ask(workActor,5,1000);
            int result = (int) Await.result(future, Duration.create(3, TimeUnit.SECONDS));
            System.out.println("result:"+result);

            //不等待返回值，直接重定向到其他的actor，有返回值来的时候会重定向到printActor
            Future<Object> future1 = Patterns.ask(workActor,8,1000);
            Patterns.pipe(future1,system.dispatcher()).to(printActor);
            workActor.tell(PoisonPill.getInstance(),ActorRef.noSender());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
