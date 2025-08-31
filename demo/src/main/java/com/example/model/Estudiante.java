/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

/**
 *
 * @author gon
 */
public class Estudiante extends Persona{
    private double codigo;
    private Programa programa;
    private boolean activo;
    private double promedio;
    public Estudiante(double ID, String nombres, String apellidos, String email, double codigo, Programa programa, boolean activo, double promedio){
        super(ID, nombres, apellidos, email);
        this.codigo = codigo;
        this.programa = programa;
        this.activo = activo;
        this.promedio = promedio;
    }

    public double getCodigo() {
        return codigo;
    }

    public Programa getPrograma() {
        return programa;
    }

    public boolean isActivo() {
        return activo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setCodigo(double codigo) {
        this.codigo = codigo;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }
    
    @Override
    public String toString(){
        return super.toString() + ", Código: " + codigo + ", Programa: " +programa + ", Activo: " + activo + ", Promedio: " + promedio;
}}
