package learnakka.com.akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import learnakka.com.akkademy.messages.SetRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AkkademyDbTest {

    ActorSystem actorSystem = ActorSystem.create();

    @Test
    public void itShouldPlaceKeyValueFromSetMessageIntoMap(){
        TestActorRef<AkkademyDb> actorRef = TestActorRef.create(actorSystem, Props.create(AkkademyDb.class));       //ActorRef在已有actorSystem的基础上根据Actor的类的引用创建actor
        actorRef.tell(new SetRequest("key","value"), ActorRef.noSender());              //通过tell将信息发送至邮箱，而第二个参数则是定义该消息不需要使用任何响应对象

        AkkademyDb akkademyDb = actorRef.underlyingActor();             //获得Actor实例的引用
        assertEquals(akkademyDb.map.get("key"),"value");        //通过获得的Actor实例的引用去调用get("key")检查map，确认将值存入map中

    }

}
