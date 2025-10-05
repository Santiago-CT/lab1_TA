package com.example.factory;

import com.example.database.DataBase;
import com.example.database.H2;
import com.example.database.MySQL;
import com.example.database.Oracle;
import com.example.persistence.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InternalFactory { //Singleton
    private static final String path_db_config = "/config/db_config";

    private InternalFactory() {
    }

    public static DataBase createDB() {
        String dbType = readDBFromFile();
        //System.out.println("USANDO DATABASE: " + dbType.trim().split(" ")[0].toUpperCase());

        if (dbType == null || dbType.trim().isEmpty()) {
            System.out.println("INFO: No se pudo leer la configuración de db_config. Usando H2 por defecto.");
            return H2.getInstance();
        }

        return switch (dbType.trim().split(" ")[0].toUpperCase()) {
            case "MYSQL" -> MySQL.getInstance();
            case "ORACLE" -> Oracle.getInstance();
            // Por defecto usa la base de datos en memoria.
            default -> H2.getInstance();
        };
    }

    public static Persistence createCursoDAO() {
        return CursoDAO.getInstance();
    }

    public static Persistence createEstudianteDAO() {
        return EstudianteDAO.getInstance();
    }

    public static Persistence createProfesorDAO() { return ProfesorDAO.getInstance(); }

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

                if (!linea.trim().isEmpty() && !linea.trim().startsWith("#")) {
                    return linea.trim();
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }

}