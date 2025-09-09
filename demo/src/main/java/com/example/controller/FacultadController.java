package com.example.controller;//package com.example.controller;

import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class FacultadController {

    public List<Map<String, Object>> obtenerListaFacultades() throws Exception {
        try {
            List<Facultad> facultades = FacultadDao.getAll();

            // Convertir cada Facultad a Map
            List<Map<String, Object>> listaFacultades = facultades.stream()
                    .map(facultad -> {
                        Map<String, Object> fila = new HashMap<>();
                        fila.put("id", facultad.getID());
                        fila.put("nombre", facultad.getNombre());
                        fila.put("decano", facultad.getDecano());
                        return fila;
                    })
                    .collect(Collectors.toList());

            return listaFacultades;

        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Facultades: " + e.getMessage(), e);
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