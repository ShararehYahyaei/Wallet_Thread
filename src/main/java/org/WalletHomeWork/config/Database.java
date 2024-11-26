package org.WalletHomeWork.config;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    @Getter
    private static final Connection connection;
    private Database(){};
    static {
        var jdbc = "jdbc:postgresql://localhost:5432/postgres";
        try {
            connection = DriverManager.getConnection(jdbc, "postgres", "123456");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // JVM Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}

