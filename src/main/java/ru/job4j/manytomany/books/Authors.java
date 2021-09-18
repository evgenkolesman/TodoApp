package ru.job4j.manytomany.books;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "authors")
public class Authors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public static Authors of(String name) {
        Authors book = new Authors();
        book.name = name;
        return book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        Authors name = (Authors) o;
        return id == name.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
