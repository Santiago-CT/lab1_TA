/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

/**
 *
 * @author gon
 */
public class Curso {

    private int ID;
    private String nombre;
    private Programa programa;
    private boolean activo;

    public Curso(int ID, String nombre, Programa programa, boolean activo) {
        this.ID = ID;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public Programa getPrograma() {
        return programa;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    

    @Override
    public String toString() {
        return "ID: " + ID + ", Nombre: " + nombre + ", Programa: " + programa + ", Activo: " + activo;
    }
}
