package com.gmail.pashasimonpure.repository.impl;

import com.gmail.pashasimonpure.repository.ConnectionRepository;
import com.gmail.pashasimonpure.util.PropertyUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionRepositoryImpl implements ConnectionRepository {

    private static final String CACHE_PREP_STMTS = "cachePrepStmts";
    private static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    private static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";

    private static final String DATABASE_PASSWORD = "datasource.password";
    private static final String DATABASE_URL = "datasource.url";
    private static final String DATABASE_USERNAME = "datasource.username";

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static HikariDataSource ds;
    private static ConnectionRepository instance;

    private ConnectionRepositoryImpl() {
    }

    public static ConnectionRepository getInstance() {
        if (instance == null) {
            instance = new ConnectionRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (ds == null) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException();
            }

            PropertyUtil propertyUtil = new PropertyUtil();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(propertyUtil.getProperty(DATABASE_URL));
            config.setUsername(propertyUtil.getProperty(DATABASE_USERNAME));
            config.setPassword(propertyUtil.getProperty(DATABASE_PASSWORD));
            config.addDataSourceProperty(CACHE_PREP_STMTS, propertyUtil.getProperty(CACHE_PREP_STMTS));
            config.addDataSourceProperty(PREP_STMT_CACHE_SIZE, propertyUtil.getProperty(PREP_STMT_CACHE_SIZE));
            config.addDataSourceProperty(PREP_STMT_CACHE_SQL_LIMIT, propertyUtil.getProperty(PREP_STMT_CACHE_SQL_LIMIT));

            ds = new HikariDataSource(config);
        }
        return ds.getConnection();
    }

}