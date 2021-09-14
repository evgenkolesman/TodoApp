package todolist.data;

import org.hibernate.query.Query;
import todolist.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class StoreData {
        private static final Logger logger = Logger.getLogger(StoreData.class);
        private final BasicDataSource pool = new BasicDataSource();
        private final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder().configure().build();
        private final SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();


        public StoreData() {
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

        public Item add(Item item) throws SQLException {
            this.wrapper(session -> session.save(item));
            return item;
        }

        public boolean replace(Integer id) {
            Item item = new StoreData().findById(id);
            this.wrapper(session -> session.merge(item));
            return this.wrapper(session -> session.merge(item)) != null;
        }

        public boolean delete(Integer id) {
            Item item = new StoreData().findById(id);
            return this.wrapper(session -> {
                session.delete(item);
                        return new StoreData().findById(id) == null;
                    }
            );
        }

        public List<Item> findAll() {
            return this.wrapper(session -> session.createQuery("from Item").list());
        }

        public List<Item> findByDescription(final String description) {
            return this.wrapper(session -> {
                    final Query query = session.createQuery("from Item where description = :description");
                    query.setParameter("description", description);
                    return query.list();
            });
        }

        public Item findById(Integer id) {
            return(Item) this.wrapper(session -> {
                final Query query = session.createQuery("from Item where id = :id");
                query.setParameter("id", id);
                return query.list().get(0);
            });

        }

        public void close() throws Exception {
            StandardServiceRegistryBuilder.destroy(registry);
        }

//        public static void main(String[] args) throws Exception {
//            StoreData tracker = new StoreData();
//

//        for (Item i : list1) {
//            tracker.add(i);
//        }

//           IntStream.range(9, 11).forEach(tracker :: delete);
//            tracker.findAll().forEach(System.out::println);
//      System.out.println(tracker.findById(1));
//        System.out.println(tracker.replace(18, new Item("item3 new")));
//        tracker.findByName("item1").forEach(System.out::println);
//        System.out.println(tracker.delete(35));


}
