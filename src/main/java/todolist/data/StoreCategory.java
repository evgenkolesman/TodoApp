package todolist.data;

import org.hibernate.Session;
import todolist.model.Category;
import todolist.util.InitPool;
import todolist.util.InitSessionFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public class StoreCategory {

    private StoreCategory() {
//        "/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml"
        InitPool.makePool("/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml");
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
//                "SELECT DISTINCT c FROM Item c JOIN FETCH c.categories").list());
                "FROM Category").list());
    }


    public Category findById(Integer id) {
        return (Category) this.wrapper(session -> session.createQuery("FROM Category WHERE id = :id").
                setParameter("id", id).
                uniqueResult());
    }


}
