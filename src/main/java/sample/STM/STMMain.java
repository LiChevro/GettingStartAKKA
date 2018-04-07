package sample.STM;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

/**
 * 功能：STM，software trascation management。与关系数据库中的事务相似，具有ACID属性
 *  需求：在分布式任务中，有可能会有和事务相关的处理
 *  业务：假设公司给员工发工资业务，Company是公司actor，EmployeeActor是员工actor，公司账户减钱，员工账户加钱
 */
public class STMMain {

    public static ActorRef companyActor = null;
    public static ActorRef employeeActor = null;

    public static void main(String[] args) throws Exception{
        ActorSystem system = ActorSystem.create("stm", ConfigFactory.load("akka.conf"));
        //创建companyActor
        companyActor = system.actorOf(Props.create(CompanyActor.class),"CompanyActor");
        //创建啊employeeActor
        employeeActor = system.actorOf(Props.create(EmployeeActor.class), "EmployeeActor");

        Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

        for (int i=0; i<23; i++){
            companyActor.tell(new Coordinated(i,timeout),ActorRef.noSender());

            Thread.sleep(200);

            int companyCount = (int) Await.result(Patterns.ask(companyActor,"getCount",timeout),timeout.duration());
            int employeeCount = (int) Await.result(Patterns.ask(employeeActor, "getCount", timeout), timeout.duration());

            System.out.println("companyCount = " + companyCount + ";employeeCount = " + employeeCount);
            System.out.println("-----------------------");
        }
    }
}
