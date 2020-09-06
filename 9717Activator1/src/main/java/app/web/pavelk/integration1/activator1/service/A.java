package app.web.pavelk.integration1.activator1.service;

import app.web.pavelk.integration1.activator1.model.Animal;
import org.springframework.stereotype.Service;

@Service("aService")
public class A {

    public Animal process(Animal animal) {
        System.out.println("A1: " + animal.getName());
        return animal;
    }
}
