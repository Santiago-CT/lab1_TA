package com.example.controller;//package com.example.controller;

import com.example.DAO.DAO;
import com.example.DTO.PersonaDTO;
import com.example.DTO.ProfesorDTO;
import com.example.factory.InternalFactory;
import com.example.model.Profesor;

import java.util.ArrayList;
import java.util.List;


public class ProfesorController {
    private final DAO dao;
    public ProfesorController(){
        dao = InternalFactory.createProfesorDAO();
    }

    public List<ProfesorDTO> getAll() throws Exception {
        try {
            List<Object> profesoresDAO = dao.getAll();
            List<ProfesorDTO> profesores = new ArrayList<>();

            for (Object obj : profesoresDAO){
                Profesor profesor = (Profesor) obj;
                profesores.add(
                        new ProfesorDTO(
                                profesor.getID(),
                                profesor.getNombres(),
                                profesor.getApellidos(),
                                profesor.getEmail(),
                                profesor.getTipoContrato()
                        )
                );
            }
            return profesores;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Profesores: " + e.getMessage(), e);
        }
    }

    public List<PersonaDTO> getNombreProfesores() throws Exception {
        try {
            List<Object> profesoresDAO = dao.getAll();
            List<PersonaDTO> profesores = new ArrayList<>();

            for (Object obj : profesoresDAO){
                Profesor p = (Profesor) obj;
                profesores.add(
                        new PersonaDTO(
                                p.getID(),
                                p.getNombres(),
                                p.getApellidos()
                        )
                );
            }
            return profesores;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Profesores: " + e.getMessage(), e);
        }
    }

    public boolean insert(ProfesorDTO profesor) throws Exception {
        try {
            // Validar datos antes de insertar
            if (!datosValidos(profesor)) {
                throw new Exception("Los datos del estudiante no son válidos");
            }

            dao.insert(profesor);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar el estudiante: " + e.getMessage(), e);
        }
    }

    public boolean datosValidos(ProfesorDTO profesor) {

        // Validar campos obligatorios
        if (profesor.getNombres() == null || profesor.getNombres().trim().isEmpty()) {
            return false;
        }

        if (profesor.getApellidos() == null || profesor.getApellidos().trim().isEmpty()) {
            return false;
        }

        if (profesor.getEmail() == null || profesor.getEmail().trim().isEmpty()) {
            return false;
        }

        return formatoEmailValido(profesor.getEmail());
    }

    private boolean formatoEmailValido(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    public boolean validarFormatoEmailPublico(String email) {
        return formatoEmailValido(email);
    }

    public boolean alreadyExist(double id) {
        try {
            return dao.alreadyExist(id);
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajesValidacion(Object profesorObj) {
        StringBuilder mensajes = new StringBuilder();

        if (!(profesorObj instanceof Profesor profesor)) {
            mensajes.append("• El objeto no es un profesor válido\n");
            return mensajes.toString();
        }

        if (profesor.getNombres() == null || profesor.getNombres().trim().isEmpty()) {
            mensajes.append("• Los nombres son obligatorios\n");
        }

        if (profesor.getApellidos() == null || profesor.getApellidos().trim().isEmpty()) {
            mensajes.append("• Los apellidos son obligatorios\n");
        }

        if (profesor.getEmail() == null || profesor.getEmail().trim().isEmpty()) {
            mensajes.append("• El email es obligatorio\n");
        } else if (!formatoEmailValido(profesor.getEmail())) {
            mensajes.append("• El formato del email no es válido\n");
        }

        return mensajes.toString();
    }

}