package com.example.factory;

import com.example.persistence.*;
import com.example.database.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InternalFactory { //Singleton
    private static final String path_db_config = "/config/db_config";

    private InternalFactory(){}

    public static DataBase createDB() {
        // Lee la configuración desde el archivo db_config
        String dbType = readDBFromFile();

        if (dbType == null || dbType.trim().isEmpty()) {
            System.out.println("INFO: No se pudo leer la configuración de db_config. Usando H2 por defecto.");
            return H2.getInstance();
        }

        return switch (dbType.trim().toUpperCase()) {
            case "MYSQL" -> MySQL.getInstance();
            case "ORACLE" -> Oracle.getInstance();
            // Por defecto usa la base de datos en memoria.
            default -> H2.getInstance();
        };
    }

    // El resto de los métodos de la fábrica no necesitan cambios.
    public static Persistence createCursoDAO() {
        return CursoDAO.getInstance();
    }
    public static Persistence createEstudianteDAO() {
        return EstudianteDAO.getInstance();
    }
    public static Persistence createProfesorDAO() {
        return ProfesorDAO.getInstance();
    }
    public static Persistence createProgramaDAO() {
        return ProgramaDAO.getInstance();
    }
    public static Persistence createFacultadDAO() {
        return FacultadDAO.getInstance();
    }
    public static Persistence createInscripcionDAO() {
        return InscripcionDAO.getInstance();
    }

    private static String readDBFromFile() {
        try (InputStream in = InternalFactory.class.getResourceAsStream(path_db_config);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String linea;
            while ((linea = reader.readLine()) != null) {

                if (!linea.trim().startsWith("#") && !linea.trim().isEmpty()) {
                    return linea.trim();
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}