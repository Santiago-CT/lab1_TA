/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

/**
 *
 * @author gon
 */
public class Persona{
    private long ID;
    private String nombres;
    private String apellidos;
    private String email;

    public Persona(long ID, String nombres, String apellidos, String email){
        this.ID = ID;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    public long getID() {
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

    public void setID(long ID) {
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
    public String toString(){
        return "ID: " + ID + ", Nombres: " + nombres + ", Apellidos: " + apellidos + ", Email: " + email;
    }
}
