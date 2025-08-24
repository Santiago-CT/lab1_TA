/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

/**
 *
 * @author gon
 */
public class CursoProfesor{
    private Curso curso;
    private Profesor profesor;
    private int anio;
    private int semestre;
    
    public CursoProfesor(Curso curso, Profesor profesor, int anio, int semestre){
        this.curso = curso;
        this.profesor = profesor;
        this.anio = anio;
        this.semestre = semestre;
    }
    
    @Override
    public String toString(){
        return "\nCurso: " + curso + ", Profesor: " + profesor + ", Año: " + anio + ", Semestre: " + semestre;
    }
}
