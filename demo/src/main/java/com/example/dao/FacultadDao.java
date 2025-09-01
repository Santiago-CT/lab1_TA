package com.example.dao;
import com.example.model.DBConnection;
import com.example.model.Facultad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FacultadDao {
    private static final String SQL_INSERT = "INSERT INTO facultad (id, nombre, decano) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT id, nombre, decano FROM facultad";
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
            ps.setDouble(2, facultad.getDecano().getID());
            ps.setDouble(3, facultad.getID());

            ps.executeUpdate();
        }
        return false;
    }
    public static boolean delete(double id) throws Exception {
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_DELETE)) {

            ps.setDouble(1, id);
            ps.executeUpdate();
        }
        return false;
    }

}
