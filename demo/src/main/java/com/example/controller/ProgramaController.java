package com.example.controller;//package com.example.controller;

import com.example.DAO.DAO;
import com.example.DAO.InscripcionDAO;
import com.example.DTO.ProgramaDTO;
import com.example.DAO.ProgramaDAO;
import com.example.factory.InternalFactory;
import com.example.model.Programa;

import java.util.ArrayList;
import java.util.List;


public class ProgramaController {
    private final DAO dao;
    public ProgramaController(){
        dao = InternalFactory.createProgramaDAO();
    }

    public List<ProgramaDTO> get() throws Exception {
        try {
            List<Object> programasDAO = dao.getAll();
            List<ProgramaDTO> programas = new ArrayList<>();

            for (Object obj: programasDAO){
                Programa p = (Programa) obj;
                programas.add(
                        new ProgramaDTO(
                                p.getID(),
                                p.getNombre(),
                                p.getDuracion(),
                                p.getRegistro(),
                                p.getFacultad().getID(),
                                p.getFacultad().getNombre()
                        )
                );
            }

            return programas;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista: " + e.getMessage(), e);
        }
    }

    public boolean insert(ProgramaDTO programa) throws Exception {
        try {
            dao.insert(programa);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar: " + e.getMessage(), e);
        }
    }

    public boolean existePrograma(double id) {
        try {
            return dao.alreadyExist(id);
        } catch (Exception e) {
            return false;
        }
    }

}