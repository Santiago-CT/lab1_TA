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
import javafx.scene.control.Button;
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

    @FXML private TableColumn<Estudiante, String> colCodigo;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colEmail;

    @FXML
    private TableColumn<Estudiante, String> colPrograma;

    @FXML
    private TableColumn<Estudiante, Integer> colSemestre;

    @FXML
    private TableColumn<Estudiante, String> colEstado;

    @FXML
    private TableColumn<Estudiante, String> colFechaIngreso;

    @FXML
    private Button btnAgregarEstudiante;

    @FXML
    private Button btnEditarEstudiante;

    @FXML
    private Button btnEliminarEstudiante;

    @FXML
    private Button btnVerDetallesEstudiante;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Código del estudiante
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        // Nombre completo (nombres + apellidos) → usar lambda porque no existe como propiedad
        colNombre.setCellValueFactory(cellData -> {
            Estudiante est = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(est.getNombres() + " " + est.getApellidos());
        });

        // Email
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Programa → sacar el nombre del objeto Programa
        colPrograma.setCellValueFactory(cellData -> {
            Estudiante est = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    est.getPrograma() != null ? est.getPrograma().getNombre() : ""
            );
        });

        // Estado (activo/inactivo)
        colEstado.setCellValueFactory(cellData -> {
            Estudiante est = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(est.isActivo() ? "Activo" : "Inactivo");
        });

        // ⚠️ colSemestre y colFechaIngreso → aún no existen en Estudiante
        // Puedes quitarlas o después agregamos esos atributos en la clase Estudiante

        // Cargar datos desde la BD
        ObservableList<Estudiante> lista = null;
        try {
            lista = FXCollections.observableArrayList(ConexionBD.getEstudiantes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tablaEstudiantes.setItems(lista);
        tablaEstudiantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        initVars();
    }}

