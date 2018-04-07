package sample.procedure;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;
import com.typesafe.config.ConfigFactory;

/**
 * 功能详解：在actor运行工程中，可能会有多种状态，各个状态也会存在切换的情况，akka已经帮助我们考虑到这种情况的处理了：Procedure
 *
 * 模拟一个婴儿。婴儿有两种状态，开心和生气，婴儿有个特点就是好玩，永远不会累，所以让其睡觉婴儿就会生气，让他继续玩就会很高兴
 */
public class ProcedureTest extends UntypedActor {

    public enum Msg{
        PLAY,SLEEP
    }

    private  final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    Procedure<Object> happy = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            log.info("I am happy!"+"It's time to "+o);
            if (o == Msg.PLAY){
                getSender().tell("I am already happy!!",getSelf());
                log.info("I am already happy!");
            }else if(o == Msg.SLEEP){
                log.info("I do not like sleep!");
                getContext().become(angray);
            }else{
                unhandled(o);
            }
        }
    };

    Procedure<Object> angray = new Procedure<Object>() {
        @Override
        public void apply(Object o) throws Exception {
            log.info("I am angray!", getSelf());
            if (o == Msg.SLEEP){
                getSender().tell("I am already angray!!",getSelf());
            }else if(o == Msg.PLAY){
                log.info("I like play!");
                getContext().become(happy);
            }else{
                unhandled(o);
            }
        }
    };

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("onReceive msg："+o);
        if (o == Msg.SLEEP){
            getContext().become(angray);
        }else if(o == Msg.PLAY){
            getContext().become(happy);
        }else{
            unhandled(o);
        }
    }

    public static void main(String[] args) {
        ActorSystem system =  ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef procedureTest = system.actorOf(Props.create(ProcedureTest.class),"ProcedureTest");

        procedureTest.tell(Msg.PLAY,ActorRef.noSender());
        procedureTest.tell(Msg.SLEEP,ActorRef.noSender());
/*         procedureTest.tell(Msg.PLAY,ActorRef.noSender());
        procedureTest.tell(Msg.PLAY,ActorRef.noSender());*/

        procedureTest.tell(PoisonPill.getInstance(),ActorRef.noSender());
    }
}

/*
*
*
* */
