package ru.job4j.lazy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.List;

public class LazyRun {
    public static void main(String[] args) {
        List<Category> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            list = session.createQuery(
                    "select distinct c from Category c join fetch c.tasks"
            ).list();
            // известный мне код все делаем в сессии
//            session.beginTransaction();
//            list = session.createQuery("from Category").list();
//            for (Category category : list) {
//                for (Task task : category.getTasks()) {
//                    System.out.println(task);
//                }
//            }
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Task task : list.get(0).getTasks()) {
            System.out.println(task);
        }
    }
}
