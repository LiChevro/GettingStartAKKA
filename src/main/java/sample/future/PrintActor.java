package sample.future;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 *
 */
public class PrintActor extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("PrintActor中，akka.future.PrintActor.onReceive:"+o);
        if (o instanceof  Integer){
            log.info("print:"+o);
        }else{
            unhandled(o);
        }
    }
}
