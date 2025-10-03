package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.PersonaDTO;
import com.example.dataTransfer.ProfesorDTO;
import com.example.factory.InternalFactory;
import com.example.model.Profesor;

import java.util.ArrayList;
import java.util.List;


public class ProfesorController {
    private static ProfesorController instance;
    private final Persistence dao;
    private ProfesorController(){
        dao = InternalFactory.createProfesorDAO();
    }

    public static ProfesorController getInstance(){
        if (instance == null) instance = new ProfesorController();
        return instance;
    }

    public List<ProfesorDTO> getAll() throws Exception {
        try {
            List<DataTransfer> profesoresDAO = dao.getAll();
            List<ProfesorDTO> profesores = new ArrayList<>();

            for (DataTransfer obj : profesoresDAO){
                ProfesorDTO profesor = (ProfesorDTO) obj;
                profesores.add(profesor);
            }
            return profesores;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Profesores: " + e.getMessage(), e);
        }
    }

    public List<PersonaDTO> getNombreProfesores() throws Exception {
        try {
            List<DataTransfer> profesoresDAO = dao.getAll();
            List<PersonaDTO> profesores = new ArrayList<>();

            for (DataTransfer obj : profesoresDAO){
                PersonaDTO p = (PersonaDTO) obj;
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
            throw new Exception("Error al obtener la lista de nombres de Profesores: " + e.getMessage(), e);
        }
    }

    public boolean insert(ProfesorDTO profesor) throws Exception {
        try {
            // Validar datos antes de insertar
            if (!datosValidos(profesor)) {
                throw new Exception("Los datos del profesor no son válidos");
            }
            dao.insert(profesor);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar el profesor: " + e.getMessage(), e);
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

    public boolean alreadyExist(ProfesorDTO profesor) {
        try {
            return dao.alreadyExist(profesor);
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

    public String generarDetallesProfesor(Object profesorObj) {
        if (!(profesorObj instanceof Profesor profesor)) {
            return "Error: Objeto no válido";
        }

        return String.format("""
        Detalles del Profesor:
        
        ID: %.0f
        Nombres: %s
        Apellidos: %s
        Nombre completo: %s
        Email: %s
        Tipo de contrato: %s
        Estado: %s
        """,
                profesor.getID(),
                profesor.getNombres(),
                profesor.getApellidos(),
                profesor.getNombres() + " " + profesor.getApellidos(),
                profesor.getEmail(),
                formatearTipoContrato(profesor.getTipoContrato()),
                "Activo"
        );
    }
    private String formatearTipoContrato(String tipoContrato) {
        if (tipoContrato == null) return "No definido";

        return switch (tipoContrato.toUpperCase()) {
            case "TIEMPO_COMPLETO" -> "Tiempo Completo";
            case "MEDIO_TIEMPO" -> "Medio Tiempo";
            case "CATEDRA" -> "Cátedra";
            default -> tipoContrato;
        };
    }
}