package com.example.persistence;

import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.InscripcionDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO implements Persistence {
    private final DataBase database;
    private static InscripcionDAO instance;

    private InscripcionDAO(){
        database = InternalFactory.createDB();
    }

    public static InscripcionDAO getInstance(){
        if (instance == null) instance = new InscripcionDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws Exception {
        String sqlInscripcion = "INSERT INTO inscripcion (estudiante_id, curso_id, anio, semestre) VALUES (?, ?, ?, ?)";
        InscripcionDTO inscripcion = (InscripcionDTO) dataTransfer;
        if (alreadyExist(inscripcion)){
            return;
        }
        try (Connection cn = database.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            PreparedStatement psInscripcion = cn.prepareStatement(sqlInscripcion);

            psInscripcion.setDouble(1, inscripcion.getIdEstudiante());
            psInscripcion.setInt(2, inscripcion.getIdCurso());
            psInscripcion.setInt(3, inscripcion.getAnio());
            psInscripcion.setInt(4, inscripcion.getSemestre());
            psInscripcion.executeUpdate();

            cn.commit();
        }
    }

    @Override
    public List<DataTransfer> getAll() throws Exception {
        String sql = """
                SELECT i.estudiante_id, p.nombres, p.apellidos, i.curso_id, c.nombre, i.anio, i.semestre
                FROM inscripcion AS i
                JOIN persona AS p ON i.estudiante_id = p.id
                JOIN curso AS c ON i.curso_id = c.id
                """;
        List<DataTransfer> inscripciones = new ArrayList<>();

        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double estudiante_id = rs.getDouble("estudiante_id");
                String estudiante_nombres = rs.getString("nombres");
                String estudiante_apellidos = rs.getString("apellidos");
                int curso_id = rs.getInt("curso_id");
                String curso_nombre = rs.getString("nombre");
                int anio = rs.getInt("anio");
                int semestre = rs.getInt("semestre");

                inscripciones.add(
                        new InscripcionDTO(
                                estudiante_id,
                                estudiante_nombres + " " + estudiante_apellidos,
                                curso_id,
                                curso_nombre,
                                anio,
                                semestre
                        )
                );
            }
            return inscripciones;
        }

    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) {
        String sql = """
                SELECT COUNT(*)
                FROM inscripcion
                WHERE estudiante_id = ? AND curso_id = ? AND anio = ? AND semestre = ?
                """;
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            InscripcionDTO inscripcion = (InscripcionDTO) dataTransfer;

            stmt.setDouble(1, inscripcion.getIdEstudiante());
            stmt.setDouble(2, inscripcion.getIdCurso());
            stmt.setDouble(3, inscripcion.getAnio());
            stmt.setDouble(4, inscripcion.getSemestre());
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
                    FROM inscripcion
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
