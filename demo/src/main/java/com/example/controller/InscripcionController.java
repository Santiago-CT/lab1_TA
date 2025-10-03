package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.InscripcionDTO;
import com.example.factory.InternalFactory;

import java.util.ArrayList;
import java.util.List;

public class InscripcionController {
    private static InscripcionController instance;
    private final Persistence dao;
    private InscripcionController(){
        dao = InternalFactory.createInscripcionDAO();
    }

    public static InscripcionController getInstance(){
        if (instance == null) instance = new InscripcionController();
        return instance;
    }

    public List<InscripcionDTO> getAll() throws Exception{
        try {
            List<DataTransfer> inscripcionesDAO = dao.getAll();
            List<InscripcionDTO> inscripciones = new ArrayList<>();
            for (DataTransfer obj: inscripcionesDAO){
                InscripcionDTO inscripcion = (InscripcionDTO) obj;
                inscripciones.add(inscripcion);
            }
            return inscripciones;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Inscripciones: " + e.getMessage(), e);
        }
    }

    public boolean insert(InscripcionDTO inscripcion) throws Exception {
        try {
            dao.insert(inscripcion);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar Inscripcion: " + e.getMessage(), e);
        }
    }

    public boolean alreadyExist(InscripcionDTO inscripcion) throws Exception {
        try {
            return dao.alreadyExist(inscripcion);
        } catch (Exception e) {
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }

}
