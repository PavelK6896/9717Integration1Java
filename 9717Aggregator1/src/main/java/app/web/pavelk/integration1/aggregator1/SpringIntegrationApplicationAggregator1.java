package app.web.pavelk.integration1.aggregator1;

import app.web.pavelk.integration1.aggregator1.service.SendingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.channel.DirectChannel;


//Агрегатор – конечная точка, которая может объединять несколько сообщений в одно.
//Сплитер – конечная точка, которая наоборот, разбивает одно сообщение на несколько.
//correlationId одинаковые идут в одну группу
//sequenceSize сообщений должно накопиться в группе
@SpringBootApplication
public class SpringIntegrationApplicationAggregator1 {

    public static void main(String[] args) throws InterruptedException {

        ConfigurableApplicationContext ctx = SpringApplication.run(SpringIntegrationApplicationAggregator1.class, args);

        DirectChannel outputChannel = ctx.getBean("outputChannel", DirectChannel.class);
        SendingService service = ctx.getBean(SendingService.class);

        //подписка
        outputChannel.subscribe(message -> System.out.println("OUTPUT CHANNEL: " + message.getPayload()));

        // service.send1();
        // service.send2();
        // service.send3();
        // service.send4();
        // service.send5();
        // service.send6();
        // service.send7();

        ctx.close();
    }


}
