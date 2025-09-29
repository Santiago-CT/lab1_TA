package com.example.controller;

import com.example.DAO.DAO;
import com.example.DTO.CursoDTO;
import com.example.factory.InternalFactory;

import java.util.ArrayList;
import java.util.List;

public class CursoController {
    private final DAO dao;
    public CursoController(){
        dao = InternalFactory.createCursoDAO();
    }
    public List<CursoDTO> getAll() throws Exception {
        try{
            List<Object> cursosDAO = dao.getAll();
            List<CursoDTO> cursos = new ArrayList<>();
            for (Object c : cursosDAO){
                CursoDTO curso = (CursoDTO) c;
                cursos.add(
                        new CursoDTO(
                                curso.getID(),
                                curso.getNombre(),
                                curso.getIdPrograma(),
                                curso.getNombrePrograma(),
                                curso.isActivo()
                        )
                );
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
            throw new Exception("Error al insertar Facultad: " + e.getMessage(), e);
        }
    }

    public boolean existeCurso(int id) throws Exception{
        try {
            return dao.alreadyExist(id);
        } catch (Exception e){
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }



}
