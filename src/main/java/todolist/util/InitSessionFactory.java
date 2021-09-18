package todolist.util;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class InitSessionFactory {

    private static Logger logger = Logger.getLogger(InitSessionFactory.class);
    private static SessionFactory sessionFactory;
    private static StandardServiceRegistry registry;

    public InitSessionFactory() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();
            sessionFactory = new MetadataSources(registry)
                    .buildMetadata().buildSessionFactory();
        }
        return sessionFactory;
    }

    public static <T> T doInTransactionWithReturn(Function<Session, T> command) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }

    public static void doInTransaction(Consumer<Session> consumer) {
        Session session = getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            consumer.accept(session);
            tx.commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            session.close();
        }
    }
}