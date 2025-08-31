/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.model.ConexionBD;
import com.example.model.Estudiante;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author gon
 */
public class EstudianteController extends SceneManager implements Initializable{
    @FXML
    private TableView<Estudiante> tablaEstudiantes;
    @FXML
    private TableColumn<Estudiante, String> colId;
    @FXML
    private TableColumn<Estudiante, String> colNombres;
    @FXML
    private TableColumn<Estudiante, String> colApellidos;
    @FXML
    private TableColumn<Estudiante, String> colEmail;
    @FXML
    private TableColumn<Estudiante, String> colTipoContrato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipoContrato.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));

        // Cargar datos desde la función
        ObservableList<Estudiante> lista = FXCollections.observableArrayList(ConexionBD.getEstudiantes());
        tablaEstudiantes.setItems(lista);
        tablaEstudiantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        initVars();
    }
}
