package com.workOUTcoach.utility;

import java.sql.*;

public final class DatabaseConnector {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String DB_NAME = "workOUTcoach";

    private static String username = "root";
    private static String password = null;

    private static Connection connection = null;
    private static Statement statement = null;

    public DatabaseConnector() {
        setupConnection();
    }

    public DatabaseConnector(String username, String password) {
        this.username = username;
        this.password = password;

        setupConnection();
    }

    public static ResultSet executeQuery(String sql) {
        if (statement != null) {
            try {
                return statement.executeQuery(sql);
            } catch (SQLException e) {
                Logger.logError("Failed to execute following query: " + sql);
                e.printStackTrace();
            }
        }

        return null;
    }

    private static void setupConnection() {
        registerDriver();
        connectToDatabase(username, password);

        if (!databaseExists())
            createDatabase();
    }

    private static boolean databaseExists() {
        String sql = "USE " + DB_NAME;

        if (statement != null) {
            try {
                if (statement.executeUpdate(sql) == 0)
                    return true;
            } catch (SQLException e) {
                Logger.logWarning("Database doesn't exist!");
            }
        }

        return false;
    }

    private static void createDatabase() {
        Logger.log("Creating database...");

        try {
            if (statement != null) {
                statement.executeUpdate("CREATE DATABASE " + DB_NAME);
                statement.executeUpdate("USE " + DB_NAME);
                statement.executeUpdate("CREATE TABLE clients(id INT NOT NULL AUTO_INCREMENT, name CHAR(255) NOT NULL, email CHAR(255), PRIMARY KEY(id))");
            } else
                Logger.logError("Failed to create database. Connection not yet established!");
        } catch (SQLException e) {
            Logger.logError("Failed to create database!");
            e.printStackTrace();
        }
    }

    private static void connectToDatabase(String username, String password) {
        Logger.log("Connecting to database...");

        try {
            connection = DriverManager.getConnection(DB_URL, username, password);
            statement = connection.createStatement();
            Logger.log("Connected!");
        } catch (SQLException e) {
            Logger.logError("Failed to connect!");
            e.printStackTrace();
        }
    }

    private static void registerDriver() {
        Logger.log("Registering driver...");

        try {
            Class.forName(JDBC_DRIVER);
        } catch (Exception e) {
            Logger.logError("Failed to register driver!");
            e.printStackTrace();
        }
    }

    public static void close() {
        Logger.log("Closing the database connection...");
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Logger.logError("Failed to close the database connection!");
                e.printStackTrace();
            }
        }
    }
}
