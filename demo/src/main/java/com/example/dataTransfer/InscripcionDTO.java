package com.example.dataTransfer;

public class InscripcionDTO implements DataTransfer{
    private double idEstudiante;
    private String nombreEstudiante;
    private int idCurso;
    private String nombreCurso;
    private int anio;
    private int semestre;
    public InscripcionDTO(double idEstudiante, String nombreEstudiante, int idCurso, String nombreCurso, int anio, int semestre){
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
        this.anio = anio;
        this.semestre = semestre;
    }

    public double getIdEstudiante() {
        return idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public int getAnio() {
        return anio;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setIdEstudiante(double idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }
}
