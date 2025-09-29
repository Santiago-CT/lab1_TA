package com.example.controller;

import com.example.DAO.DAO;
import com.example.DTO.InscripcionDTO;
import com.example.factory.InternalFactory;

import java.util.ArrayList;
import java.util.List;

public class InscripcionController {
    private final DAO dao;
    public InscripcionController(){
        dao = InternalFactory.createInscripcionDAO();
    }
    public List<InscripcionDTO> getAll() throws Exception{
        try {
            List<Object> inscripcionesDAO = dao.getAll();
            List<InscripcionDTO> inscripciones = new ArrayList<>();
            for (Object obj: inscripcionesDAO){
                InscripcionDTO inscripcion = (InscripcionDTO) obj;
                inscripciones.add(
                        new InscripcionDTO(
                            inscripcion.getIdEstudiante(),
                            inscripcion.getNombreEstudiante(),
                            inscripcion.getIdCurso(),
                            inscripcion.getNombreCurso(),
                            inscripcion.getAnio(),
                            inscripcion.getSemestre()
                        )
                );
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
            throw new Exception("Error al insertar Facultad: " + e.getMessage(), e);
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
