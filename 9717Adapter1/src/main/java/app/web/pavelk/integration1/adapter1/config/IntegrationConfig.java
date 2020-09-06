package app.web.pavelk.integration1.adapter1.config;

import app.web.pavelk.integration1.adapter1.model.Animal;
import app.web.pavelk.integration1.adapter1.service.AnimalFromToEmailConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.MessageChannel;


import javax.persistence.EntityManagerFactory;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@Configuration
public class IntegrationConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AnimalFromToEmailConverter animalFromToEmailConverter;

    @Value("${mail.email}")
    private String email;
    @Value("${mail.password}")
    private String password;

    @Bean("jpaOutputAdapterChannel")
    public MessageChannel jpaOutputAdapterChannel() {
        return new DirectChannel();
    }

    @Bean("sendMailChannel")//бин с именем
    public MessageChannel sendMailChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow mailListener() {//слушатель сообщений
       /* внимание,  надо закодировать имя и пароль с URLEncoder.encode(),
        чтоб не было лишних @
        поэтому email@mail.ru превращается в email%40mail.ru*/
        return IntegrationFlows
                .from(Mail
                                .imapInboundAdapter("imaps://"
                                        + URLEncoder.encode(this.email, Charset.defaultCharset())
                                        + ":"
                                        + URLEncoder.encode(this.password, Charset.defaultCharset())
                                        + "@imap.mail.ru:993/inbox"
                                ).javaMailProperties(p -> {
                                    p.put("mail.debug", "true");
                                    p.put("mail.imaps.ssl.trust", "*");
                                }),

                        //MessageProducers // не требуеться Poller
                        //MessageSource // требуеться Poller
                        e -> e.poller(Pollers.fixedDelay(3000).maxMessagesPerPoll(1))//периуд запроса писем
                )//то что работает
                .<javax.mail.Message>handle((payload, headers) -> (payload))
                .handle(animalFromToEmailConverter, "animalFromEmail")//вызуваем метод
                //.<Animal>filter(animal -> !animal.getName().startsWith("cat"))//просто фильтор
                .handle(Jpa
                                .outboundAdapter(this.entityManagerFactory)//выходной адаптер в базу данных.
                                .entityClass(Animal.class)
                                .persistMode(PersistMode.PERSIST),//НАСТАИВАТЬ
                        e -> e.transactional(true))//деловой
                .get();


    }

    //вспомогательный поток - сгенерируем данные, отправим Animals в почтовый ящик.
    //но можно вручную отправить пиьсма с текстом вида cat0.9542215123310624
    @Bean//sendMailChannel
    public IntegrationFlow sendMailFlow() {//отправка сообщений
        return IntegrationFlows
                .from("sendMailChannel")//то что работает
                .handle(Mail
                                .outboundAdapter("smtp.mail.ru")
                                .port(25)
                                .credentials(this.email, this.password)
                                .javaMailProperties(p -> {//настройка почьты
                                    p.put("mail.debug", "false");
                                    p.put("mail.smtp.ssl.trust", "*");
                                    p.put("mail.smtp.starttls.enable", "true");
                                }),
                        e -> e.id("sendMailEndpoint"))
                .get();
    }
}
