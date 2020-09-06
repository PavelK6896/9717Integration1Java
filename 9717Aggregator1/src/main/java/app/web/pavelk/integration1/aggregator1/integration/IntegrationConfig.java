package app.web.pavelk.integration1.aggregator1.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.aggregator.TimeoutCountSequenceSizeReleaseStrategy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public MessageChannel outputChannel() {
        return new DirectChannel();
    }

    @Bean//канал
    public MessageChannel input1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input2() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input3() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input4() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input5() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input6() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel input7() {
        return new DirectChannel();
    }


    @Bean//поток работает с каналом
    public IntegrationFlow flow1() {
        return IntegrationFlows.from("input1")
                .log()
                .split()
                .log()
                //.filter(l->l.equals("B"))
                .aggregate()//тот самый агрегатор с параметрами
                .log()
                .channel("outputChannel").get();
    }

    @Bean
    public IntegrationFlow flow2() { //без сплитера нет заголовков
        return IntegrationFlows.from("input2")
                .log()
                .aggregate()
                .log()
                .channel("outputChannel").get();
    }

    @Bean
    public IntegrationFlow flow3() {//Зададим CorrelationStrategy и ReleaseStrategy самостоятельно.
        return IntegrationFlows.from("input3")
                .log()
                .aggregate(a -> a
                        .correlationStrategy(new MyCorrelationStrategy()) //getCorrelationKey
                        .releaseStrategy(group -> group.size() == 2))
                .log()
                .channel("outputChannel").get();
    }

    @Bean
    public IntegrationFlow flow4() {//expireGroupsUponCompletion включаем
        return IntegrationFlows.from("input4")
                .log()
                .aggregate(a -> a
                        .correlationStrategy(new MyCorrelationStrategy())
                        .releaseStrategy(group -> group.size() == 2)
                        .expireGroupsUponCompletion(true))
                .log()
                .channel("outputChannel").get();
    }

    //такой же, как предыдущий, но без correlationStrategy
    @Bean
    public IntegrationFlow flow5() {
        return IntegrationFlows.from("input5")
                .log()
                .aggregate(a -> a.releaseStrategy(group -> group.size() == 2)
                        .expireGroupsUponCompletion(true))
                .log()
                .channel("outputChannel").get();
    }

    @Bean
    public IntegrationFlow flow6() {//Группировка по времени sendPartialResultOnExpiry() позволяет именно выпустить
        return IntegrationFlows.from("input6")
                .log()
                .aggregate(a -> a.releaseStrategy(group -> group.size() >= 100500)
                        // .expireGroupsUponCompletion(true)
                        .groupTimeout(500)
                        .sendPartialResultOnExpiry(true))
                .log()
                .channel("outputChannel").get();
    }

    //время тут отсчитывается от первого элемента группы
    //Группа выпускается, если хотя бы одно из условий настало – количество или время.
    @Bean
    public IntegrationFlow flow7() {
        return IntegrationFlows.from("input7")
                .log()
                .aggregate(a -> a.releaseStrategy
                        (new TimeoutCountSequenceSizeReleaseStrategy(100500, 500))
                        .expireGroupsUponCompletion(true))
                .log()
                .channel("outputChannel").get();
    }

    static class MyCorrelationStrategy implements CorrelationStrategy {

        @Override
        public Object getCorrelationKey(Message<?> message) {
            String letter = (String) message.getPayload();
            if (letter.startsWith("a") || letter.startsWith("b"))
                return 1;
            else return 2;
        }
    }

}
