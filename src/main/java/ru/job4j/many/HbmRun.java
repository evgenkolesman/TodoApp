package ru.job4j.many;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.many.cars.Car;
import ru.job4j.many.cars.Models;

import java.util.List;
import java.util.stream.IntStream;

public class HbmRun {
    private static Logger logger = Logger.getLogger(HbmRun.class);

    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Models one = Models.of("Audi");
            Models two = Models.of("LADA");
            Models three = Models.of("Renault");
            Models four = Models.of("Skoda");
            Models five = Models.of("Ferrari");

            List<Models> list = List.of(one, two, three, four, five);
            for (Models a : list) {
                session.save(a);
            }

            Car car = Car.of("CAR");

            IntStream.range(1, list.size() + 1).mapToObj(i -> session.load(Models.class, i)).forEach(car::addModels);
            session.save(car);

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}