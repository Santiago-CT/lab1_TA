package com.example.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBase {
    Connection getConnection() throws SQLException;
    void crearTablas();
    String getDate();
    String getDb_name();
    void setDb_name(String db_name);
}
