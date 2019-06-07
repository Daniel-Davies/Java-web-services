package com.uci.todorestservice.db;

import java.sql.*;

public class DatabaseConnector {
    public Connection connection;
    String username = "root";
    String password = "inf124";

    public DatabaseConnector() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/store?useSSL=false&serverTimezone=UTC", username, password);
    }

    public Connection getConnection() {
        return connection;
    }
}