package com.example.persistence;

import com.example.dataTransfer.CursoDTO;
import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.EstudianteDTO;
import com.example.database.DataBase;
import com.example.database.Oracle;
import com.example.factory.InternalFactory;
import com.example.model.*;
import com.example.observer.Observer;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements Persistence {

    private DataBase database;
    private static EstudianteDAO instance;

    private EstudianteDAO(){
        database = InternalFactory.createDB();
    }

    public static EstudianteDAO getInstance(){
        if (instance == null) instance = new EstudianteDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws Exception {
        String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
        String SQL_ESTUDIANTE = "INSERT INTO estudiante (persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";
        EstudianteDTO estudiante = (EstudianteDTO) dataTransfer;
        if (alreadyExist(estudiante)){
            return;
        }
        try (Connection cn = database.getConnection()) {
            cn.setAutoCommit(false);

            try (PreparedStatement psPersona = cn.prepareStatement(SQL_PERSONA);
                 PreparedStatement psEstudiante = cn.prepareStatement(SQL_ESTUDIANTE)) {

                // Insertar persona
                psPersona.setDouble(1, estudiante.getID());
                psPersona.setString(2, estudiante.getNombres());
                psPersona.setString(3, estudiante.getApellidos());
                psPersona.setString(4, estudiante.getEmail());
                psPersona.executeUpdate();

                // Insertar estudiante
                psEstudiante.setDouble(1, estudiante.getID());
                psEstudiante.setDouble(2, estudiante.getCodigo());
                psEstudiante.setDouble(3, estudiante.getIdPrograma());
                psEstudiante.setBoolean(4, estudiante.isActivo());
                psEstudiante.setDouble(5, estudiante.getPromedio());
                psEstudiante.executeUpdate();

                cn.commit();

            } catch (Exception e) {
                cn.rollback();
                throw e;
            }
        }
    }

    @Override
    public List<DataTransfer> getAll() throws Exception {
        String sql = getSQL();

        List<DataTransfer> estudiantes = new ArrayList<>();
        try (Connection cn = database.getConnection();
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

                double programa_id = rs.getDouble("programa_id");
                String programa_nombre = rs.getString("programa_nombre");

                estudiantes.add(
                        new EstudianteDTO(
                                id,
                                nombres,
                                apellidos,
                                email,
                                codigo,
                                programa_id,
                                programa_nombre,
                                activo,
                                promedio
                        )
                );
            }
            return estudiantes;
        }
    }

    private String getSQL() {
        String sql;
        if (database instanceof Oracle) {
            sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email,
                           e.codigo, e.activo, e.promedio,
                           prog.id programa_id, prog.nombre programa_nombre
                    FROM persona p
                    JOIN estudiante e ON p.id = e.persona_id
                    JOIN programa prog ON e.programa_id = prog.id
                    """;
        } else {
            sql = """
                    SELECT p.id, p.nombres, p.apellidos, p.email,
                           e.codigo, e.activo, e.promedio,
                           prog.id AS programa_id, prog.nombre AS programa_nombre
                    FROM persona p
                    JOIN estudiante e ON p.id = e.persona_id
                    JOIN programa prog ON e.programa_id = prog.id
                    """;
        }
        return sql;
    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) {
        String sql = "SELECT COUNT(*) FROM estudiante WHERE persona_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            EstudianteDTO estudiante = (EstudianteDTO) dataTransfer;
            stmt.setDouble(1, estudiante.getID());
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
                    FROM estudiante
                    """;
        try (Connection cn = database.getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()
        ){
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

}
