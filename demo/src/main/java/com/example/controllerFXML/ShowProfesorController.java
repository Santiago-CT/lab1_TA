package com.example.controllerFXML;

import com.example.dataTransfer.ProfesorDTO;
import com.example.controller.ProfesorController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowProfesorController extends SceneManager implements Initializable {

    @FXML
    private TableView<ProfesorDTO> tablaProfesores;
    @FXML
    private TableColumn<ProfesorDTO, Double> colId;
    @FXML
    private TableColumn<ProfesorDTO, String> colNombres;
    @FXML
    private TableColumn<ProfesorDTO, String> colApellidos;
    @FXML
    private TableColumn<ProfesorDTO, String> colEmail;
    @FXML
    private TableColumn<ProfesorDTO, String> colTipoContrato;
    @FXML
    private Button btnAgregar;

    private ProfesorController profesorController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profesorController = ProfesorController.getInstance();
        setBotonActivo(btnProfesores);
        configurarColumnas();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipoContrato.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));

        tablaProfesores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    public void actualizarTabla() {
        try {
            List<ProfesorDTO> lista = profesorController.getAll();
            ObservableList<ProfesorDTO> observableList = FXCollections.observableArrayList(lista);
            tablaProfesores.setItems(observableList);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de profesores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarProfesor());

        // Habilitar botones cuando se seleccione una fila
        tablaProfesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            // Aquí puedes habilitar/deshabilitar botones según la selección
        });
    }

    private void mostrarVentanaAgregarProfesor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addProfesor.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AddProfesorController controller = loader.getController();
            controller.setParentController(this);

            // Crear y configurar la ventana modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar Nuevo Profesor");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnAgregar.getScene().getWindow());
            modalStage.setResizable(false);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // Mostrar la ventana modal
            modalStage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar profesor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Métodos de utilidad para mostrar mensajes
    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}