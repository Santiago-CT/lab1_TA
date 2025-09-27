package com.example.DAO;

import com.example.DTO.ProfesorDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.Profesor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements DAO{
    private final DataBase database;

    public ProfesorDAO(){
        database = InternalFactory.createDB();
    }

    @Override
    public void insert(Object obj) throws SQLException {
        String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
        String SQL_PROFESOR = "INSERT INTO profesor (persona_id, tipoContrato) VALUES (?, ?)";
        try (Connection cn = database.getConnection()) {
            ProfesorDTO profesor = (ProfesorDTO) obj;
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
    public List<Object> getAll() throws Exception{
        List<Object> profesores = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nombres, p.apellidos, p.email, pr.tipoContrato 
            FROM persona p
            JOIN profesor pr ON p.id = pr.persona_id
        """;

        try (Connection cn = database.getConnection();
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
            throw e;
        }
        return profesores;
    }

    @Override
    public boolean alreadyExist(Object obj) {
        String sql = "SELECT COUNT(*) FROM profesor WHERE persona_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, (Double) obj);
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
