package com.example;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

class Persona{
    private double ID;
    private String nombres;
    private String apellidos;
    private String email;

    public Persona(double ID, String nombres, String apellidos, String email){
        this.ID = ID;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }
    @Override
    public String toString(){
        return "ID: " + ID + ", Nombres: " + nombres + ", Apellidos: " + apellidos + ", Email: " + email;
    }
}

class Profesor extends Persona{
    private String tipoContrato;
    public Profesor(double ID, String nombres, String apellidos, String email, String tipoContrato){
        super(ID, nombres, apellidos, email);
        this.tipoContrato = tipoContrato;
    }
    @Override
    public String toString(){
        return super.toString() + ", Tipo de contrato: " + tipoContrato;
    }
 }
  
class Estudiante extends Persona{
    private double codigo;
    private Programa programa;
    private boolean activo;
    private boolean promedio;
    public Estudiante(double ID, String nombres, String apellidos, String email, double codigo, Programa programa, boolean activo, boolean promedio){
        super(ID, nombres, apellidos, email);
        this.codigo = codigo;
        this.programa = programa;
        this.activo = activo;
        this.promedio = promedio;
    }
    @Override
    public String toString(){
        return super.toString() + ", Código: " + codigo + ", Programa: " +programa + ", Activo: " + activo + ", Promedio: " + promedio;
}}
class Programa {
    private double ID;
    private String nombre;
    private double duracion;
    private Date registro;
    private Facultad facultad;
    public Programa(double id,String nombre, double duracion, Date registro, Facultad facultad){
        this.ID=ID;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }
    @Override
    public String toString(){
        return "";
    }
}
class Facultad{
    private double ID;
    private String nombre;
    private Persona decano;
    public Facultad(double id, String nombres,Persona decano){
        this.ID = id;
        this.nombre = nombre;
        this.decano = decano;
    }
    @Override
    public String toString(){
        return "";
    }
}

class Curso{
        private int ID;
        private String nombre;
        private Programa programa;
        private boolean activo;
        public Curso(int ID, String nombre, Programa programa, boolean activo){
            this.ID = ID;
            this.nombre = nombre;
            this.programa = programa;
            this.activo = activo;
        }
        @Override
        public String toString(){
            return "ID: " + ID + ", Nombre: " + nombre + ", Programa: " + programa + ", Activo: " + activo;
        }
}
class Inscripcion{
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
        return "Estudiante: " + estudiante + ", Curso: " + curso + ", Año: " + anio + ", Semestre: " + semestre;
    }
}

class CursoProfesor{
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
        return "Curso: " + curso + ", Profesor: " + profesor + ", Año: " + anio + ", Semestre: " + semestre;
    }
}
