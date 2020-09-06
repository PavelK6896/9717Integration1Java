package app.web.pavelk.integration1.adapter1;

import app.web.pavelk.integration1.adapter1.service.EmailEmitterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SpringIntegrationApplicationAdapter1 {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringIntegrationApplicationAdapter1.class, args);
        EmailEmitterService emitterService = (EmailEmitterService) context.getBean(EmailEmitterService.class);
        emitterService.sendEmails(1);//отправляем два письма
    }

}
