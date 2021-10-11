package todolist.data;

import todolist.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todolist.util.InitPool;
import todolist.util.InitSessionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.IntStream;

public class StoreData {

    private StoreData() {
        InitPool.getInstance("/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml");
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
