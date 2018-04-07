package sample.akkahttp;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;


public class HttpServerMinimalExampleTest {

    public static void main(String[] args) {
        //使用下面定义的路由开启服务
        ActorSystem system = ActorSystem.create("routes");  //这里是要去定义一个Route吗

        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        //为了访问所有的指令，需要一个定义Routes的实例
        HttpServerMinimalExampleTest app = new HttpServerMinimalExampleTest();

        final Flow<HttpRequest,HttpResponse,NotUsed> routeFlow = app.createRoute().flow(system,materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost",8080),materializer);

        System.out.println("Server online at http://localhost:8080/\\nPress RETURN to stop...");
        try {
            System.in.read();       //让它运行直到用户按下返回
        } catch (IOException e) {
            e.printStackTrace();
        }

        binding.thenCompose(ServerBinding::unbind)        //触发从端口解除绑定
                        .thenAccept(unbound->system.terminate());       //并在完成后关闭
    }

    private Route createRoute(){
        return route(
                path("hello",()->
                        get(()->
                                complete("<h1>say hello to akka-http</h1>"))
                )
        );
    }

}

