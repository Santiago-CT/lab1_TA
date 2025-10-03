package com.example.controllerFXML;
import com.example.dataTransfer.*;
import com.example.controller.*;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automatizacion {

  public static final List<Curso> cursosObservables = new ArrayList<>();
    private static final Map<Double, Profesor> profesores = new HashMap<>();
    private static final Map<Double, Facultad> facultades = new HashMap<>();
    private static final Map<Double, Programa> programas = new HashMap<>();
    private static final Map<Double, Estudiante> estudiantes = new HashMap<>();
    private static final Map<Integer, Curso> cursos = new HashMap<>();
    private static final CursosInscritos cursosInscritos = new CursosInscritos();
    private static final Map<Integer, CursoProfesor> cursoProfesor = new HashMap<>();
    private static final CursosProfesores cursosProfesores = new CursosProfesores();
    private static final InscripcionesPersonas inscripcionesPersonas = new InscripcionesPersonas();

    private static final ProfesorController profesorController = ProfesorController.getInstance();
    private static final FacultadController facultadController = FacultadController.getInstance();
    private static final ProgramaController programaController = ProgramaController.getInstance();
    private static final EstudianteController estudianteController = EstudianteController.getInstance();
    private static final CursoController cursoController = CursoController.getInstance();
    private static final InscripcionController inscripcionController = InscripcionController.getInstance();

    private DataBase database;
    public Automatizacion(){
        database = InternalFactory.createDB();
    }

    public void run() {
        try {
            database.crearTablas();
            buildData();
            insertData();
        } catch (Exception e) {
            if (!e.getMessage().contains("restricción única") && !e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error durante la automatización: " + e.getMessage());
            }
        }
    }

    private void buildData() {
        profesores.put(153.0, new Profesor(153.0, "Elvis", "Cano", "elvis.cano@unillanos.edu.co", "TIEMPO COMPLETO"));
        profesores.put(154.0, new Profesor(154.0, "Roger", "Calderon", "roger.calderon@unillanos.edu.co", "TIEMPO COMPLETO"));

        facultades.put(1.0, new Facultad(1.0, "Facultad de Ciencias Básicas e Ingenierías", profesores.get(153.0)));

        programas.put(11.0, new Programa(11.0, "Ingeniería de Sistemas", 10, Date.valueOf(LocalDate.now()), facultades.get(1.0)));

        estudiantes.put(124.0, new Estudiante(124.0, "Juan", "Aristizabal", "jearistizabal@unillanos.edu.co", 160004903.0, programas.get(11.0), true, 4.2));
        estudiantes.put(125.0, new Estudiante(125.0, "Santiago", "Cardona", "scardona@unillanos.edu.co", 160004907.0, programas.get(11.0), true, 3.9));

        Curso c1 = new Curso(16, "Tecnologías Avanzadas", programas.get(11.0), true);
        Curso c2 = new Curso(20, "Estructuras de Datos", programas.get(11.0), true);
        cursos.put(c1.getID(), c1);
        cursos.put(c2.getID(), c2);

        cursosObservables.clear();
        cursosObservables.add(c1);
        cursosObservables.add(c2);

        cursosInscritos.inscribirCurso(new Inscripcion(estudiantes.get(124.0), cursos.get(20), 2025, 1));
        cursosInscritos.inscribirCurso(new Inscripcion(estudiantes.get(125.0), cursos.get(16), 2024, 2));
    }

    private void insertData() throws Exception {
        for (Profesor p : profesores.values()) {
            ProfesorDTO profesorDTO = new ProfesorDTO(
                    p.getID(),
                    p.getNombres(),
                    p.getApellidos(),
                    p.getEmail(),
                    p.getTipoContrato()
            );
            if (!profesorController.alreadyExist(profesorDTO))
                profesorController.insert(profesorDTO);
        }
        for (Facultad f : facultades.values()) {
            FacultadDTO facultadDTO = new FacultadDTO(
                    f.getID(),
                    f.getNombre(),
                    f.getDecano().getID(),
                    f.getDecano().getNombres()
            );
            if (!facultadController.alreadyExist(facultadDTO))
                facultadController.insert(facultadDTO);
        }
        for (Programa p : programas.values()) {
            ProgramaDTO programaDTO = new ProgramaDTO(
                    p.getID(),
                    p.getNombre(),
                    p.getDuracion(),
                    p.getRegistro(),
                    p.getFacultad().getID(),
                    p.getFacultad().getNombre()
            );
            if (!programaController.existePrograma(programaDTO))
                programaController.insert(programaDTO);
        }
        for (Estudiante e : estudiantes.values()) {
            EstudianteDTO estudianteDTO = new EstudianteDTO(
                    e.getID(),
                    e.getNombres(),
                    e.getApellidos(),
                    e.getEmail(),
                    e.getCodigo(),
                    e.getPrograma().getID(),
                    e.getPrograma().getNombre(),
                    e.isActivo(),
                    e.getPromedio()
            );
            if (!estudianteController.alreadyExist(estudianteDTO))
                estudianteController.insert(estudianteDTO);
        }
        for (Curso c : cursos.values()) {
            CursoDTO cursoDTO = new CursoDTO(
                    c.getID(),
                    c.getNombre(),
                    c.getPrograma().getID(),
                    c.getPrograma().getNombre(),
                    c.isActivo()
            );
            if (!cursoController.existeCurso(cursoDTO))
                cursoController.insert(cursoDTO);
        }
        for (Inscripcion i: cursosInscritos.getListado()){
            InscripcionDTO dto = new InscripcionDTO(
                    i.getEstudiante().getID(),
                    i.getEstudiante().getNombres(),
                    i.getCurso().getID(),
                    i.getCurso().getNombre(),
                    i.getAnio(),
                    i.getSemestre()
            );
            if (!inscripcionController.alreadyExist(dto))
                inscripcionController.insert(dto);
        }
    }
}