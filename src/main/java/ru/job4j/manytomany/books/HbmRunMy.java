package ru.job4j.manytomany.books;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRunMy {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Authors one = Authors.of("First author");
            Authors two = Authors.of("Second author");

            Books first = Books.of("First Book");
            first.getAuthors().add(one);
            first.getAuthors().add(two);

            Books second = Books.of("Second book");
            second.getAuthors().add(two);

            session.persist(first);
            session.persist(second);

            Books third = session.get(Books.class, 2);
            session.remove(third);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}