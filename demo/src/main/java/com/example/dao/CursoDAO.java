package com.example.dao;
import com.example.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CursoDAO {

    private static final String SQL_CURSO ="INSERT INTO curso (id, nombre, programa_id, activo) VALUES (?, ?, ?, ?)";

    public static void insert(Curso curso) throws Exception {
        try (Connection cn = DBConnection.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción


            PreparedStatement psCurso = cn.prepareStatement(SQL_CURSO);

            psCurso.setInt(1, curso.getID());
            psCurso.setString(2, curso.getNombre());
            psCurso.setDouble(3, curso.getPrograma().getID());
            psCurso.setBoolean(4, curso.isActivo());
            psCurso.executeUpdate();

            cn.commit();
        }
    }
    public static void show() throws Exception {
        String sql = """
                SELECT c.id, c.nombre, c.programa_id, c.activo
                FROM curso as c
                """;
        try (Connection cn = DBConnection.getConnection(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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


}
