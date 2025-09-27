package com.example.DTO;


public class CursoDTO {
    private int ID;
    private String nombre;
    private double idPrograma;
    private String nombrePrograma;
    private boolean activo;

    public CursoDTO(int ID, String nombre, double idPrograma, String nombrePrograma, boolean activo) {
        this.ID = ID;
        this.nombre = nombre;
        this.idPrograma = idPrograma;
        this.nombrePrograma = nombrePrograma;
        this.activo = activo;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public double getIdPrograma() {
        return idPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
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

    public void setIdPrograma(double idPrograma) {
        this.idPrograma = idPrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString(){
        return this.ID + " " + this.nombre;
    }
}
