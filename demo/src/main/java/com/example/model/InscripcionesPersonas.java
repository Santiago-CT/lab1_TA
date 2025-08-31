/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

import java.util.ArrayList;

/**
 *
 * @author gon
 */
public class InscripcionesPersonas {
    
    private ArrayList<Persona> listado;

    /**
     * 
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
        for (Persona persona : listado) {
            result.add(persona.toString());
        }
        return result;
    }
    * 
    * 
    **/
    

    public InscripcionesPersonas() {
        this.listado = new ArrayList<>();
    }

    public void inscribir(Persona persona) {
        listado.add(persona);
    }

    public void eliminar(Persona persona) {
        listado.remove(persona);
    }

    public void actualizar(Persona persona) {
        int index = listado.indexOf(persona);
        if (index != -1) {
            listado.set(index, persona);
        }
    }

    public void guardarinformacion() {

    }

    public void cargarDatos() {

    }
}
