package app.web.pavelk.integration1.adapter1.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EmailEmitterService {//для отправки
    private final MessageChannel messageChannel;
    private final AnimalFromToEmailConverter animalFromToEmailConverter;

    public EmailEmitterService(@Qualifier("sendMailChannel") MessageChannel sendMailChannel,
                               AnimalFromToEmailConverter converter) {
        this.messageChannel = sendMailChannel;
        this.animalFromToEmailConverter = converter;

    }

    public void sendEmails(int emailCount) {//отправить письмо

        for (int i = 0; i < emailCount; i++) {
            Message message = MessageBuilder
                    .withPayload(animalFromToEmailConverter.createRandomAnimalEmail())//SimpleMailMessage
                    .build();
            messageChannel.send(message);//отправили письмо
        }
    }
}
