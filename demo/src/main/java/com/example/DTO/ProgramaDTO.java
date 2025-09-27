package com.example.DTO;

import java.sql.Date;

public class ProgramaDTO {
        private double ID;
        private String nombre;
        private double duracion;
        private Date registro;
        private double idFacultad;
        private String nombreFacultad;

        public ProgramaDTO(double ID, String nombre, double duracion, Date registro, double idFacultad, String nombreFacultad) {
            this.ID = ID;
            this.nombre = nombre;
            this.duracion = duracion;
            this.registro = registro;
            this.idFacultad = idFacultad;
            this.nombreFacultad = nombreFacultad;
        }

    public double getID() {
        return ID;
    }

    public double getIdFacultad() {
        return idFacultad;
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

    public void setID(double ID) {
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

    public void setIdFacultad(double idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }

    @Override
    public String toString(){return this.ID + " - " + this.nombre;}
}
