package todolist.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import todolist.model.Item;
import todolist.util.InitSessionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StoreData {

    private final BasicDataSource pool = new BasicDataSource();
    private final Logger logger = Logger.getLogger(StoreCategory.class);
    private final String fileName = "/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml";

    private StoreData() {
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
        static final StoreData INSTANCE = new StoreData();
    }

    public static StoreData getInstance() {
        return Lazy.INSTANCE;
    }

    private <T> T wrapper(final Function<Session, T> command) {
        return InitSessionFactory.doInTransactionWithReturn(command);
    }

    public void add(Item item) throws SQLException {
        this.wrapper(session -> session.save(item));
    }

    public void update(Integer id) {
        this.wrapper(session -> {
            Item item = session.get(Item.class, id);
            item.setDone(!item.getDone());
            return item;
        });
    }

    public void delete(Integer id) {
        this.wrapper(session ->
                session.createQuery("DELETE Item where id = :id").
                        setParameter("id", id).
                        executeUpdate());
    }

    public List<Item> findAll() {
        return this.wrapper(session -> session.createQuery("FROM Item ORDER BY id ASC").list());
    }

    public List<Item> findByDescription(final String description) {
        return this.wrapper(session -> session.createQuery("FROM Item WHERE description = :description ORDER BY id ASC").
                setParameter("description", description).
                list());
    }

    public Item findById(Integer id) {
        return (Item) this.wrapper(session -> session.createQuery("from Item where id = :id").
                setParameter("id", id).
                uniqueResult());
    }
}
