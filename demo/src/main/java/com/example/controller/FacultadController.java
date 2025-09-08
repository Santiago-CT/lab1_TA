package com.example.controller;//package com.example.controller;

import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;


public class FacultadController {

    public ObservableList<Object> obtenerListaFacultades() throws Exception {
        try {
            List<Facultad> profesores = FacultadDao.getAll();
            return FXCollections.observableArrayList(profesores.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Profesores: " + e.getMessage(), e);
        }
    }

    public boolean insertar(Object facultadObj) throws Exception {
        if (!(facultadObj instanceof Facultad facultad)) {
            throw new Exception("El objeto proporcionado no es válido");
        }

        try {
            // Validar datos antes de insertar
            if (!datosValidos(facultad)) {
                throw new Exception("Los datos de Facultad no son válidos");
            }

            FacultadDao.insert(facultad);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar Facultad: " + e.getMessage(), e);
        }
    }

    public double getId(Object facultadObj) {
        if (facultadObj instanceof Facultad facultad) {
            return facultad.getID();
        }
        return 0.0;
    }

    /**
     * Obtiene el nombre completo
     */
    public String getName(Object facultadObj) {
        if (facultadObj instanceof Facultad facultad) {
            return facultad.getNombre();
        }
        return "";
    }

    public String getDecano(Object facultadObj) {
        if (facultadObj instanceof Facultad facultad) {
            return facultad.getDecano().getNombres() + facultad.getDecano().getApellidos();
        }
        return "";
    }

    public boolean datosValidos(Object facultadObj) {
        if (!(facultadObj instanceof Facultad facultad)) {
            return false;
        }

        // Validar campos obligatorios
        if (facultad.getNombre() == null || facultad.getNombre().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    public boolean existeFacultad(double id) {
        try {
            return FacultadDao.existeFacultad(id);
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajesValidacion(Object facultadObj) {
        StringBuilder mensajes = new StringBuilder();

        if (!(facultadObj instanceof Facultad facultad)) {
            mensajes.append("• El objeto no es un profesor válido\n");
            return mensajes.toString();
        }

        if (facultad.getNombre() == null || facultad.getNombre().trim().isEmpty()) {
            mensajes.append("• Los nombres son obligatorios\n");
        }

        return mensajes.toString();
    }
}