package todolist.data;

import todolist.model.Item;
import org.hibernate.Session;
import todolist.util.InitPool;
import todolist.util.InitSessionFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public class StoreData {

    private StoreData() {
        InitPool.makePool("/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml");
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

    public void update(Integer id, Boolean done) {
        this.wrapper(session ->
                session.createQuery("UPDATE Item SET done = :done WHERE id = :id").
                        setParameter("done", done).
                        setParameter("id", id).executeUpdate());
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

/**
 * Для проверки можно использовать
 * public static void main(String[] args) {
 * IntStream.rangeClosed(6,20).forEach(s-> new StoreData().delete(s));
 */

