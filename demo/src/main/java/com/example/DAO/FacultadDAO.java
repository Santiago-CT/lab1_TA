package com.example.DAO;
import com.example.DTO.FacultadDTO;
import com.example.database.DataBase;
import com.example.factory.InternalFactory;
import com.example.model.Facultad;
import com.example.model.Persona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class FacultadDAO implements DAO{

    private final DataBase database;

    public FacultadDAO(){
        database = InternalFactory.createDB();
    }

    @Override
    public void insert(Object obj) throws Exception {
        String SQL_INSERT = "INSERT INTO facultad (id, nombre, decano) VALUES (?, ?, ?)";

        try (Connection cn = database.getConnection()) {
            FacultadDTO facultad = (FacultadDTO) obj;
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
    public List<Object> getAll() throws Exception{
        String SQL_SELECT_ALL = """
            SELECT f.id, f.nombre, f.decano, 
                   p.nombres, p.apellidos
            FROM facultad f 
            LEFT JOIN persona p ON f.decano = p.id
            """;
        List<Object> facultades = new ArrayList<>();

        try (Connection cn = database.getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double id = rs.getDouble("id");
                String nombre = rs.getString("nombre");

                Persona decano = null;
                if (rs.getObject("decano") != null) {
                    double decanoId = rs.getDouble("decano");
                    String nombres = rs.getString("nombres");
                    String apellidos = rs.getString("apellidos");

                    if (nombres != null && apellidos != null) {
                        decano = new Persona(decanoId, nombres, apellidos, null);
                    }
                }

                Facultad facultad = new Facultad(id, nombre, decano);
                facultades.add(facultad);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return facultades;
    }

    @Override
    public boolean alreadyExist(Object obj) {
        String sql = "SELECT COUNT(*) FROM facultad WHERE id = ?";
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
