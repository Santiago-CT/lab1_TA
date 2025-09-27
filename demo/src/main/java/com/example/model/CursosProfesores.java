/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import com.example.services.Servicios;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gon
 */
public class CursosProfesores implements Servicios {

    private ArrayList<CursoProfesor> listado;

    @Override
    public String imprimirPosicion(int posicion) {
        if (posicion < 0 || posicion >= listado.size()) {
            return "Posición inválida";
        }
        return listado.get(posicion).toString();
    }

    @Override
    public int candidadActual() {
        return listado.size();
    }

    @Override
    public List<String> imprimirListado() {
        List<String> result = new ArrayList<>();
        for (CursoProfesor cursoProfesor : listado) {
            result.add(cursoProfesor.toString());
        }
        return result;
    }

    public CursosProfesores() {
        this.listado = new ArrayList<>();
    }

    public void inscribir(CursoProfesor cursoProfesor) {
        listado.add(cursoProfesor);
    }

    /**public void actualizar(CursoProfesor cursoProfesor) {
        int index = listado.indexOf(cursoProfesor);
        if (index != -1) {
            listado.set(index, cursoProfesor);
        }
    }**/

    public void guardarinformacion(CursoProfesor cursoProfesor) {
        
    }

    public void cargarDatos() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CursoProfesor cp : listado) {
            sb.append(cp.toString()).append("\n");
        }
        return sb.toString();
    }
}
