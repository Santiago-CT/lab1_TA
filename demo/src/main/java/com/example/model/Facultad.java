/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

/**
 *
 * @author gon
 */
public class Facultad{
    private double ID;
    private String nombre;
    private Persona decano;
    public Facultad(double id, String nombre, Persona decano){
        this.ID = id;
        this.nombre = nombre;
        this.decano = decano;
    }

    public double getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public Persona getDecano() {
        return decano;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDecano(Persona decano) {
        this.decano = decano;
    }
    
    
    @Override
    public String toString(){
        return "ID: " + this.ID + " Nombre: " + this.nombre 
                + " Decano: " + this.decano.toString();
    }
}
