/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gon
 */
public class Automatizacion {

    private static final Map<Long, Profesor> profesores = new HashMap<>();
    private static final Map<Long, Facultad> facultades = new HashMap<>();
    private static final Map<Long, Programa> programas = new HashMap<>();
    private static final Map<Long, Estudiante> estudiantes = new HashMap<>();
    private static final Map<Long, Curso> cursos = new HashMap<>();
    private static final CursosInscritos cursosInscritos = new CursosInscritos();
    private static final Map<Long, CursoProfesor> cursoProfesor = new HashMap<>();
    private static final CursosProfesores cursosProfesores = new CursosProfesores();
    private static final InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();

    private static void buildProfesores() {
        System.out.println("Creando profesores...");
        Profesor p1 = new Profesor(153, "Elvis", "Cano", "ejemplo@unillanos.edu.co", "Fijo");
        Profesor p2 = new Profesor(154, "Roger", "Calderon", "ejemplo2@unillanos.edu.co", "Fijo");
        Profesor p3 = new Profesor(155, "Nestor", "Suat", "ejemplo3@unillanos.edu.co", "Fijo");
        System.out.println(p1.toString());
        System.out.println(p2.toString());
        System.out.println(p3.toString());
        profesores.put(p1.getID(), p1);
        profesores.put(p2.getID(), p2);
        profesores.put(p3.getID(), p3);
    }

    private static void buildFacultad() {
        System.out.println("\nCreando facultad...");
        Facultad fcbi = new Facultad(1, "FCBI", profesores.get((long)153));
        System.out.println(fcbi.toString());
        facultades.put(fcbi.getID(), fcbi);

    }

    private static void buildPrograma() {
        System.out.println("\nCreando programa...");
        Date fechaCreacion = new Date();
        Programa sistemas = new Programa(11, "Ing Sistemas", 10, fechaCreacion, facultades.get((long)1));
        System.out.println(sistemas.toString());
        programas.put(sistemas.getID(), sistemas);
    }

    private static void buildEstudiantes() {
        System.out.println("\nCreando estudiantes...");

        Estudiante e1 = new Estudiante(124, "Juan Esteban", "Aristizabal Sabogal", "jearistizabal@unillanos.edu.co", 160004903.0, programas.get((long)11), true, 4.2);
        Estudiante e2 = new Estudiante(125, "Santiago", "Cardona", "scardona@unillanos.edu.co", 160004907.0, programas.get((long)11), true, 3.9);
        Estudiante e3 = new Estudiante(126, "Camilo", "Acosta", "caacosta@unillanos.edu.co", 160004840.0, programas.get((long)11), true, 3.6);
        System.out.println(e1.toString());
        System.out.println(e2.toString());
        System.out.println(e3.toString());
        estudiantes.put(e1.getID(), e1);
        estudiantes.put(e2.getID(), e2);
        estudiantes.put(e3.getID(), e3);

    }

    private static void buildCursos() {
        System.out.println("\nCreando cursos...");
        Curso c1 = new Curso(16, "Tecnologias Avanzadas", programas.get((long)11), true);
        Curso c2 = new Curso(18, "Seguridad de la informacion", programas.get((long)11), true);
        Curso c3 = new Curso(20, "Estructuras de Datos", programas.get((long)11), true);
        System.out.println(c1.toString());
        System.out.println(c2.toString());
        System.out.println(c3.toString());
        cursos.put(c1.getID(),c1);
        cursos.put(c2.getID(),c2);
        cursos.put(c3.getID(),c3);
    }

    private static void buildCursosInscritos() {
        System.out.println("\nCreando Inscripciones de cursos...");
        
        Inscripcion ins1 = new Inscripcion(estudiantes.get((long)124), cursos.get((long)20), 2025, 1);
        Inscripcion ins2 = new Inscripcion(estudiantes.get((long)125), cursos.get((long)20), 2025, 1);
        Inscripcion ins3 = new Inscripcion(estudiantes.get((long)126), cursos.get((long)20), 2025, 1);
        Inscripcion ins4 = new Inscripcion(estudiantes.get((long)124), cursos.get((long)16), 2024, 2);
        Inscripcion ins5 = new Inscripcion(estudiantes.get((long)125), cursos.get((long)16), 2024, 2);
        Inscripcion ins6 = new Inscripcion(estudiantes.get((long)124), cursos.get((long)18), 2024, 1);
        System.out.println(ins1.toString());
        System.out.println(ins2.toString());
        System.out.println(ins3.toString());
        System.out.println(ins4.toString());
        System.out.println(ins5.toString());
        System.out.println(ins6.toString());

        System.out.println("\nInscribiendo cursos...");
        cursosInscritos.inscribirCurso(ins1);
        cursosInscritos.inscribirCurso(ins2);
        cursosInscritos.inscribirCurso(ins3);
        cursosInscritos.inscribirCurso(ins4);
        cursosInscritos.inscribirCurso(ins5);
        cursosInscritos.inscribirCurso(ins6);
        System.out.println(cursosInscritos.imprimirListado());
    }
    
    private static void buildCursoProfesor(){
        System.out.println("\nCreando cursoProfesor...");
        CursoProfesor cp1 = new CursoProfesor(cursos.get((long)16), profesores.get((long)154), 2025, 1);
        CursoProfesor cp2 = new CursoProfesor(cursos.get((long)20), profesores.get((long)155), 2025, 1);
        CursoProfesor cp3 = new CursoProfesor(cursos.get((long)18), profesores.get((long)154), 2025, 1);
        System.out.println(cp1.toString());
        System.out.println(cp2.toString());
        System.out.println(cp3.toString());
        cursoProfesor.put((long)1, cp1);
        cursoProfesor.put((long)2, cp2);
        cursoProfesor.put((long)3, cp3);
    }
    
    private static void buildCursosProfesores(){
        System.out.println("\nInscribiendo cursosProfesores...");
        cursosProfesores.inscribir(cursoProfesor.get((long)1));
        cursosProfesores.inscribir(cursoProfesor.get((long)2));
        cursosProfesores.inscribir(cursoProfesor.get((long)3));
        System.out.println(cursosProfesores.imprimirListado());
    }
    
    private static void buildInscripcionesPersonas(){
        System.out.println("\nInscribiendo en inscripcionesPersonas...");
        inscripcionesPersonas.inscribir(profesores.get((long)153));
        inscripcionesPersonas.inscribir(profesores.get((long)154));
        inscripcionesPersonas.inscribir(profesores.get((long)155));
        inscripcionesPersonas.inscribir(estudiantes.get((long)124));
        inscripcionesPersonas.inscribir(estudiantes.get((long)125));
        inscripcionesPersonas.inscribir(estudiantes.get((long)126));
    }

    public static void run() {
        buildProfesores();
        buildFacultad();
        buildPrograma();
        buildEstudiantes();
        buildCursos();
        buildCursosInscritos();
        buildCursoProfesor();
        buildCursosProfesores();
        buildInscripcionesPersonas();
    }
}
