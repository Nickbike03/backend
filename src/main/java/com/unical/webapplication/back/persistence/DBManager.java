package com.unical.webapplication.back.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBManager {

    private static volatile HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/notes4unical1");
        config.setUsername("postgres");
        config.setPassword("1234");
        
        // Configurazioni ottimali per il pool
        config.setMaximumPoolSize(100);       // Numero massimo di connessioni
        config.setMinimumIdle(5);            // Numero minimo di connessioni inattive
        config.setConnectionTimeout(30000);  // 30 secondi di timeout
        config.setIdleTimeout(600000);       // 10 minuti di inattività
        config.setMaxLifetime(1800000);      // 30 minuti di vita massima
        config.setAutoCommit(true);
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("socketTimeout", "30");
        
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}

