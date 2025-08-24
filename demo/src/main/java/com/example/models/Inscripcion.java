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

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public int getAnio() {
        return anio;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
    
    
    @Override
    public String toString(){
        return "\nEstudiante: " + estudiante + ", Curso: " + curso + ", Año: " + anio + ", Semestre: " + semestre;
    }
}
