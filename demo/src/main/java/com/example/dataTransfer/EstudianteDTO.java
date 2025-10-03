package com.example.dataTransfer;

public class EstudianteDTO extends PersonaDTO implements DataTransfer {
    private double codigo;
    private double idPrograma;
    private String nombrePrograma;
    private boolean activo;
    private double promedio;

    public EstudianteDTO(double ID, String nombres, String apellidos, String email, double codigo, double idPrograma, String nombrePrograma, boolean activo, double promedio){
        super(ID, nombres, apellidos, email);
        this.codigo = codigo;
        this.idPrograma = idPrograma;
        this.nombrePrograma = nombrePrograma;
        this.activo = activo;
        this.promedio = promedio;
    }

    public double getCodigo() {
        return codigo;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
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

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(double idPrograma) {
        this.idPrograma = idPrograma;
    }
}
