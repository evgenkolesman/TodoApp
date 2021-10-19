package todolist.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import todolist.model.Category;
import todolist.util.InitSessionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StoreCategory {
    private final BasicDataSource pool = new BasicDataSource();
    private final Logger logger = Logger.getLogger(StoreCategory.class);
    private final String fileName = "/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml";

    private StoreCategory() {
        Properties cfg = new Properties();
        try (
                BufferedReader io = new BufferedReader(
                        new FileReader(fileName)
                )) {
            cfg.load(io);
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
        static final todolist.data.StoreCategory INSTANCE = new todolist.data.StoreCategory();
    }

    public static todolist.data.StoreCategory getInstance() {
        return Lazy.INSTANCE;
    }

    private <T> T wrapper(final Function<Session, T> command) {
        return InitSessionFactory.doInTransactionWithReturn(command);
    }

    public void add(Category category) throws SQLException {
        this.wrapper(session -> session.save(category));
    }

    public void update(Integer id, String name) {
        this.wrapper(session ->
                session.createQuery("UPDATE Category SET name = :name WHERE id = :id").
                        setParameter("name", name).
                        setParameter("id", id).executeUpdate());
    }

    public void delete(Integer id) {
        this.wrapper(session ->
                session.createQuery("DELETE Category where id = :id").
                        setParameter("id", id).
                        executeUpdate());
    }

    public List<Category> findAll() {
        return this.wrapper(session -> session.createQuery(
                "FROM Category").list());
    }


    public Category findById(Integer id) {
        return (Category) this.wrapper(session -> session.createQuery("FROM Category WHERE id = :id").
                setParameter("id", id).
                uniqueResult());
    }
}
