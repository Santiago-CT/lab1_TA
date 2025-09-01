package com.example.model;


import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
public class DBSetup {
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
                      CREATE TABLE IF NOT EXISTS profesor (
                          persona_id DOUBLE PRIMARY KEY,
                          tipoContrato VARCHAR(100) NOT NULL,
                          FOREIGN KEY (persona_id) REFERENCES persona(id) ON DELETE CASCADE
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
                    id INT PRIMARY KEY,
                    nombre VARCHAR(100) NOT NULL,
                    programa_id DOUBLE NOT NULL,
                    activo BOOLEAN NOT NULL,
                    FOREIGN KEY (programa_id) REFERENCES programa(id)
                );
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
