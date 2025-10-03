package com.example.persistence;

import com.example.dataTransfer.CursoDTO;
import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.ProfesorDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CursoDAO implements Persistence {

    private final DataBase database;
    private static CursoDAO instance;

    private CursoDAO(){
        database = InternalFactory.createDB();
    }

    public static CursoDAO getInstance(){
        if (instance == null) instance = new CursoDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws Exception {
        String SQL_CURSO = "INSERT INTO curso (id, nombre, programa_id, activo) VALUES (?, ?, ?, ?)";
        CursoDTO curso = (CursoDTO) dataTransfer;
        if (alreadyExist(curso)){
            return;
        }
        try (Connection cn = database.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            PreparedStatement psCurso = cn.prepareStatement(SQL_CURSO);

            psCurso.setInt(1, curso.getID());
            psCurso.setString(2, curso.getNombre());
            psCurso.setDouble(3, curso.getIdPrograma());
            psCurso.setBoolean(4, curso.isActivo());
            psCurso.executeUpdate();

            cn.commit();
        }
    }

    @Override
    public List<DataTransfer> getAll() throws Exception {
        String sql = """
                SELECT c.id, c.nombre, c.programa_id, c.activo, p.nombre AS programa_nombre
                FROM curso AS c
                JOIN programa AS p ON p.id = c.programa_id
                """;
        List<DataTransfer> cursos = new ArrayList<>();
        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double programa_id = rs.getDouble("programa_id");
                String programa_nombre = rs.getString("programa_nombre");
                boolean activo = rs.getBoolean("activo");

                cursos.add(new CursoDTO(id, nombre, programa_id, programa_nombre, activo));
            }
        }
        return cursos;
    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) throws Exception{
        String sql = """
                    SELECT COUNT(*)
                    FROM curso AS c
                    WHERE c.id = ?
                    """;
        try (Connection cn = database.getConnection();
             PreparedStatement stmt = cn.prepareStatement(sql);
        ){
            CursoDTO curso = (CursoDTO) dataTransfer;
            stmt.setInt(1, curso.getID());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int count() throws Exception {
        String sql = """
                    SELECT COUNT(*)
                    FROM curso
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
