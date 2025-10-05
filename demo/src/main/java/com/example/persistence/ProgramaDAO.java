package com.example.persistence;
import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.ProgramaDTO;
import com.example.database.DataBase;
import com.example.database.Oracle;
import com.example.factory.InternalFactory;
import com.example.model.*;
import com.example.observer.Observer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDAO implements Persistence {
    private DataBase database;
    private static ProgramaDAO instance;

    private ProgramaDAO(){
        database = InternalFactory.createDB();
    }

    public static ProgramaDAO getInstance(){
        if (instance == null) instance = new ProgramaDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws Exception {
        String SQL_PROGRAMA="INSERT INTO programa (id, duracion, registro, nombre, facultad_id) VALUES (?, ?, ?, ?, ?)";
        ProgramaDTO programa = (ProgramaDTO) dataTransfer;
        if (alreadyExist(programa)){
            return;
        }
        try (Connection cn = database.getConnection()) {
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
    public List<DataTransfer> getAll() throws Exception {
        String sql = getSQL();

        List<DataTransfer> programas = new ArrayList<>();
        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                programas.add(
                        new ProgramaDTO(
                                rs.getDouble("id"),
                                rs.getString("nombre"),
                                rs.getDouble("duracion"),
                                rs.getDate("registro"),
                                rs.getDouble("facultad_id"),
                                rs.getString("facultad_nombre")
                        )
                );
            }
            return programas;
        }
    }

    private String getSQL() {
        String sql;
        if (database instanceof Oracle){
            sql = """
                SELECT p.id, p.nombre, p.duracion, p.registro,
                       f.id facultad_id, f.nombre facultad_nombre
                FROM programa p
                JOIN facultad f ON p.facultad_id = f.id
                ORDER BY p.nombre
                """;
        } else {
            sql = """
                SELECT p.id, p.nombre, p.duracion, p.registro,
                       f.id AS facultad_id, f.nombre AS facultad_nombre
                FROM programa AS p
                JOIN facultad AS f ON p.facultad_id = f.id
                ORDER BY p.nombre
                """;
        }
        return sql;
    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) {
        String sql = "SELECT COUNT(*) FROM programa WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ProgramaDTO programa = (ProgramaDTO) dataTransfer;
            stmt.setDouble(1, programa.getID());
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
