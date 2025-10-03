package com.example.services;

import com.example.persistence.Persistence;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;

public class DB_Services {
    private final static DataBase database = InternalFactory.createDB();
    private final static Persistence profesorDAO = InternalFactory.createProfesorDAO();
    private final static Persistence estudianteDAO = InternalFactory.createEstudianteDAO();
    private final static Persistence facultadDAO = InternalFactory.createFacultadDAO();
    private final static Persistence programaDAO = InternalFactory.createProgramaDAO();
    private final static Persistence cursoDAO = InternalFactory.createCursoDAO();
    private final static Persistence inscripcionDAO = InternalFactory.createInscripcionDAO();

    public static String getDate(){
        return database.getDate();
    }

    public static int countProfesores() throws Exception {
        return profesorDAO.count();
    }
    public static int countEstudiantes() throws Exception {
        return estudianteDAO.count();
    }
    public static int countFacultades() throws Exception {
        return facultadDAO.count();
    }
    public static int countProgramas() throws Exception {
        return programaDAO.count();
    }
    public static int countCursos() throws Exception {
        return cursoDAO.count();
    }
    public static int countInscripciones() throws Exception {
        return inscripcionDAO.count();
    }
}
