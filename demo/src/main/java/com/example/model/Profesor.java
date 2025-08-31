/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

/**
 *
 * @author gon
 */
public class Profesor extends Persona{
    private String tipoContrato;
    public Profesor(double ID, String nombres, String apellidos, String email, String tipoContrato){
        super(ID, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
    
    @Override
    public String toString(){
        return super.toString() + ", Tipo de contrato: " + tipoContrato;
    }
 }
  
