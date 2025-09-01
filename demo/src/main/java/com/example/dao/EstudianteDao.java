package com.example.dao;

import com.example.model.DBConnection;
import com.example.model.Estudiante;
import com.example.model.Facultad;
import com.example.model.Programa;


import java.sql.*;
import java.util.ArrayList;

public class EstudianteDao {

    private static final String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
    private static final String SQL_ESTUDIANTE = "INSERT INTO estudiante (persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";

    // Insertar estudiante
    public static void insert(Estudiante estudiante) throws Exception {
        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false);

            PreparedStatement psPersona = cn.prepareStatement(SQL_PERSONA);
            PreparedStatement psEstudiante = cn.prepareStatement(SQL_ESTUDIANTE);

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
    public static boolean eliminar(double personaId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try {
                // Primero eliminar las inscripciones del estudiante
                String sqlInscripciones = "DELETE FROM inscripcion WHERE estudiante_id = ?";
                try (PreparedStatement stmtInscripciones = conn.prepareStatement(sqlInscripciones)) {
                    stmtInscripciones.setDouble(1, personaId);
                    stmtInscripciones.executeUpdate();
                }

                // Luego eliminar de la tabla estudiante
                String sqlEstudiante = "DELETE FROM estudiante WHERE persona_id = ?";
                try (PreparedStatement stmtEstudiante = conn.prepareStatement(sqlEstudiante)) {
                    stmtEstudiante.setDouble(1, personaId);
                    stmtEstudiante.executeUpdate();
                }

                // Finalmente eliminar de la tabla persona
                String sqlPersona = "DELETE FROM persona WHERE id = ?";
                try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                    stmtPersona.setDouble(1, personaId);
                    int filasAfectadas = stmtPersona.executeUpdate();

                    if (filasAfectadas > 0) {
                        conn.commit(); // Confirmar transacción
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir transacción en caso de error
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Estudiante> get() throws Exception {
        String sql = """
                SELECT p.id, p.nombres, p.apellidos, p.email,
                       e.codigo, e.activo, e.promedio,
                       prog.id AS programa_id, prog.nombre AS programa_nombre,
                       prog.duracion, prog.registro,
                       f.id AS facultad_id, f.nombre AS facultad_nombre
                FROM persona p
                JOIN estudiante e ON p.id = e.persona_id
                JOIN programa prog ON e.programa_id = prog.id
                JOIN facultad f ON prog.facultad_id = f.id
                """;

        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                double codigo = rs.getDouble("codigo");
                boolean activo = rs.getBoolean("activo");
                double promedio = rs.getDouble("promedio");

                // Crear Facultad
                Facultad facultad = new Facultad(
                        rs.getDouble("facultad_id"),
                        rs.getString("facultad_nombre"),
                        null
                );

                // Crear Programa
                Programa programa = new Programa(
                        rs.getDouble("programa_id"),
                        rs.getString("programa_nombre"),
                        rs.getDouble("duracion"),
                        rs.getDate("registro"),
                        facultad
                );

                estudiantes.add(new Estudiante(id, nombres, apellidos, email, codigo, programa, activo, promedio));
            }
            return estudiantes;
        }
    }
    public static boolean actualizar(Estudiante estudiante) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try {
                // Verificar que el email no esté en uso por otra persona
                if (existeEmail(estudiante.getEmail(), estudiante.getID())) {
                    System.err.println("El email ya está en uso por otra persona");
                    return false;
                }

                // Verificar que el código no esté en uso por otro estudiante
                if (existeCodigoEstudiante(estudiante.getCodigo(), estudiante.getID())) {
                    System.err.println("El código ya está en uso por otro estudiante");
                    return false;
                }

                // Actualizar tabla persona
                String sqlPersona = "UPDATE persona SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
                try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                    stmtPersona.setString(1, estudiante.getNombres());
                    stmtPersona.setString(2, estudiante.getApellidos());
                    stmtPersona.setString(3, estudiante.getEmail());
                    stmtPersona.setDouble(4, estudiante.getID());
                    stmtPersona.executeUpdate();
                }

                // Actualizar tabla estudiante
                String sqlEstudiante = "UPDATE estudiante SET codigo = ?, programa_id = ?, activo = ?, promedio = ? WHERE persona_id = ?";
                try (PreparedStatement stmtEstudiante = conn.prepareStatement(sqlEstudiante)) {
                    stmtEstudiante.setDouble(1, estudiante.getCodigo());
                    stmtEstudiante.setDouble(2, estudiante.getPrograma().getID());
                    stmtEstudiante.setBoolean(3, estudiante.isActivo());
                    stmtEstudiante.setDouble(4, estudiante.getPromedio());
                    stmtEstudiante.setDouble(5, estudiante.getID());
                    int filasAfectadas = stmtEstudiante.executeUpdate();

                    if (filasAfectadas > 0) {
                        conn.commit(); // Confirmar transacción
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir transacción en caso de error
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean existeCodigoEstudiante(double codigo, double idExcluir) {
        String sql = "SELECT COUNT(*) FROM estudiante e JOIN persona p ON e.persona_id = p.id WHERE e.codigo = ? AND p.id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, codigo);
            stmt.setDouble(2, idExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean existeEstudiante(double id) {
        String sql = "SELECT COUNT(*) FROM estudiante WHERE persona_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean existeEmail(String email, double idExcluir) {
        String sql = "SELECT COUNT(*) FROM persona WHERE email = ? AND id != ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setDouble(2, idExcluir);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
