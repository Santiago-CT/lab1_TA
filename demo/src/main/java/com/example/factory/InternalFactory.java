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
        String db = readDBFromFile();
        return switch (db) {
            case "MySQL" -> new MySQL();
            case "Oracle" -> new Oracle();
            default -> new H2();
        };
    }

    public static DAO createCursoDAO(){
        return new CursoDAO();
    }
    public static DAO createEstudianteDAO(){
        return new EstudianteDAO();
    }
    public static DAO createProfesorDAO(){
        return new ProfesorDAO();
    }
    public static DAO createProgramaDAO(){
        return new ProgramaDAO();
    }
    public static DAO createFacultadDAO(){
        return new FacultadDAO();
    }
    public static DAO createInscripcionDAO(){
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
