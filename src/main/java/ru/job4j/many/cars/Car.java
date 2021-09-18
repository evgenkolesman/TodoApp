package ru.job4j.many.cars;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name="car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Models> models = new ArrayList<>();

    public Car() {
    }

    public static Car of(String name) {
        Car car = new Car();
        car.name = name;
        return car;
    }

    public void addModels(Models model) {
        this.models.add(model);
    }

    public void setModels(List<Models> models) {
        this.models = models;
    }

    public List<Models> getModels() {
        return models;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
