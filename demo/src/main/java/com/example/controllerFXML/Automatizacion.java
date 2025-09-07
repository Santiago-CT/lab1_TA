/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controllerFXML;
import com.example.model.DBSetup;
import com.example.dao.EstudianteDao;
import com.example.dao.ProfesorDAO;
import com.example.dao.FacultadDao;
import com.example.dao.CursoDAO;
import com.example.dao.ProgramaDao;
import com.example.model.Curso;
import com.example.model.CursoProfesor;
import com.example.model.CursosInscritos;
import com.example.model.CursosProfesores;
import com.example.model.Estudiante;
import com.example.model.Facultad;
import com.example.model.Inscripcion;
import com.example.model.InscripcionesPersonas;
import com.example.model.Profesor;
import com.example.model.Programa;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gon
 */
public class Automatizacion {

    private static final Map<Double, Profesor> profesores = new HashMap<>();
    private static final Map<Double, Facultad> facultades = new HashMap<>();
    private static final Map<Double, Programa> programas = new HashMap<>();
    private static final Map<Double, Estudiante> estudiantes = new HashMap<>();
    private static final Map<Integer, Curso> cursos = new HashMap<>();
    private static final CursosInscritos cursosInscritos = new CursosInscritos();
    private static final Map<Integer, CursoProfesor> cursoProfesor = new HashMap<>();
    private static final CursosProfesores cursosProfesores = new CursosProfesores();
    private static final InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();

    private static void buildProfesores() {
        System.out.println("Creando profesores...");
        Profesor p1 = new Profesor(153.0, "Elvis", "Cano", "ejemplo@unillanos.edu.co", "Fijo");
        Profesor p2 = new Profesor(154.0, "Roger", "Calderon", "ejemplo2@unillanos.edu.co", "Fijo");
        Profesor p3 = new Profesor(155.0, "Nestor", "Suat", "ejemplo3@unillanos.edu.co", "Fijo");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        profesores.put(p1.getID(), p1);
        profesores.put(p2.getID(), p2);
        profesores.put(p3.getID(), p3);
    }

    private static void buildFacultad() {
        System.out.println("\nCreando facultad...");
        Facultad fcbi = new Facultad(1.0, "FCBI", profesores.get(153.0));
        System.out.println(fcbi);
        facultades.put(fcbi.getID(), fcbi);

    }

    private static void buildPrograma() {
        System.out.println("\nCreando programa...");
        java.sql.Date fechaCreacion = new java.sql.Date(System.currentTimeMillis());
        Programa sistemas = new Programa(11.0, "Ing Sistemas", 10, fechaCreacion, facultades.get(1.0));
        System.out.println(sistemas);
        programas.put(sistemas.getID(), sistemas);
    }

    private static void buildEstudiantes() {
        System.out.println("\nCreando estudiantes...");

        Estudiante e1 = new Estudiante(124.0, "Juan Esteban", "Aristizabal Sabogal", "jearistizabal@unillanos.edu.co", 160004903.0, programas.get(11.0), true, 4.2);
        Estudiante e2 = new Estudiante(125.0, "Santiago", "Cardona", "scardona@unillanos.edu.co", 160004907.0, programas.get(11.0), true, 3.9);
        Estudiante e3 = new Estudiante(126.0, "Camilo", "Acosta", "caacosta@unillanos.edu.co", 160004840.0, programas.get(11.0), true, 3.6);
        System.out.println(e1);
        System.out.println(e2);
        System.out.println(e3);
        estudiantes.put(e1.getID(), e1);
        estudiantes.put(e2.getID(), e2);
        estudiantes.put(e3.getID(), e3);

    }

    private static void buildCursos() {
        System.out.println("\nCreando cursos...");
        Curso c1 = new Curso(16, "Tecnologias Avanzadas", programas.get(11.0), true);
        Curso c2 = new Curso(18, "Seguridad de la informacion", programas.get(11.0), true);
        Curso c3 = new Curso(20, "Estructuras de Datos", programas.get(11.0), true);
        System.out.println(c1);
        System.out.println(c2);
        System.out.println(c3);
        cursos.put(c1.getID(), c1);
        cursos.put(c2.getID(), c2);
        cursos.put(c3.getID(), c3);
    }

    private static void buildCursosInscritos() {
        System.out.println("\nCreando Inscripciones de cursos...");

        Inscripcion ins1 = new Inscripcion(estudiantes.get(124.0), cursos.get(20), 2025, 1);
        Inscripcion ins2 = new Inscripcion(estudiantes.get(125.0), cursos.get(20), 2025, 1);
        Inscripcion ins3 = new Inscripcion(estudiantes.get(126.0), cursos.get(20), 2025, 1);
        Inscripcion ins4 = new Inscripcion(estudiantes.get(124.0), cursos.get(16), 2024, 2);
        Inscripcion ins5 = new Inscripcion(estudiantes.get(125.0), cursos.get(16), 2024, 2);
        Inscripcion ins6 = new Inscripcion(estudiantes.get(124.0), cursos.get(18), 2024, 1);
        System.out.println(ins1);
        System.out.println(ins2);
        System.out.println(ins3);
        System.out.println(ins4);
        System.out.println(ins5);
        System.out.println(ins6);

        System.out.println("\nInscribiendo cursos...");
        cursosInscritos.inscribirCurso(ins1);
        cursosInscritos.inscribirCurso(ins2);
        cursosInscritos.inscribirCurso(ins3);
        cursosInscritos.inscribirCurso(ins4);
        cursosInscritos.inscribirCurso(ins5);
        cursosInscritos.inscribirCurso(ins6);
        System.out.println(cursosInscritos.imprimirListado());
    }

    private static void buildCursoProfesor() {
        System.out.println("\nCreando cursoProfesor...");
        CursoProfesor cp1 = new CursoProfesor(cursos.get(16), profesores.get(154.0), 2025, 1);
        CursoProfesor cp2 = new CursoProfesor(cursos.get(20), profesores.get(155.0), 2025, 1);
        CursoProfesor cp3 = new CursoProfesor(cursos.get(18), profesores.get(154.0), 2025, 1);
        System.out.println(cp1);
        System.out.println(cp2);
        System.out.println(cp3);
        cursoProfesor.put(1, cp1);
        cursoProfesor.put(2, cp2);
        cursoProfesor.put(3, cp3);
    }

    private static void buildCursosProfesores() {
        System.out.println("\nInscribiendo cursosProfesores...");
        cursosProfesores.inscribir(cursoProfesor.get(1));
        cursosProfesores.inscribir(cursoProfesor.get(2));
        cursosProfesores.inscribir(cursoProfesor.get(3));
        System.out.println(cursosProfesores.imprimirListado());
    }

    private static void buildInscripcionesPersonas() {
        System.out.println("\nInscribiendo en inscripcionesPersonas...");
        inscripcionesPersonas.inscribir(profesores.get(153.0));
        inscripcionesPersonas.inscribir(profesores.get(154.0));
        inscripcionesPersonas.inscribir(profesores.get(155.0));
        inscripcionesPersonas.inscribir(estudiantes.get(124.0));
        inscripcionesPersonas.inscribir(estudiantes.get(125.0));
        inscripcionesPersonas.inscribir(estudiantes.get(126.0));
    }

    public static void run() {
        try {
            buildProfesores();
            buildFacultad();
            buildPrograma();
            buildEstudiantes();
            buildCursos();
            buildCursosInscritos();
            buildCursoProfesor();
            buildCursosProfesores();
            buildInscripcionesPersonas();

            DBSetup.crearTablas();
            for (Profesor profesor : profesores.values()) {
                ProfesorDAO.insert(profesor);
            }
            for (Facultad facultad : facultades.values()) {
                FacultadDao.insert(facultad);
            }
            for (Programa programa : programas.values()) {
                ProgramaDao.insert(programa);
            }
            for (Estudiante estudiante : estudiantes.values()) {
                EstudianteDao.insert(estudiante);
            }
            for (Curso curso : cursos.values()) {
                CursoDAO.insert(curso);
            }
            cursosInscritos.guardarinformacion();
            cursosInscritos.cargarDatos();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
