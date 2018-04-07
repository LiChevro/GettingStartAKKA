package sample.supervision;

import akka.actor.UntypedActor;
import scala.Option;

public class RestartActor extends UntypedActor {

    public enum Msg{
        DONE,RESTART;
    }

    @Override
    public void preStart() throws Exception{
        System.out.println("preStart hashCode="+this.hashCode());
    }
    @Override
    public void postStop() throws Exception{
        System.out.println("preStop hashCode="+this.hashCode());
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) throws Exception {
        System.out.println("preRestart    hashCode=" + this.hashCode());
    }
    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        System.out.println("postRestart    hashCode=" + this.hashCode());
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o==Msg.DONE){
            getContext().stop(getSelf());
        }else if(o==Msg.RESTART){
            System.out.println(((Object)null).toString());
        }else{
            unhandled(o);
        }
    }
}
