package app.web.pavelk.integration1.activator1.service;

import app.web.pavelk.integration1.activator1.model.Animal;
import org.springframework.stereotype.Service;


@Service("bService")
public class B {

    public Animal process(Animal animal) {
        System.out.println("B2: " + animal.getName());
        return animal;
    }
}
