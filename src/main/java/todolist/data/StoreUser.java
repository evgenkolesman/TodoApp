package todolist.data;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import todolist.model.User;
import todolist.util.InitPool;
import todolist.util.InitSessionFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StoreUser {

    private StoreUser() {
        InitPool.getInstance().makePool( "/home/evgenios/IdeaProjects/TodoApp/src/main/resources/hibernate.cfg.xml");
    }

    private static class Lazy {
        static final StoreUser INSTANCE = new StoreUser();
    }

    public static StoreUser getInstance() {
        return StoreUser.Lazy.INSTANCE;
    }

    private <T> T wrapper(final Function<Session, T> command) {
        return InitSessionFactory.doInTransactionWithReturn(command);
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
