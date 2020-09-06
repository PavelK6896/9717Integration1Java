package app.web.pavelk.integration1.adapter1.service;

import app.web.pavelk.integration1.adapter1.model.Animal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

@Service
public class AnimalFromToEmailConverter {

    @Value("${mail.email}")
    private String email;

    public SimpleMailMessage createRandomAnimalEmail() {//генерируем письмо
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setSubject("New animal");

        //генеририруем случайные строки вида cat0.9542215123310624, cat0.2964173089983424
        mailMessage.setText("cat" + Math.random());

        //сами себе высылаем, чтобы не заводить два ящика
        mailMessage.setTo(this.email);
        mailMessage.setFrom(this.email);

        return mailMessage;
    }

    public Animal animalFromEmail(Message message) {
        String animalName = "";
        try {
            animalName = message.getContent().toString();
            System.out.println("!!!!!!!! animalFromEmail animalName -- " + animalName);
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
        }
        return new Animal(animalName);
    }
}
