package todolist.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todolist.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StoreUser {
    private static final Logger logger = Logger.getLogger(StoreData.class);
    private final BasicDataSource pool = new BasicDataSource();
    private final StandardServiceRegistry registry =
            new StandardServiceRegistryBuilder().configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();


    private StoreUser() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml")
        )) {
            cfg.load(io);
            //Class.forName(cfg.getProperty("hibernate.connection.driver_class"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        pool.setDriverClassName(cfg.getProperty("hibernate.connection.driver_class"));
        pool.setUrl(cfg.getProperty("hibernate.connection.url"));
        pool.setUsername(cfg.getProperty("hibernate.connection.username"));
        pool.setPassword(cfg.getProperty("hibernate.connection.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static class Lazy {
        static final StoreUser INSTANCE = new StoreUser();
    }

    public static StoreUser getInstance() {
        return StoreUser.Lazy.INSTANCE;
    }

    private <T> T wrapper(final Function<Session, T> command) {
        Session session = sf.openSession();
        session.beginTransaction();
        try {
            T result = command.apply(session);
            session.getTransaction().commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void add(User user) throws SQLException {
        this.wrapper(session -> session.save(user));
    }

    public void delete(Integer id) {
        this.wrapper(session ->
                session.createQuery("DELETE User where id = :id").
                        setParameter("id", id).
                        executeUpdate());
    }

    public List<User> findAll() {
        return this.wrapper(session -> session.createQuery("FROM User ORDER BY id ASC").list());
    }

    public User findById(Integer id) {
        return (User) this.wrapper(session -> session.createQuery("from User where id = :id").
                setParameter("id", id).
                uniqueResult());
    }

    public User findByEmail(String email) {
        return (User) this.wrapper(session -> session.createQuery("from User where email = :email").
                setParameter("email", email).
                uniqueResult());
    }

}
