package com.example.controller;

import com.example.DAO.DAO;
import com.example.DTO.EstudianteDTO;
import com.example.factory.InternalFactory;
import com.example.model.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class EstudianteController {
    private final DAO dao;
    public EstudianteController(){
        dao = InternalFactory.createEstudianteDAO();
    }

    public boolean validarFormatoEmailPublico(String email) {
        return validarFormatoEmail(email);
    }

    public List<EstudianteDTO> getAll() throws Exception {
        try {
            List<Object> estudiantesDAO = dao.getAll();
            List<EstudianteDTO> estudiantes = new ArrayList<>();

            for (Object e: estudiantesDAO){
                Estudiante estudiante = (Estudiante) e;
                estudiantes.add(
                        new EstudianteDTO(
                                estudiante.getID(),
                                estudiante.getNombres(),
                                estudiante.getApellidos(),
                                estudiante.getEmail(),
                                estudiante.getCodigo(),
                                estudiante.getPrograma().getID(),
                                estudiante.getPrograma().getNombre(),
                                estudiante.isActivo(),
                                estudiante.getPromedio()
                        )
                );
            }
            return estudiantes;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de estudiantes: " + e.getMessage(), e);
        }
    }

    public boolean insert(EstudianteDTO estudiante) throws Exception {
        try {
            dao.insert(estudiante);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar: " + e.getMessage(), e);
        }
    }

    public String generarDetallesEstudiante(Object estudianteObj) {
        if (!(estudianteObj instanceof Estudiante estudiante)) {
            return "Error: Objeto no válido";
        }

        return String.format("""
            Detalles del Estudiante:
            
            ID: %.0f
            Código: %.0f
            Nombres: %s
            Apellidos: %s
            Email: %s
            Programa: %s
            Facultad: %s
            Promedio: %.2f
            Estado: %s
            """,
                estudiante.getID(),
                estudiante.getCodigo(),
                estudiante.getNombres(),
                estudiante.getApellidos(),
                estudiante.getEmail(),
                estudiante.getPrograma() != null ? estudiante.getPrograma().getNombre() : "Sin programa",
                estudiante.getPrograma() != null && estudiante.getPrograma().getFacultad() != null ?
                        estudiante.getPrograma().getFacultad().getNombre() : "Sin facultad",
                estudiante.getPromedio(),
                estudiante.isActivo() ? "Activo" : "Inactivo"
        );
    }

    private boolean validarFormatoEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    public boolean alreadyExist(double id) {
        try {
            return dao.alreadyExist(id);
        } catch (Exception e) {
            return false;
        }
    }
}