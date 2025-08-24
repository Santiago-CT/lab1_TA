/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

/**
 *
 * @author gon
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class ConexionBD {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // protocolo:motor:ubicacion
        // JDBC es la API que permite a Java conectarse a bases de datos relacionales.
        // Indica que el driver y el motor de la base de datos es H2
        // testdb es el nombre del archivo que contendrá la base de datos H2
        // DB_CLOSE_DELAY=-1 mantiene la BD en memoria viva hasta que termine la JVM,
        // incluso si cierras la última conexión.
        String user = "sa"; // significa System Administrator, el superusuario en H2
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public static void crearTablas() {
        ArrayList<String> ddls = new ArrayList<>();
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS persona (
                      id DOUBLE PRIMARY KEY,
                      nombres VARCHAR(100) NOT NULL,
                      apellidos VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL
                  );
            """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS facultad (
                      id DOUBLE PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      decano DOUBLE NOT NULL,
                      FOREIGN KEY (decano) REFERENCES persona(id)
                  );
            """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS programa (
                      id DOUBLE PRIMARY KEY,
                      duracion DOUBLE NOT NULL,
                      registro DATE NOT NULL,
                      nombre VARCHAR(100) NOT NULL,
                      facultad_id DOUBLE NOT NULL,
                      FOREIGN KEY (facultad_id) REFERENCES facultad(id)
                  );
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
                  );
                  """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS curso (
                      id DOUBLE PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      programa_id DOUBLE NOT NULL,
                      activo BOOLEAN NOT NULL,
                      FOREIGN KEY (programa_id) REFERENCES programa(id)
                  );
                    """);
        ddls.add("""
                    CREATE TABLE IF NOT EXISTS inscripcion (
                      estudiante_id DOUBLE NOT NULL,
                      curso_id DOUBLE NOT NULL,
                      anio INT NOT NULL,
                      semestre INT NOT NULL,
                      PRIMARY KEY (estudiante_id, curso_id, anio, semestre),
                      FOREIGN KEY (estudiante_id) REFERENCES estudiante(persona_id),
                      FOREIGN KEY (curso_id) REFERENCES curso(id)
                    );
                 """);

        try (Connection cn = ConexionBD.getConnection(); Statement st = cn.createStatement()) {
            for (String ddl : ddls) {
                st.execute(ddl);
            }
            System.out.println("Tablas creadas con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
