package com.example.controllerFXML;

import com.example.DTO.FacultadDTO;
import com.example.controller.FacultadController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowFacultadController extends SceneManager implements Initializable {

    @FXML private TableView<FacultadDTO> tablaFacultades;
    @FXML private TableColumn<FacultadDTO, Double> colID;
    @FXML private TableColumn<FacultadDTO, String> colNombre;
    @FXML private TableColumn<FacultadDTO, String> colDecano;

    private FacultadController facultadController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facultadController = new FacultadController();
        configurarColumnas();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDecano.setCellValueFactory(new PropertyValueFactory<>("nombreDecano"));

        tablaFacultades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarFacultad());

    }

    private void mostrarVentanaAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addFacultad.fxml"));
            Parent root = loader.load();

            AddFacultadController controller = loader.getController();
            controller.setParentController(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnAgregar.getScene().getWindow());
            modalStage.setResizable(false);

            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar facultad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarTabla() {
        try {
            // Obtener todas las facultades de la base de datos
            List<FacultadDTO> listaFacultades = facultadController.getAll();
            ObservableList<FacultadDTO> observableList = FXCollections.observableArrayList(listaFacultades);
            tablaFacultades.setItems(observableList);

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de facultades: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaFacultades.setItems(FXCollections.observableArrayList());
        }
    }



}