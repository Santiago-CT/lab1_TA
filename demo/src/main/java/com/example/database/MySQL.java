package com.example.database;

import java.sql.*;
import java.util.ArrayList;

public class MySQL implements DataBase{
    private static MySQL instance;
    private static Connection driverManager;
    private MySQL(){}

    public static MySQL getInstance(){
        //System.out.println(instance);
        if (instance != null) instance = new MySQL();
        //System.out.println(instance);
        return instance;
    }
    @Override
    public Connection getConnection() throws SQLException {
        String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String USER = "app_user";
        String PASSWORD = "App1234$";
        if (driverManager == null) driverManager = DriverManager.getConnection(URL, USER, PASSWORD);
        return driverManager;
    }

    @Override
    public void crearTablas() {
        ArrayList<String> ddls = new ArrayList<>();
        ddls.add("""
                CREATE TABLE IF NOT EXISTS persona (
                    id DOUBLE PRIMARY KEY,
                    nombres VARCHAR(100) NOT NULL,
                    apellidos VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NOT NULL
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS profesor (
                    persona_id DOUBLE PRIMARY KEY,
                    tipoContrato VARCHAR(100) NOT NULL,
                    FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS facultad (
                    id DOUBLE PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    decano DOUBLE NOT NULL,
                    FOREIGN KEY (decano) REFERENCES persona(id)
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS programa (
                    id DOUBLE PRIMARY KEY,
                    duracion DOUBLE NOT NULL,
                    registro DATE NOT NULL,
                    nombre VARCHAR(100) NOT NULL,
                    facultad_id DOUBLE NOT NULL,
                    FOREIGN KEY (facultad_id) REFERENCES facultad(id)
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS estudiante (
                    persona_id DOUBLE PRIMARY KEY,
                    codigo DOUBLE NOT NULL,
                    programa_id DOUBLE NOT NULL,
                    activo BOOLEAN NOT NULL,
                    promedio DOUBLE NOT NULL,
                    FOREIGN KEY (persona_id) REFERENCES persona(id),
                    FOREIGN KEY (programa_id) REFERENCES programa(id)
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS curso (
                    id INT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    programa_id DOUBLE NOT NULL,
                    activo BOOLEAN NOT NULL,
                    FOREIGN KEY (programa_id) REFERENCES programa(id)
                ) ENGINE=InnoDB;
                """);
        ddls.add("""
                CREATE TABLE IF NOT EXISTS inscripcion (
                    estudiante_id DOUBLE NOT NULL,
                    curso_id INT NOT NULL,
                    anio INT NOT NULL,
                    semestre INT NOT NULL,
                    PRIMARY KEY (estudiante_id, curso_id, anio, semestre),
                    FOREIGN KEY (estudiante_id) REFERENCES estudiante(persona_id),
                    FOREIGN KEY (curso_id) REFERENCES curso(id)
                ) ENGINE=InnoDB;
                """);

        try (Connection cn = getConnection(); Statement st = cn.createStatement()) {
            for (String ddl : ddls) {
                st.execute(ddl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDate() {
        String sql = "SELECT NOW() AS date";
        try (Connection cn = getConnection();
             PreparedStatement st = cn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getString("date");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
