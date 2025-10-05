/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controllerFXML;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.services.DB_Services;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author gon
 */
public class InicioController extends SceneManager implements Initializable {

    public Label totalEstudiantes;
    public Label totalProfesores;
    public Label totalCursos;
    public Label totalFacultades;
    public Label totalProgramas;
    public Label totalInscripciones;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setBotonActivo(btnInicio);
            initVars();
            getQuantities();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void getQuantities() throws Exception {
        totalCursos.setText(String.valueOf(DB_Services.countCursos()));
        totalFacultades.setText(String.valueOf(DB_Services.countFacultades()));
        totalProgramas.setText(String.valueOf(DB_Services.countProgramas()));
        totalProfesores.setText(String.valueOf(DB_Services.countProfesores()));
        totalEstudiantes.setText(String.valueOf(DB_Services.countEstudiantes()));
        totalInscripciones.setText(String.valueOf(DB_Services.countInscripciones()));
    }
}
