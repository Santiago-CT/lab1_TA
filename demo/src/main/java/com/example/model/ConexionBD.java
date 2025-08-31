/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.model;

/**
 *
 * @author gon
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ConexionBD {

    public static Connection getConnection() throws Exception {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // protocolo:motor:ubicacion
        // JDBC es la API que permite a Java conectarse a bases de datos relacionales.
        // Indica que el driver y el motor de la base de datos es H2
        // testdb es el nombre del archivo que contendrá la base de datos H2
        // DB_CLOSE_DELAY=-1 mantiene la BD en memoria viva hasta que termine la JVM,
        // incluso si cierras la última conexión.
        String user = "sa"; // significa System Administrator, el superusuario en H2
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }

    public static void crearTablas() {
        ArrayList<String> ddls = new ArrayList<>();
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS persona (
                      id DOUBLE PRIMARY KEY,
                      nombres VARCHAR(100) NOT NULL,
                      apellidos VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL
                  );
            """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS profesor (
                      persona_id DOUBLE PRIMARY KEY,
                      tipoContrato VARCHAR(100) NOT NULL,
                      FOREIGN KEY (persona_id) REFERENCES persona(id)
                  );
            """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS facultad (
                      id DOUBLE PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      decano DOUBLE NOT NULL,
                      FOREIGN KEY (decano) REFERENCES persona(id)
                  );
            """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS programa (
                      id DOUBLE PRIMARY KEY,
                      duracion DOUBLE NOT NULL,
                      registro DATE NOT NULL,
                      nombre VARCHAR(100) NOT NULL,
                      facultad_id DOUBLE NOT NULL,
                      FOREIGN KEY (facultad_id) REFERENCES facultad(id)
                  );
                  """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS estudiante (
                      persona_id DOUBLE PRIMARY KEY,
                      codigo DOUBLE NOT NULL,
                      programa_id DOUBLE NOT NULL,
                      activo BOOLEAN NOT NULL,
                      promedio DOUBLE NOT NULL,
                      FOREIGN KEY (persona_id) REFERENCES persona(id),
                      FOREIGN KEY (programa_id) REFERENCES programa(id)
                  );
                  """);
        ddls.add("""
                  CREATE TABLE IF NOT EXISTS curso (
                      id INT PRIMARY KEY,
                      nombre VARCHAR(100) NOT NULL,
                      programa_id DOUBLE NOT NULL,
                      activo BOOLEAN NOT NULL,
                      FOREIGN KEY (programa_id) REFERENCES programa(id)
                  );
                    """);
        ddls.add("""
                    CREATE TABLE IF NOT EXISTS inscripcion (
                      estudiante_id DOUBLE NOT NULL,
                      curso_id INT NOT NULL,
                      anio INT NOT NULL,
                      semestre INT NOT NULL,
                      PRIMARY KEY (estudiante_id, curso_id, anio, semestre),
                      FOREIGN KEY (estudiante_id) REFERENCES estudiante(persona_id),
                      FOREIGN KEY (curso_id) REFERENCES curso(id)
                    );
                 """);

        try (Connection cn = ConexionBD.getConnection(); Statement st = cn.createStatement()) {
            for (String ddl : ddls) {
                st.execute(ddl);
            }
            System.out.println("Tablas creadas con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String sqlPersona = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";

    public static void insertProfesor(Profesor profesor) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlProfesor = "INSERT INTO profesor (persona_id, tipoContrato) VALUES (?, ?)";

            PreparedStatement psPersona = cn.prepareStatement(sqlPersona);
            PreparedStatement psProfesor = cn.prepareStatement(sqlProfesor);

            psPersona.setDouble(1, profesor.getID());
            psPersona.setString(2, profesor.getNombres());
            psPersona.setString(3, profesor.getApellidos());
            psPersona.setString(4, profesor.getEmail());
            psPersona.executeUpdate();

            psProfesor.setDouble(1, profesor.getID());
            psProfesor.setString(2, profesor.getTipoContrato());
            psProfesor.executeUpdate();

            cn.commit();
        }
    }

    public static void insertEstudiante(Estudiante estudiante) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlEstudiante = "INSERT INTO estudiante (persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement psPersona = cn.prepareStatement(sqlPersona);
            PreparedStatement psEstudiante = cn.prepareStatement(sqlEstudiante);

            psPersona.setDouble(1, estudiante.getID());
            psPersona.setString(2, estudiante.getNombres());
            psPersona.setString(3, estudiante.getApellidos());
            psPersona.setString(4, estudiante.getEmail());
            psPersona.executeUpdate();

            psEstudiante.setDouble(1, estudiante.getID());
            psEstudiante.setDouble(2, estudiante.getCodigo());
            psEstudiante.setDouble(3, estudiante.getPrograma().getID());
            psEstudiante.setBoolean(4, estudiante.isActivo());
            psEstudiante.setDouble(5, estudiante.getPromedio());
            psEstudiante.executeUpdate();

            cn.commit();
        }
    }

    public static void insertFacultad(Facultad facultad) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlFacultad = "INSERT INTO facultad (id, nombre, decano) VALUES (?, ?, ?)";

            PreparedStatement psFacultad = cn.prepareStatement(sqlFacultad);

            psFacultad.setDouble(1, facultad.getID());
            psFacultad.setString(2, facultad.getNombre());
            psFacultad.setDouble(3, facultad.getDecano().getID());
            psFacultad.executeUpdate();

            cn.commit();
        }
    }

    public static void insertPrograma(Programa programa) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlPrograma = "INSERT INTO programa (id, duracion, registro, nombre, facultad_id) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement psPrograma = cn.prepareStatement(sqlPrograma);

            psPrograma.setDouble(1, programa.getID());
            psPrograma.setDouble(2, programa.getDuracion());
            psPrograma.setDate(3, programa.getRegistro());
            psPrograma.setString(4, programa.getNombre());
            psPrograma.setDouble(5, programa.getFacultad().getID());
            psPrograma.executeUpdate();

            cn.commit();
        }
    }

    public static void insertCurso(Curso curso) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlCurso = "INSERT INTO curso (id, nombre, programa_id, activo) VALUES (?, ?, ?, ?)";

            PreparedStatement psCurso = cn.prepareStatement(sqlCurso);

            psCurso.setInt(1, curso.getID());
            psCurso.setString(2, curso.getNombre());
            psCurso.setDouble(3, curso.getPrograma().getID());
            psCurso.setBoolean(4, curso.isActivo());
            psCurso.executeUpdate();

            cn.commit();
        }
    }

    public static void insertInscripcion(Inscripcion inscripcion) throws Exception {
        try (Connection cn = ConexionBD.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            String sqlInscripcion = "INSERT INTO inscripcion (estudiante_id, curso_id, anio, semestre) VALUES (?, ?, ?, ?)";
            PreparedStatement psInscripcion = cn.prepareStatement(sqlInscripcion);

            psInscripcion.setDouble(1, inscripcion.getEstudiante().getID());
            psInscripcion.setInt(2, inscripcion.getCurso().getID());
            psInscripcion.setInt(3, inscripcion.getAnio());
            psInscripcion.setInt(4, inscripcion.getSemestre());
            psInscripcion.executeUpdate();

            cn.commit();
        }
    }

    public static void showProfesores() throws Exception {
        String sql = """
                     SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
                     FROM persona as p
                     JOIN profesor as pr ON p.id = pr.persona_id
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nPROFESORES");
            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                String tipoContrato = rs.getString("tipoContrato");

                System.out.printf("ID: %f | Nombre: %s %s | Email: %s | Contrato: %s%n",
                        id, nombres, apellidos, email, tipoContrato);
            }
        }
    }

    public static void showFacultades() throws Exception {
        String sql = """
                     SELECT f.id, f.nombre, f.decano 
                     FROM facultad as f
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nFACULTADES");
            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");
                double decano = rs.getDouble("decano");

                System.out.printf("ID: %f | Nombre: %s | Decano: %f%n",
                        id, nombre, decano);
            }
        }

    }
    
    public static void showProgramas() throws Exception {
        String sql = """
                     SELECT p.id, p.duracion, p.registro, p.nombre, p.facultad_id
                     FROM programa as P
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nPROGRAMAS");
            while (rs.next()) {
                double id = rs.getDouble("id");
                double duracion = rs.getDouble("duracion");
                String registro = rs.getString("registro");
                String nombre = rs.getString("nombre");
                double facultad_id = rs.getDouble("facultad_id");

                System.out.printf("ID: %f | Duracion: %f | Registro: %s | Nombre: %s | Facultad: %f%n",
                        id, duracion, registro, nombre, facultad_id);
            }
        }
        
    }
    
    public static void showEstudiantes() throws Exception {
        String sql = """
                     SELECT e.persona_id, e.codigo, e.programa_id, e.activo, e.promedio
                     FROM estudiante as e
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nESTUDIANTES");
            while (rs.next()) {
                double persona_id = rs.getDouble("persona_id");
                double codigo = rs.getDouble("codigo");
                double programa_id = rs.getDouble("programa_id");
                boolean activo = rs.getBoolean("activo");
                double promedio = rs.getDouble("promedio");
                
                System.out.printf("ID: %f | Codigo: %f | programa_id: %f | Activo: %s | Promedio: %f%n",
                        persona_id, codigo, programa_id, activo, promedio);
            }
        }
        
    }
    
    public static void showCursos() throws Exception {
        String sql = """
                     SELECT c.id, c.nombre, c.programa_id, c.activo
                     FROM curso as c
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nCURSOS");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double programa_id = rs.getDouble("programa_id");
                boolean activo = rs.getBoolean("activo");
                
                System.out.printf("ID: %d | Nombre: %s | programa_id: %f | Activo: %s%n",
                        id, nombre, programa_id, activo);
            }
        }
        
    }
    
    public static void showInscripciones() throws Exception {
        String sql = """
                     SELECT i.estudiante_id, i.curso_id, i.anio, i.semestre
                     FROM inscripcion as i
                     """;
        try (Connection cn = ConexionBD.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.println("\nINSCRIPCIONES");
            while (rs.next()) {
                double estudiante_id = rs.getDouble("estudiante_id");
                int curso_id = rs.getInt("curso_id");
                int anio = rs.getInt("anio");
                int semestre = rs.getInt("semestre");
                
                System.out.printf("estudiante_id: %f | curso_id: %d | Año: %d | Semestre: %d%n",
                        estudiante_id, curso_id, anio, semestre);
            }
        }
        
    }

}