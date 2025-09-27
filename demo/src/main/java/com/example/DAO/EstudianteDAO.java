package com.example.DAO;

import com.example.DTO.EstudianteDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements DAO{

    private final DataBase database;

    public EstudianteDAO(){
        database = InternalFactory.createDB();
    }

    @Override
    public void insert(Object obj) throws Exception {
        String SQL_PERSONA = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
        String SQL_ESTUDIANTE = "INSERT INTO estudiante (persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";

        try (Connection cn = database.getConnection()) {
            EstudianteDTO estudiante = (EstudianteDTO) obj;
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
    public List<Object> getAll() throws Exception {
        String sql = """
                SELECT p.id, p.nombres, p.apellidos, p.email,
                       e.codigo, e.activo, e.promedio,
                       prog.id AS programa_id, prog.nombre AS programa_nombre,
                       prog.duracion, prog.registro,
                       f.id AS facultad_id, f.nombre AS facultad_nombre
                FROM persona p
                JOIN estudiante e ON p.id = e.persona_id
                JOIN programa prog ON e.programa_id = prog.id
                JOIN facultad f ON prog.facultad_id = f.id
                """;

        List<Object> estudiantes = new ArrayList<>();
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

                // Crear Facultad
                Facultad facultad = new Facultad(
                        rs.getDouble("facultad_id"),
                        rs.getString("facultad_nombre"),
                        null
                );

                // Crear Programa
                Programa programa = new Programa(
                        rs.getDouble("programa_id"),
                        rs.getString("programa_nombre"),
                        rs.getDouble("duracion"),
                        rs.getDate("registro"),
                        facultad
                );

                estudiantes.add(
                        new Estudiante(
                                id,
                                nombres,
                                apellidos,
                                email,
                                codigo,
                                programa,
                                activo,
                                promedio
                        )
                );
            }
            return estudiantes;
        }
    }

    @Override
    public boolean alreadyExist(Object obj) {
        String sql = "SELECT COUNT(*) FROM estudiante WHERE persona_id = ?";
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
                    FROM estudiante
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
