package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.EstudianteDTO;
import com.example.factory.InternalFactory;
import com.example.model.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class EstudianteController {
    private static EstudianteController instance;
    private final Persistence dao;
    private EstudianteController(){
        dao = InternalFactory.createEstudianteDAO();
    }

    public static EstudianteController getInstance(){
        if (instance == null) instance = new EstudianteController();
        return instance;
    }

    public boolean validarFormatoEmailPublico(String email) {
        return validarFormatoEmail(email);
    }

    public List<EstudianteDTO> getAll() throws Exception {
        try {
            List<DataTransfer> estudiantesDAO = dao.getAll();
            List<EstudianteDTO> estudiantes = new ArrayList<>();

            for (DataTransfer e: estudiantesDAO){
                EstudianteDTO estudiante = (EstudianteDTO) e;
                estudiantes.add(estudiante);
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

    private boolean validarFormatoEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    public boolean alreadyExist(EstudianteDTO estudiante) {
        try {
            return dao.alreadyExist(estudiante);
        } catch (Exception e) {
            return false;
        }
    }
}