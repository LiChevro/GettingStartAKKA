package sample.lifecycle;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyWork extends UntypedActor {

    LoggingAdapter logger = Logging.getLogger(getContext().system(),this);

    public static enum Msg{
        WORKING,DONE,CLOSE;
    }

    @Override
    public void preStart(){
        logger.info("myWork starting.");
    }

    @Override
    public void postStop(){
        logger.info("myWork stopping.");
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        try {
            if(msg == Msg.WORKING){
                logger.info("I am working.");
            }else if(msg == Msg.DONE){
                logger.info("I am stopping work.");
            }else if(msg == Msg.CLOSE){
                logger.info("stop close.");
                getSender().tell(Msg.CLOSE,getSelf());
                getContext().stop(getSelf());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
