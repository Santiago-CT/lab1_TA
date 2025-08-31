/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gon
 */
public class CursosInscritos implements Servicios {

    private ArrayList<Inscripcion> listado;

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
        for (Inscripcion inscripcion : listado) {
            result.add(inscripcion.toString());
        }
        return result;
    }

    public CursosInscritos() {
        this.listado = new ArrayList<>();
    }

    public void inscribirCurso(Inscripcion inscripcion) {
        listado.add(inscripcion);
    }

    public void eliminar(Inscripcion inscripcion) {
        listado.remove(inscripcion);
    }

    public void actualizar(Inscripcion inscripcion) {
        int index = listado.indexOf(inscripcion);
        if (index != -1) {
            listado.set(index, inscripcion);
        }
    }

    public void guardarinformacion() throws Exception {
        for (Inscripcion inscripcion : listado) {
            ConexionBD.insertInscripcion(inscripcion);
        }
        System.out.println("-- INFORMACION GUARDADA EN LA BASE DE DATOS --");
    }

    public void cargarDatos() throws Exception {
        System.out.println("-- CARGANDO INFORMACION DESDE LA BASE DE DATOS --");
        ConexionBD.showProfesores();
        ConexionBD.showFacultades();
        ConexionBD.showProgramas();
        ConexionBD.showEstudiantes();
        ConexionBD.showCursos();
        ConexionBD.showInscripciones();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Inscripcion inscripcion : listado) {
            sb.append(inscripcion.toString()).append("\n");
        }
        return sb.toString();
    }
}
