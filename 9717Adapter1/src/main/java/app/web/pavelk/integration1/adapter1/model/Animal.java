package app.web.pavelk.integration1.adapter1.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Animal {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    public Animal(String name) {
        this.setName(name);
    }
}
