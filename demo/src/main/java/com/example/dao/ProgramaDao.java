package com.example.dao;
import com.example.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProgramaDao {
    private static final String SQL_PROGRAMA="INSERT INTO programa (id, duracion, registro, nombre, facultad_id) VALUES (?, ?, ?, ?, ?)";
    public static void insert(Programa programa) throws Exception {
        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción


            PreparedStatement psPrograma = cn.prepareStatement(SQL_PROGRAMA);

            psPrograma.setDouble(1, programa.getID());
            psPrograma.setDouble(2, programa.getDuracion());
            psPrograma.setDate(3, programa.getRegistro());
            psPrograma.setString(4, programa.getNombre());
            psPrograma.setDouble(5, programa.getFacultad().getID());
            psPrograma.executeUpdate();

            cn.commit();
        }
    }


    public static void show() throws Exception {
        String sql = """
                SELECT p.id, p.duracion, p.registro, p.nombre, p.facultad_id
                FROM programa as P
                """;
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
    public static ArrayList<Programa> get() throws Exception {
        String sql = """
                SELECT p.id, p.nombre, p.duracion, p.registro,
                       f.id AS facultad_id, f.nombre AS facultad_nombre
                FROM programa p
                JOIN facultad f ON p.facultad_id = f.id
                ORDER BY p.nombre
                """;

        ArrayList<Programa> programas = new ArrayList<>();
        try (Connection cn = DBConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Crear Facultad
                Facultad facultad = new Facultad(
                        rs.getDouble("facultad_id"),
                        rs.getString("facultad_nombre"),
                        null // Lista de programas null para evitar referencia circular
                );

                // Crear Programa
                Programa programa = new Programa(
                        rs.getDouble("id"),
                        rs.getString("nombre"),
                        rs.getDouble("duracion"),
                        rs.getDate("registro"),
                        facultad
                );

                programas.add(programa);
            }
            return programas;
        }
    }

    public static boolean existePrograma(double id) {
        String sql = "SELECT COUNT(*) FROM programa WHERE id = ?";
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
