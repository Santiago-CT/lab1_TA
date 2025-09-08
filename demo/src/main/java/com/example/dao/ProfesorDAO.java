package com.example.dao;

import com.example.model.ConexionBD;
import com.example.model.DBConnection;
import com.example.model.Profesor;

import java.sql.*;
import java.util.ArrayList;

public class ProfesorDAO {

    private static final String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
    private static final String SQL_PROFESOR = "INSERT INTO profesor (persona_id, tipoContrato) VALUES (?, ?)";
    public static ArrayList<Profesor> get() {
        String sql = """
                SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
                FROM persona as p
                JOIN profesor as pr ON p.id = pr.persona_id
                """;
        ArrayList<Profesor> profesores = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombres = rs.getString("nombres");
                String apellidos = rs.getString("apellidos");
                String email = rs.getString("email");
                String tipoContrato = rs.getString("tipoContrato");

                profesores.add(new Profesor(id, nombres, apellidos, email, tipoContrato));

            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            return profesores;
        }
    }
    public static void insert(Profesor profesor) throws SQLException {
        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psPersona = cn.prepareStatement(SQL_PERSONA);
                 PreparedStatement psProfesor = cn.prepareStatement(SQL_PROFESOR)) {

                psPersona.setDouble(1, profesor.getID());
                psPersona.setString(2, profesor.getNombres());
                psPersona.setString(3, profesor.getApellidos());
                psPersona.setString(4, profesor.getEmail());
                psPersona.executeUpdate();

                psProfesor.setDouble(1, profesor.getID());
                psProfesor.setString(2, profesor.getTipoContrato());
                psProfesor.executeUpdate();

                cn.commit();
            } catch (SQLException e) {
                cn.rollback();
                throw e;
            }
        }
    }

    public static boolean eliminar(double id) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String sqlProfesor = "DELETE FROM profesor WHERE persona_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlProfesor)) {
                    stmt.setDouble(1, id);
                    stmt.executeUpdate();
                }

                String sqlPersona = "DELETE FROM persona WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlPersona)) {
                    stmt.setDouble(1, id);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Profesor getById(double id) {
        String sql = """
            SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
            FROM persona p
            JOIN profesor pr ON p.id = pr.persona_id
            WHERE p.id = ?
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setDouble(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Profesor(
                            rs.getDouble("id"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("tipoContrato")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Profesor> getAll() {
        ArrayList<Profesor> profesores = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
            FROM persona p
            JOIN profesor pr ON p.id = pr.persona_id
        """;

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                profesores.add(new Profesor(
                        rs.getDouble("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("tipoContrato")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profesores;
    }

    public static boolean actualizar(Profesor profesor) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Iniciar transacción

            try {
                // Verificar que el email no esté en uso por otra persona
                if (existeEmail(profesor.getEmail(), profesor.getID())) {
                    System.err.println("El email ya está en uso por otra persona");
                    return false;
                }

                // Actualizar tabla persona
                String sqlPersona = "UPDATE persona SET nombres = ?, apellidos = ?, email = ? WHERE id = ?";
                try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                    stmtPersona.setString(1, profesor.getNombres());
                    stmtPersona.setString(2, profesor.getApellidos());
                    stmtPersona.setString(3, profesor.getEmail());
                    stmtPersona.setDouble(4, profesor.getID());
                    stmtPersona.executeUpdate();
                }

                // Actualizar tabla profesor
                String sqlProfesor = "UPDATE profesor SET tipoContrato = ? WHERE persona_id = ?";
                try (PreparedStatement stmtProfesor = conn.prepareStatement(sqlProfesor)) {
                    stmtProfesor.setString(1, profesor.getTipoContrato());
                    stmtProfesor.setDouble(2, profesor.getID());
                    int filasAfectadas = stmtProfesor.executeUpdate();

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

    public static boolean existeProfesor(double id) {
        String sql = "SELECT COUNT(*) FROM profesor WHERE persona_id = ?";
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

}
