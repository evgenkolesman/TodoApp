package ru.job4j.lazy.cars;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;

public class MBRun {
    static Logger logger = Logger.getLogger(MBRun.class);

    public static void main(String[] args) {
        List<Brand> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            //join fetch метод
            list = session.createQuery(
                    "select distinct c from Brand c join fetch c.models"
            ).list();
            // известный мне код все делаем в сессии
//            session.beginTransaction();
//            list = session.createQuery("from Brand").list();
//            for (Brand brand : list) {
//                for (ModelCar modelCar : brand.getModels()) {
//                    System.out.println(modelCar);
//                }
//            }
//            session.getTransaction().commit();

            if (session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
                session.getTransaction().commit();
            }
            session.close();
        }  catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (ModelCar modelCar: list.get(1).getModels()) {
            System.out.println(modelCar);
        }
    }
}
