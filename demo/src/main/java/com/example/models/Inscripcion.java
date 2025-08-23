/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

/**
 *
 * @author gon
 */
public class Inscripcion{
    private Estudiante estudiante;
    private Curso curso;
    private int anio;
    private int semestre;
    public Inscripcion(Estudiante estudiante, Curso curso, int anio, int semestre){
        this.estudiante = estudiante;
        this.curso = curso;
        this.anio = anio;
        this.semestre = semestre;
    }
    
    
    @Override
    public String toString(){
        return "\nEstudiante: " + estudiante + ", Curso: " + curso + ", Año: " + anio + ", Semestre: " + semestre;
    }
}
