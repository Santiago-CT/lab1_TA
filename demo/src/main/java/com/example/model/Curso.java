package com.example.model;

public class Curso extends Observable {
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

    // --- Getters ---
    public int getID() { return ID; }
    public String getNombre() { return nombre; }
    public boolean isActivo() { return activo; }
    public Programa getPrograma() { return programa; }

    // --- Setters que notifican cambios ---
    public void setID(int ID) {
        this.ID = ID;
        notificarObservadores(this);
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        notificarObservadores(this);
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
        notificarObservadores(this);
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        notificarObservadores(this);
    }

    @Override
    public String toString() {
        return "ID: " + ID + ", Nombre: " + nombre + ", Programa: " + programa + ", Activo: " + activo;
    }
}