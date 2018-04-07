package sample.helloworld;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main2 {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("nihao");
        ActorRef a = system.actorOf(Props.create(HelloWorld.class),"liling");
        System.out.println("这是a.path："+a.path());
    }
}
