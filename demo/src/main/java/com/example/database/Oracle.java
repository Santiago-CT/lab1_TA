package com.example.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Oracle implements DataBase{
    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC driver not found.", e);
        }

        String URL = "jdbc:oracle:thin:@//localhost:1521/XEPDB1";
        String USER = "system";
        String PASSWORD = "password";

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    @Override
    public void crearTablas() {
        List<String> ddls = new ArrayList<>();

        ddls.add("""
        CREATE TABLE persona (
            id NUMBER PRIMARY KEY,
            nombres VARCHAR2(100) NOT NULL,
            apellidos VARCHAR2(100) NOT NULL,
            email VARCHAR2(100) NOT NULL
        )
    """);

        ddls.add("""
        CREATE TABLE profesor (
            persona_id NUMBER PRIMARY KEY,
            tipoContrato VARCHAR2(100) NOT NULL,
            CONSTRAINT fk_profesor_persona FOREIGN KEY (persona_id) 
                REFERENCES persona(id) ON DELETE CASCADE
        )
    """);

        ddls.add("""
        CREATE TABLE facultad (
            id NUMBER PRIMARY KEY,
            nombre VARCHAR2(100) NOT NULL,
            decano NUMBER NOT NULL,
            CONSTRAINT fk_facultad_decano FOREIGN KEY (decano) REFERENCES persona(id)
        )
    """);

        ddls.add("""
        CREATE TABLE programa (
            id NUMBER PRIMARY KEY,
            duracion NUMBER NOT NULL,
            registro DATE NOT NULL,
            nombre VARCHAR2(100) NOT NULL,
            facultad_id NUMBER NOT NULL,
            CONSTRAINT fk_programa_facultad FOREIGN KEY (facultad_id) REFERENCES facultad(id)
        )
    """);

        ddls.add("""
        CREATE TABLE estudiante (
            persona_id NUMBER PRIMARY KEY,
            codigo NUMBER NOT NULL,
            programa_id NUMBER NOT NULL,
            activo NUMBER(1) NOT NULL,
            promedio NUMBER NOT NULL,
            CONSTRAINT fk_estudiante_persona FOREIGN KEY (persona_id) REFERENCES persona(id),
            CONSTRAINT fk_estudiante_programa FOREIGN KEY (programa_id) REFERENCES programa(id)
        )
    """);

        ddls.add("""
        CREATE TABLE curso (
            id NUMBER PRIMARY KEY,
            nombre VARCHAR2(100) NOT NULL,
            programa_id NUMBER NOT NULL,
            activo NUMBER(1) NOT NULL,
            CONSTRAINT fk_curso_programa FOREIGN KEY (programa_id) REFERENCES programa(id)
        )
    """);

        ddls.add("""
        CREATE TABLE inscripcion (
            estudiante_id NUMBER NOT NULL,
            curso_id NUMBER NOT NULL,
            anio NUMBER NOT NULL,
            semestre NUMBER NOT NULL,
            CONSTRAINT pk_inscripcion PRIMARY KEY (estudiante_id, curso_id, anio, semestre),
            CONSTRAINT fk_inscripcion_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiante(persona_id),
            CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso_id) REFERENCES curso(id)
        )
    """);

        try (Connection cn = getConnection();
             Statement st = cn.createStatement()) {
            for (String ddl : ddls) {
                try {
                    st.execute(ddl);
                } catch (SQLException e) {
                    if (e.getErrorCode() == 955) {
                        // si la tabla ya existe se ignora
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDate() {

        String sql = "SELECT TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS') AS current_date_time FROM dual";
        try (Connection cn = getConnection();
             PreparedStatement st = cn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getString("current_date_time");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}