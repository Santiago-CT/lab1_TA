package com.example.DAO;
import com.example.DTO.ProgramaDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO implements DAO{

    private final DataBase database;

    public ProgramaDAO(){
        database = InternalFactory.createDB();
    }

    @Override
    public void insert(Object obj) throws Exception {
        String SQL_PROGRAMA="INSERT INTO programa (id, duracion, registro, nombre, facultad_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = database.getConnection()) {
            ProgramaDTO programa = (ProgramaDTO) obj;
            cn.setAutoCommit(false); // para asegurar transacción
            PreparedStatement psPrograma = cn.prepareStatement(SQL_PROGRAMA);

            psPrograma.setDouble(1, programa.getID());
            psPrograma.setDouble(2, programa.getDuracion());
            psPrograma.setDate(3, programa.getRegistro());
            psPrograma.setString(4, programa.getNombre());
            psPrograma.setDouble(5, programa.getIdFacultad());
            psPrograma.executeUpdate();

            cn.commit();
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<Object> getAll() throws Exception {
        String sql = """
                SELECT p.id, p.nombre, p.duracion, p.registro,
                       f.id AS facultad_id, f.nombre AS facultad_nombre
                FROM programa p
                JOIN facultad f ON p.facultad_id = f.id
                ORDER BY p.nombre
                """;

        List<Object> programas = new ArrayList<>();
        try (Connection cn = database.getConnection();
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

    @Override
    public boolean alreadyExist(Object obj) {
        String sql = "SELECT COUNT(*) FROM programa WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, (double) obj);
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

    @Override
    public int count() throws Exception {
        String sql = """
                    SELECT COUNT(*)
                    FROM programa
                    """;
        try (Connection cn = database.getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery();
        ){
            if (rs.next()) {
                return rs.getInt(1);
            }

        }
        return 0;
    }

}
