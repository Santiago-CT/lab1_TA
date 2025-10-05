package com.example.controller;

import com.example.controllerFXML.ObserverController;
import com.example.dataTransfer.DataTransfer;
import com.example.observer.Observable;
import com.example.observer.Observer;
import com.example.persistence.Persistence;
import com.example.dataTransfer.CursoDTO;
import com.example.factory.InternalFactory;

import java.util.ArrayList;
import java.util.List;

public class CursoController implements Observable {
    private static CursoController instance;
    private final Persistence dao;
    private final List<Observer> observerList;
    private static String lastInsert;

    public static String getLastInsert() {
        return lastInsert != null ? lastInsert : "No hay inserciones recientes";
    }
    public void setLastInsert(CursoDTO curso) {
        lastInsert = "ID: " + curso.getID() + ", Nombre: " + curso.getNombre() + ", ID Programa: " + curso.getIdPrograma() + ", Nombre Programa: " + curso.getNombrePrograma();
        notifyObservers();
    }

    private CursoController(){
        dao = InternalFactory.createCursoDAO();
        observerList = new ArrayList<>();
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
            setLastInsert(curso);
            return true;
        } catch (Exception e) {
            throw new Exception("Error al insertar Curso: " + e.getMessage(), e);
        }
    }

    public boolean alreadyExist(CursoDTO curso) throws Exception{
        try {
            return dao.alreadyExist(curso);
        } catch (Exception e){
            throw new Exception("Error al verificar existencia" + e.getMessage(), e);
        }
    }


    @Override
    public void addObserver(Observer o) {
        if (!observerList.contains(o)) {
            observerList.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o: observerList){
            o.update();
        }
    }
}
