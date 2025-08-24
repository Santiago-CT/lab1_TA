/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gon
 */
public class CursosInscritos implements Servicios {

    private ArrayList<Inscripcion> listado;

    @Override
    public String imprimirPosicion(int posicion) {
        if (posicion < 0 || posicion >= listado.size()) {
            return "Posición inválida";
        }
        return listado.get(posicion).toString();
    }

    @Override
    public int candidadActual() {
        return listado.size();
    }

    @Override
    public List<String> imprimirListado() {
        List<String> result = new ArrayList<>();
        for (Inscripcion inscripcion : listado) {
            result.add(inscripcion.toString());
        }
        return result;
    }

    public CursosInscritos() {
        this.listado = new ArrayList<>();
    }

    public void inscribirCurso(Inscripcion inscripcion) {
        listado.add(inscripcion);
    }

    public void eliminar(Inscripcion inscripcion) {
        listado.remove(inscripcion);
    }

    public void actualizar(Inscripcion inscripcion) {
        int index = listado.indexOf(inscripcion);
        if (index != -1) {
            listado.set(index, inscripcion);
        }
    }

    public void guardarinformacion() throws Exception {
        for (Inscripcion inscripcion : listado) {
            try (Connection cn = ConexionBD.getConnection()) {
                cn.setAutoCommit(false); // para asegurar transacción

                String sqlPersona = "INSERT INTO persona (id, nombres, apellidos, email) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psPersona = cn.prepareStatement(sqlPersona)) {
                    psPersona.setDouble(1, 1);
                    psPersona.setString(2, "Juan");
                    psPersona.setString(3, "Pérez");
                    psPersona.setString(4, "juan@mail.com");
                    psPersona.executeUpdate();
                }

                String sqlEstudiante = "INSERT INTO estudiante (persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement psEstudiante = cn.prepareStatement(sqlEstudiante)) {
                    psEstudiante.setDouble(1, 1); // mismo ID
                    psEstudiante.setDouble(2, 12345);
                    psEstudiante.setDouble(3, 10);
                    psEstudiante.setBoolean(4, true);
                    psEstudiante.setDouble(5, 4.5);
                    psEstudiante.executeUpdate();
                }

                cn.commit();
            }
        }
    }

    public void cargarDatos() {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Inscripcion inscripcion : listado) {
            sb.append(inscripcion.toString()).append("\n");
        }
        return sb.toString();
    }
}
