package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.FacultadDTO;
import com.example.factory.InternalFactory;
import com.example.model.Facultad;

import java.util.ArrayList;
import java.util.List;


public class FacultadController {
    private static FacultadController instance;
    private final Persistence dao;

    private FacultadController(){
        dao = InternalFactory.createFacultadDAO();
    }

    public static FacultadController getInstance(){
        if (instance == null) instance = new FacultadController();
        return instance;
    }


    public List<FacultadDTO> getAll() throws Exception {
        try {
            List<DataTransfer> facultadesDAO = dao.getAll();
            List<FacultadDTO> facultades = new ArrayList<>();
            
            for (DataTransfer f: facultadesDAO){
                FacultadDTO facultad = (FacultadDTO) f;
                facultades.add(facultad);
            }
            return facultades;

        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de Facultades: " + e.getMessage(), e);
        }
    }

    public boolean insert(FacultadDTO facultad) throws Exception {
        try {
            dao.insert(facultad);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar Facultad: " + e.getMessage(), e);
        }
    }

    public boolean alreadyExist(FacultadDTO facultad) throws Exception {
        try {
            return dao.alreadyExist(facultad);
        } catch (Exception e) {
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }
}