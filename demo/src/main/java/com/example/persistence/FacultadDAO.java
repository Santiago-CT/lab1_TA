package com.example.persistence;
import com.example.dataTransfer.DataTransfer;
import com.example.dataTransfer.EstudianteDTO;
import com.example.dataTransfer.FacultadDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.Facultad;
import com.example.model.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class FacultadDAO implements Persistence {

    private final DataBase database;
    private static FacultadDAO instance;

    private FacultadDAO(){
        database = InternalFactory.createDB();
    }

    public static FacultadDAO getInstance(){
        if (instance == null) instance = new FacultadDAO();
        return instance;
    }

    @Override
    public void insert(DataTransfer dataTransfer) throws Exception {
        String SQL_INSERT = "INSERT INTO facultad (id, nombre, decano) VALUES (?, ?, ?)";
        FacultadDTO facultad = (FacultadDTO) dataTransfer;
        if (alreadyExist(facultad)){
            return;
        }
        try (Connection cn = database.getConnection()) {
            cn.setAutoCommit(false); // para asegurar transacción

            PreparedStatement psFacultad = cn.prepareStatement(SQL_INSERT);

            psFacultad.setDouble(1, facultad.getID());
            psFacultad.setString(2, facultad.getNombre());
            psFacultad.setDouble(3, facultad.getIdDecano());
            psFacultad.executeUpdate();

            cn.commit();
        }
    }

    @Override
    public List<DataTransfer> getAll() throws Exception{
        String SQL_SELECT_ALL = """
            SELECT f.id, f.nombre, f.decano, 
                   p.nombres, p.apellidos
            FROM facultad f 
            LEFT JOIN persona p ON f.decano = p.id
            """;
        List<DataTransfer> facultades = new ArrayList<>();

        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");
                double decanoId = rs.getDouble("decano");
                String decanoNombres = rs.getString("nombres");
                String decanoApellidos = rs.getString("apellidos");

                facultades.add(
                        new FacultadDTO(
                                id,
                                nombre,
                                decanoId,
                                decanoNombres + " " + decanoApellidos
                        )
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return facultades;
    }

    @Override
    public boolean alreadyExist(DataTransfer dataTransfer) {
        String sql = "SELECT COUNT(*) FROM facultad WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            FacultadDTO facultad = (FacultadDTO) dataTransfer;
            stmt.setDouble(1, facultad.getID());
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
                    FROM facultad
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
