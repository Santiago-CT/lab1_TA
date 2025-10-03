package com.example.controller;

import com.example.dataTransfer.DataTransfer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.CursoDTO;
import com.example.factory.InternalFactory;

import java.util.ArrayList;
import java.util.List;

public class CursoController {
    private static CursoController instance;
    private final Persistence dao;
    private CursoController(){
        dao = InternalFactory.createCursoDAO();
    }

    public static CursoController getInstance(){
        if (instance == null) instance = new CursoController();
        return instance;
    }

    public List<CursoDTO> getAll() throws Exception {
        try{
            List<DataTransfer> cursosDAO = dao.getAll();
            List<CursoDTO> cursos = new ArrayList<>();
            for (DataTransfer c : cursosDAO){
                CursoDTO curso = (CursoDTO) c;
                cursos.add(curso);
            }
            return  cursos;
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de cursos: " + e.getMessage(), e);
        }
    }

    public boolean insert(CursoDTO curso) throws Exception {
        try {
            dao.insert(curso);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar Curso: " + e.getMessage(), e);
        }
    }

    public boolean existeCurso(CursoDTO curso) throws Exception{
        try {
            return dao.alreadyExist(curso);
        } catch (Exception e){
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }



}
