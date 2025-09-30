package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQL implements DataBase {
    @Override
    public Connection getConnection() throws SQLException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new SQLException("Faltan variables de entorno para la conexión (DB_URL, DB_USER, DB_PASSWORD)");
        }
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public void crearTablas() {
        System.out.println("Conexión PostgreSQL: la creación de tablas se omite en Docker.");
    }

    @Override
    public String getDate() { return "Fecha de BD no implementada para PostgreSQL"; }
}