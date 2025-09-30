package com.example.factory;

import com.example.DAO.*;
import com.example.database.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternalFactory {
    private static final String path_db_config = "/config/db_config";

    public static DataBase createDB() {
        // Lee la Propiedad del Sistema establecida en la clase Main.
        String dbType = System.getProperty("DB_TYPE");

        // Si no se especifica ninguna propiedad, usa H2 por defecto.
        if (dbType == null) {
            System.out.println("INFO: Propiedad del sistema DB_TYPE no encontrada. Usando H2 por defecto.");
            return new H2();
        }

        System.out.println("INFO: Usando la base de datos seleccionada: " + dbType);
        return switch (dbType.toUpperCase()) {
            case "MYSQL" -> new MySQL();
            case "ORACLE" -> new Oracle();
            // Por defecto, incluyendo la opción "H2", usa la base de datos en memoria.
            default -> new H2();
        };
    }

    // El resto de los métodos de la fábrica no necesitan cambios.
    public static DAO createCursoDAO() {
        return new CursoDAO();
    }
    public static DAO createEstudianteDAO() {
        return new EstudianteDAO();
    }
    public static DAO createProfesorDAO() {
        return new ProfesorDAO();
    }
    public static DAO createProgramaDAO() {
        return new ProgramaDAO();
    }
    public static DAO createFacultadDAO() {
        return new FacultadDAO();
    }
    public static DAO createInscripcionDAO() {
        return new InscripcionDAO();
    }

    private static String readDBFromFile() {
        try (InputStream in = InternalFactory.class.getResourceAsStream(path_db_config);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String linea;
            while ((linea = reader.readLine()) != null) {
                if (!linea.startsWith("#")) {
                    return linea;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}