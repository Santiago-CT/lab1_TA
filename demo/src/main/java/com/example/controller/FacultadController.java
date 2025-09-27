package com.example.controller;//package com.example.controller;

import com.example.DAO.DAO;
import com.example.DTO.FacultadDTO;
import com.example.factory.InternalFactory;
import com.example.model.Facultad;

import java.util.ArrayList;
import java.util.List;


public class FacultadController {
    private final DAO dao;
    public FacultadController(){
        dao = InternalFactory.createFacultadDAO();
    }
    public List<FacultadDTO> getAll() throws Exception {
        try {
            List<Object> facultadesDAO = dao.getAll();
            List<FacultadDTO> facultades = new ArrayList<>();
            
            for (Object f: facultadesDAO){
                Facultad facultad = (Facultad) f;
                facultades.add(
                        new FacultadDTO(
                                facultad.getID(),
                                facultad.getNombre(),
                                facultad.getDecano().getID(),
                                facultad.getDecano().getNombres() + " " + facultad.getDecano().getApellidos()
                        )
                );
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

    public boolean alreadyExist(double id) throws Exception {
        try {
            return dao.alreadyExist(id);
        } catch (Exception e) {
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }

}