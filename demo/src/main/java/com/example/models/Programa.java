/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

import java.util.Date;

/**
 *
 * @author gon
 */
class Programa {

    private long ID;
    private String nombre;
    private double duracion;
    private Date registro;
    private Facultad facultad;

    public Programa(long ID, String nombre, double duracion, Date registro, Facultad facultad) {
        this.ID = ID;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }

    public long getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDuracion() {
        return duracion;
    }

    public Date getRegistro() {
        return registro;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
    
    

    @Override
    public String toString() {
        return "ID: " + this.ID + " Nombre: " + this.nombre 
                + " Duracion: " + this.duracion + " Registro: " 
                + this.registro + " Facultad: " + this.facultad.toString();
    }
}
