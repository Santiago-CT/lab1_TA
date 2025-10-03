package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.ProgramaDTO;
import com.example.factory.InternalFactory;
import com.example.model.Programa;

import java.util.ArrayList;
import java.util.List;


public class ProgramaController {
    private static ProgramaController instance;
    private final Persistence dao;
    private ProgramaController(){
        dao = InternalFactory.createProgramaDAO();
    }

    public static ProgramaController getInstance(){
        if (instance == null) instance = new ProgramaController();
        return instance;
    }

    public List<ProgramaDTO> get() throws Exception {
        try {
            List<DataTransfer> programasDAO = dao.getAll();
            List<ProgramaDTO> programas = new ArrayList<>();

            for (DataTransfer obj: programasDAO){
                ProgramaDTO programa = (ProgramaDTO) obj;
                programas.add(programa);
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

    public boolean existePrograma(ProgramaDTO programa) {
        try {
            return dao.alreadyExist(programa);
        } catch (Exception e) {
            return false;
        }
    }
}