package com.example.controller;//package com.example.controller;

import com.example.dao.ProfesorDAO;
import com.example.model.Profesor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;


public class ProfesorController {
    public Object crearProfesor(String nombres, String apellidos, String email, String tipoContrato) {
        // Generar un ID temporal (dependiendo de cómo manejes los IDs)
        double id = System.currentTimeMillis(); // ID temporal basado en timestamp

        return new Profesor(id, nombres, apellidos, email, tipoContrato);
    }
    public boolean existeEmailEnSistema(String email) {
        try {
            // Asumiendo que ConexionBD tiene un método similar para profesores
            // Si no existe, necesitarías crearlo o usar ProfesorDAO
            return ProfesorDAO.existeEmail(email, -1);
        } catch (Exception e) {
            System.err.println("Error al verificar email en sistema: " + e.getMessage());
            return false;
        }
    }

    public ObservableList<Object> obtenerListaProfesores() throws Exception {
        try {
            List<Profesor> profesores = ProfesorDAO.get();
            return FXCollections.observableArrayList(profesores.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Profesores: " + e.getMessage(), e);
        }
    }
    public boolean insertarProfesor(String nombres, String apellidos, String email, String tipoContrato) throws Exception {

        // Crear el profesor usando el método crearProfesor
        Object profesorObj = crearProfesor(nombres, apellidos, email, tipoContrato);

        if (profesorObj == null) {
            throw new Exception("Error al crear el profesor");
        }

        // Usar el método insertar existente
        return insertar(profesorObj);
    }
    public boolean insertar(Object profesorObj) throws Exception {
        if (!(profesorObj instanceof Profesor profesor)) {
            throw new Exception("El objeto proporcionado no es válido");
        }

        try {
            // Validar datos antes de insertar
            if (!datosValidos(profesor)) {
                throw new Exception("Los datos del estudiante no son válidos");
            }


            // Verificar que el email no esté en uso
            if (ProfesorDAO.existeEmail(profesor.getEmail(), -1)) {
                throw new Exception("El email ya está en uso por otra persona");
            }

            ProfesorDAO.insert(profesor);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar el estudiante: " + e.getMessage(), e);
        }
    }


    public double getId(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getID();
        }
        return 0.0;
    }

    /**
     * Obtiene el nombre completo
     */
    public String getFullName(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getNombres() + " " + profesor.getApellidos();
        }
        return "";
    }
    public String getNombres(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getNombres();
        }
        return "";
    }
    public String getTipoContrato(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getTipoContrato();
        }
        return "";
    }


    /**
     * Obtiene los apellidos de un profesor
     */
    public String getApellidos(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getApellidos();
        }
        return "";
    }

    /**
     * Obtiene el email de un estudiante
     */
    public String getEmail(Object profesorObj) {
        if (profesorObj instanceof Profesor profesor) {
            return profesor.getEmail();
        }
        return "";
    }

    public boolean datosValidos(Object profesorObj) {
        if (!(profesorObj instanceof Profesor profesor)) {
            return false;
        }

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

        if (!formatoEmailValido(profesor.getEmail())) {
            return false;
        }

        return true;
    }

    private boolean formatoEmailValido(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    public boolean validarFormatoEmailPublico(String email) {
        return formatoEmailValido(email);
    }

    public boolean existeProfesor(double id) {
        try {
            return ProfesorDAO.existeProfesor(id);
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