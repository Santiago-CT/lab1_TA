package com.example.DTO;

public class PersonaDTO {
    private double ID;
    private String nombres;
    private String apellidos;
    private String email;

    public PersonaDTO(double ID, String nombres, String apellidos, String email){
        this.ID = ID;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }
    public PersonaDTO(double ID, String nombres, String apellidos){
        this.ID = ID;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = null;
    }

    public double getID() {
        return ID;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setID(double ID) {
        this.ID = ID;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){return this.ID + " - " + this.nombres + " " + this.apellidos;}
}
