package app.web.pavelk.integration1.activator1.service;

import app.web.pavelk.integration1.activator1.model.Animal;
import org.springframework.stereotype.Service;


@Service("cService")
public class C {

    public Animal process(Animal animal) {
        System.out.println("C3: " + animal.getName());
        return animal;
    }
}
