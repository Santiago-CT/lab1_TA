package com.example.dao;
import com.example.model.DBConnection;
import com.example.model.Facultad;
import com.example.model.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class FacultadDao {
    private static final String SQL_INSERT = "INSERT INTO facultad (id, nombre, decano) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = """
            SELECT f.id, f.nombre, f.decano, 
                   p.nombres, p.apellidos
            FROM facultad f 
            LEFT JOIN persona p ON f.decano = p.id
            """;
    private static final String SQL_SELECT_BY_ID = "SELECT id, nombre, decano FROM facultad WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE facultad SET nombre = ?, decano = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM facultad WHERE id = ?";

    public static void insert(Facultad facultad) throws Exception {
        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción


            PreparedStatement psFacultad = cn.prepareStatement(SQL_INSERT);

            psFacultad.setDouble(1, facultad.getID());
            psFacultad.setString(2, facultad.getNombre());
            psFacultad.setDouble(3, facultad.getDecano().getID());
            psFacultad.executeUpdate();

            cn.commit();
        }
    }
    public static void show() throws Exception {
        String sql = """
                SELECT f.id, f.nombre, f.decano 
                FROM facultad as f
                """;
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
    public static boolean update(Facultad facultad) throws Exception {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, facultad.getNombre());
            if (facultad.getDecano() != null) {
                ps.setDouble(2, facultad.getDecano().getID());
            } else {
                ps.setNull(2, java.sql.Types.DOUBLE);
            }
            ps.setDouble(3, facultad.getID());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            throw new Exception("Error al actualizar facultad: " + e.getMessage(), e);
        }
    }
    public static boolean delete(double id) throws Exception {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_DELETE)) {

            ps.setDouble(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            throw new Exception("Error al eliminar facultad: " + e.getMessage(), e);
        }
    }
    public static List<Facultad> getAll() {
        List<Facultad> facultades = new ArrayList<>();

        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");

                Persona decano = null;
                if (rs.getObject("decano") != null) {
                    double decanoId = rs.getDouble("decano");
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");

                    if (nombres != null && apellidos != null) {
                        decano = new Persona(decanoId, nombres, apellidos, null);
                    }
                }

                Facultad facultad = new Facultad(id, nombre, decano);
                facultades.add(facultad);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return facultades;
    }

    public static boolean existeFacultad(double id) {
        String sql = "SELECT COUNT(*) FROM facultad WHERE id = ?";
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
