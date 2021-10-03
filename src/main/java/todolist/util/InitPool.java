package todolist.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import todolist.data.StoreData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;

public class InitPool {
    private static final BasicDataSource pool = new BasicDataSource();
    private static Logger logger = Logger.getLogger(InitSessionFactory.class);

    private InitPool() {
    }

    private static class Lazy {
        static final InitPool INSTANCE = new InitPool();
    }

    public static InitPool getInstance() {
        return Lazy.INSTANCE;
    }

    public void makePool(String fileName) {
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
        pool.setMaxOpenPreparedStatements(100); }
}
