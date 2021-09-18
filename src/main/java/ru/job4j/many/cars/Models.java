package ru.job4j.many.cars;


import javax.persistence.*;
import java.util.Objects;


@Table(name = "carmodels")
@Entity
public class Models {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public static Models of(String name) {
        Models models = new Models();
        models.name = name;
        return models;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Models models = (Models) o;
        return Objects.equals(id, models.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}