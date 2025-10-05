package com.example.controllerFXML;

import com.example.dataTransfer.ProgramaDTO;
import com.example.controller.ProgramaController;
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
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

public class ShowProgramaController extends SceneManager implements Initializable {

    @FXML private TableView<ProgramaDTO> tablaProgramas;
    @FXML private TableColumn<ProgramaDTO, Double> colID;
    @FXML private TableColumn<ProgramaDTO, String> colNombre;
    @FXML private TableColumn<ProgramaDTO, Double> colDuracion;
    @FXML private TableColumn<ProgramaDTO, Date> colRegistro;
    @FXML private TableColumn<ProgramaDTO, String> colFacultad;

    @FXML private Button btnAgregar;

    private final ProgramaController programaController = ProgramaController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBotonActivo(btnProgramas);
        configurarColumnas();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("registro"));
        colFacultad.setCellValueFactory(new PropertyValueFactory<>("nombreFacultad"));

        tablaProgramas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarPrograma());

        // Listener para habilitar/deshabilitar botones según selección
        tablaProgramas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean haySeleccion = newSel != null;
        });

    }

    private void mostrarVentanaAgregarPrograma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPrograma.fxml"));
            Parent root = loader.load();

            // Asumo que tienes un controlador para agregar facultad
            AddProgramaController controller = loader.getController();
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
            List<ProgramaDTO> listaProgramas = programaController.get();
            ObservableList<ProgramaDTO> observableList = FXCollections.observableList(listaProgramas);
            tablaProgramas.setItems(observableList);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos: " + e.getMessage());
            e.printStackTrace();
            // En caso de error, mostrar lista vacía
            tablaProgramas.setItems(FXCollections.observableArrayList());
        }
    }



}