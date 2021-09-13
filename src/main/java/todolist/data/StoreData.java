package todolist.data;

import todolist.model.Item;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


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
                    new FileReader("/home/evgenios/IdeaProjects/TodoApp/src/main/resources/db.properties")
            )) {
                cfg.load(io);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            try {
                Class.forName(cfg.getProperty("driver"));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            pool.setDriverClassName(cfg.getProperty("driver"));
            pool.setUrl(cfg.getProperty("url"));
            pool.setUsername(cfg.getProperty("username"));
            pool.setPassword(cfg.getProperty("password"));
            pool.setMinIdle(5);
            pool.setMaxIdle(10);
            pool.setMaxOpenPreparedStatements(100);
        }

        public Item add(Item item) throws SQLException {
            Session session = sf.openSession();
            session.beginTransaction();
            try {
                session.save(item);
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.error("error: ", e);
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
            return item;
        }

        public boolean replace(Integer id, Item item) {
            Session session = sf.openSession();
            session.beginTransaction();
            boolean flag = false;
            try {
                if (session.contains(item.getDescription())) {
                    session.merge(item);
                    session.getTransaction().commit();
                    flag = true;
                }
            } catch (Exception e) {
                logger.error("error: ", e);
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
            return flag;
        }

        public boolean delete(Integer id) {
            Session session = sf.openSession();
            session.beginTransaction();
            boolean flag = false;
            try {
                Item item = new Item();
                item = findById(id);

                session.delete(item);
                session.getTransaction().commit();
                flag = true;

            } catch (Exception e) {
                logger.error("error: ", e);
                session.getTransaction().rollback();
            } finally {
                session.close();
            }
            return flag;
        }

        public List<Item> findAll() {
            Session session = sf.openSession();
            List<Item> list = new ArrayList<>();
            try {
                session.beginTransaction();
                Criteria criteria = session.createCriteria(Item.class, "items");
                list = criteria.list();
            } catch (Exception e) {
                logger.error("error: ", e);
            } finally {
                session.close();
            }

            return list;
        }

        public List<Item> findByDescription(String key) {
            Session session = sf.openSession();
            List<Item> list = new ArrayList<>();
            try {
                session.beginTransaction();
                Criteria criteria = session.createCriteria(Item.class, "items");
                criteria.add(Restrictions.eq("items.description", key));
                list = criteria.list();
            } catch (Exception e) {
                logger.error("error: ", e);
            } finally {
                session.close();
            }

            return list;
        }

        public Item findById(Integer id) {
            Session session = sf.openSession();
            Item item;
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Item.class, "items");
            criteria.add(Restrictions.eq("items.id", id));
            item = (Item) criteria.list().get(0);
            session.close();
            return item;
        }

        public void close() throws Exception {
            StandardServiceRegistryBuilder.destroy(registry);
        }

//        public static void main(String[] args) throws Exception {
//            TodoDao tracker = new TodoDao();
//            List<Item> list1 = List.of(
//                    new Item("item1"),
//                    new Item("item2"));
//
////        for (Item i : list1) {
////            tracker.add(i);
////        }
//
//            IntStream.range(3, 11).forEach(tracker :: delete);
//            tracker.findAll().forEach(System.out::println);
////        System.out.println(tracker.findById(1));
////        System.out.println(tracker.replace(18, new Item("item3 new")));
////        tracker.findByName("item1").forEach(System.out::println);
////        System.out.println(tracker.delete(35));
//
//        }
}
