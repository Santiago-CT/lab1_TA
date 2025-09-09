package com.example.controller;//package com.example.controller;

import com.example.dao.ProgramaDao;
import com.example.model.Programa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;


public class ProgramaController {

    public ObservableList<Object> obtenerListaProgramas() throws Exception {
        try {
            List<Programa> programa = ProgramaDao.get();
            return FXCollections.observableArrayList(programa.toArray());
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista: " + e.getMessage(), e);
        }
    }

    public boolean insertar(Object programaObj) throws Exception {
        if (!(programaObj instanceof Programa programa)) {
            throw new Exception("El objeto proporcionado no es válido");
        }

        try {
            // Validar datos antes de insertar
            if (!datosValidos(programa)) {
                throw new Exception("Los datos del estudiante no son válidos");
            }


            ProgramaDao.insert(programa);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar el estudiante: " + e.getMessage(), e);
        }
    }

    public double getId(Object programaObj) {
        if (programaObj instanceof Programa programa) {
            return programa.getID();
        }
        return 0.0;
    }

    public String getName(Object programaObj) {
        if (programaObj instanceof Programa programa) {
            return programa.getNombre();
        }
        return "";
    }

    public double getDuracion(Object programaObj) {
        if (programaObj instanceof Programa programa) {
            return programa.getDuracion();
        }
        return 0.0;
    }

    public String getRegistro(Object programaObj) {
        if (programaObj instanceof Programa programa) {
            return programa.getRegistro().toString();
        }
        return "";
    }

    public boolean datosValidos(Object programaObj) {
        if (!(programaObj instanceof Programa programa)) {
            return false;
        }

        // Validar campos obligatorios
        if (programa.getNombre() == null || programa.getNombre().trim().isEmpty()) {
            return false;
        }

        if (programa.getDuracion() == 0.0 || programa.getDuracion() < 0.0) {
            return false;
        }

        return true;
    }

    public boolean existePrograma(double id) {
        try {
            return ProgramaDao.existePrograma(id);
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerMensajesValidacion(Object programaObj) {
        StringBuilder mensajes = new StringBuilder();

        if (!(programaObj instanceof Programa programa)) {
            mensajes.append("• El objeto no es un programa válido\n");
            return mensajes.toString();
        }

        if (programa.getNombre() == null || programa.getNombre().trim().isEmpty()) {
            mensajes.append("• Los nombres son obligatorios\n");
        }

        if (programa.getDuracion() == 0.0 || programa.getDuracion() < 0.0) {
            mensajes.append("• Los apellidos son obligatorios\n");
        }


        return mensajes.toString();
    }
}