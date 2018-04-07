package sample.Router;

import akka.actor.*;
import akka.routing.*;
import com.typesafe.config.ConfigFactory;
import sample.inbox.InboxTest;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 需求：一组actor提供相同的服务，我们在调用任务的时候只需要选择其中一个actor进行处理即可
 *       AKKA提供了Router来进行消息的调度
 *
 * 备注：使用到了inbox的InboxTest来进行消息传递
 */
public class RouterTest extends UntypedActor {

    public Router router;

    {
        ArrayList<Routee> routees = new ArrayList<>();
        for (int i=0; i<5; i++){
            //借用上面的inboxActor,创建一个acoter
            ActorRef worker = getContext().actorOf(Props.create(InboxTest.class),"worker_"+i);  //ActorRef是一个actor调度对象，用actorRef可以在actor之间传递消息
            getContext().watch(worker); //监听
            routees.add(new ActorRefRoutee(worker));
        }
        /*
        * RoundRobinRoutingLogic:轮询
        * BroadcastRoutingLogic:广播
        * RandomRountingLogic:随机
        * SmallestMailboxRoutingLogic:空闲
        * */
        router = new Router(new RoundRobinRoutingLogic(),routees);
       //router = new Router(new SmallestMailboxRoutingLogic());   //这几个转发策略有什么 区别？？
        /*
        * 创建Router有两种方法，一种是通过向Router中逐个添加routee，另外一种就是用RoundRobinGroup或者RoudRobinPool来创建Router的ActorRef。
        * */
    }
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof  InboxTest.Msg){
            System.out.println("即将进行路由转发.....");
            router.route(o,getSender());    //进行路由转发
        }else if (o instanceof Terminated){
            //发生中断，将该actor删除。当然这里可以参考之前的actor重启策略，进行优化，为了简单，这里仅进行删除处理
            router = router.removeRoutee(((Terminated)o).actor());      //删除
            System.out.println(((Terminated)o).actor().path()+"该actor已经删除。router.size="+router.routees().size());
            if (router.routees().size() == 0){  //没有可用actor了
                System.out.println("没有可用actor了，系统关闭。");
                flag.compareAndSet(true,false);
                getContext().system().shutdown();
            }
        }else{
            unhandled(o);
        }
    }

    /**
     * AtomicBoolean 能够保证在高并发的情况下保证只有一个线程能够访问这个属性值
     * 可以使用的功能需求：“高效并发处理‘只初始化一次’的功能要求”
     *  分析：AtomicBoolean实际上是使用Volatile属性来完成功能的
     */
    public static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        //创建一个routerTest的actor
        ActorRef routerTest = system.actorOf(Props.create(RouterTest.class),"RouterTest");

        int i=1;
        while(flag.get()){
            routerTest.tell(InboxTest.Msg.WORKING,ActorRef.noSender());

            if(i%10 == 0){
                routerTest.tell(InboxTest.Msg.CLOSE,ActorRef.noSender());
            }
            try{
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
            }
            i++;
        }
    }
}
