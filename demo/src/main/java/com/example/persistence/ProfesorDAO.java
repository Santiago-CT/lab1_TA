package com.example.persistence;

import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.ProfesorDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements Persistence {
    private final DataBase database;
    private static ProfesorDAO instance;

    private ProfesorDAO(){
        database = InternalFactory.createDB();
    }

    public static ProfesorDAO getInstance(){
        if (instance == null) instance = new ProfesorDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws SQLException {
        String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
        String SQL_PROFESOR = "INSERT INTO profesor (persona_id, tipoContrato) VALUES (?, ?)";
        ProfesorDTO profesor = (ProfesorDTO) dataTransfer;
        if (alreadyExist(profesor)){
            return;
        }
        try (Connection cn = database.getConnection()) {
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

    @Override
    public List<DataTransfer> getAll() throws Exception{
        List<DataTransfer> profesores = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
            FROM persona p
            JOIN profesor pr ON p.id = pr.persona_id
        """;

        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                profesores.add(
                        new ProfesorDTO(
                            rs.getDouble("id"),
                            rs.getString("nombres"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("tipoContrato")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return profesores;
    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) {
        String sql = "SELECT COUNT(*) FROM profesor WHERE persona_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ProfesorDTO profesor = (ProfesorDTO) dataTransfer;
            stmt.setDouble(1, profesor.getID());
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
                    FROM profesor
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
