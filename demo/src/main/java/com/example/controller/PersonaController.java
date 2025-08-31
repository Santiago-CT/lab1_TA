package com.example.controller;

import com.example.model.ConexionBD;
import com.example.model.Profesor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class PersonaController extends SceneManager implements Initializable {
    
    @FXML
    private TableView<Profesor> tablaProfesores;
    @FXML
    private TableColumn<Profesor, String> colId;
    @FXML
    private TableColumn<Profesor, String> colNombres;
    @FXML
    private TableColumn<Profesor, String> colApellidos;
    @FXML
    private TableColumn<Profesor, String> colEmail;
    @FXML
    private TableColumn<Profesor, String> colTipoContrato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipoContrato.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));

        // Cargar datos desde la función
        ObservableList<Profesor> lista = FXCollections.observableArrayList(ConexionBD.getProfesores());
        tablaProfesores.setItems(lista);
        tablaProfesores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        initVars();
    }

}
