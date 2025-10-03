package com.example.dataTransfer;


public class FacultadDTO implements DataTransfer {
    private double ID;
    private String nombre;
    private double idDecano;
    private String nombreDecano;
    public FacultadDTO(double id, String nombre, double idDecano, String nombreDecano){
        this.ID = id;
        this.nombre = nombre;
        this.idDecano = idDecano;
        this.nombreDecano = nombreDecano;
    }

    public double getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public double getIdDecano() {
        return idDecano;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIdDecano(double idDecano) {
        this.idDecano = idDecano;
    }

    public String getNombreDecano() {
        return nombreDecano;
    }

    public void setNombreDecano(String nombreDecano) {
        this.nombreDecano = nombreDecano;
    }

    @Override
    public String toString(){return this.ID + " - " + this.nombre;}
}
