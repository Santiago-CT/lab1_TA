package com.example.controllerFXML;

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
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowProfesorController extends SceneManager implements Initializable {

    @FXML
    private TableView<Object> tablaProfesores; // Cambiado a Object para usar con ProfesorController
    @FXML
    private TableColumn<Object, String> colId;
    @FXML
    private TableColumn<Object, String> colNombres;
    @FXML
    private TableColumn<Object, String> colApellidos;
    @FXML
    private TableColumn<Object, String> colEmail;
    @FXML
    private TableColumn<Object, String> colTipoContrato;
    @FXML
    private Button btnAgregarProfesor;

    private ProfesorController profesorController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profesorController = new ProfesorController();

        // Configurar columnas usando cell factories personalizadas
        configurarColumnas();

        // Cargar datos usando ProfesorController
        cargarDatos();

        tablaProfesores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        // Configurar columnas para trabajar con Object usando ProfesorController
        colId.setCellValueFactory(cellData -> {
            Object profesor = cellData.getValue();
            double id = profesorController.getId(profesor);
            return new javafx.beans.property.SimpleStringProperty(String.valueOf((int)id));
        });

        colNombres.setCellValueFactory(cellData -> {
            Object profesor = cellData.getValue();
            String nombres = profesorController.getNombres(profesor);
            return new javafx.beans.property.SimpleStringProperty(nombres);
        });

        colApellidos.setCellValueFactory(cellData -> {
            Object profesor = cellData.getValue();
            String apellidos = profesorController.getApellidos(profesor);
            return new javafx.beans.property.SimpleStringProperty(apellidos);
        });

        colEmail.setCellValueFactory(cellData -> {
            Object profesor = cellData.getValue();
            String email = profesorController.getEmail(profesor);
            return new javafx.beans.property.SimpleStringProperty(email);
        });

        colTipoContrato.setCellValueFactory(cellData -> {
            Object profesor = cellData.getValue();
            String tipoContrato = profesorController.getTipoContrato(profesor);
            return new javafx.beans.property.SimpleStringProperty(tipoContrato);
        });
    }


    private void cargarDatos() {
        try {
            ObservableList<Object> lista = profesorController.obtenerListaProfesores();
            tablaProfesores.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de profesores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarEventosBotones() {
        btnAgregarProfesor.setOnAction(event -> mostrarVentanaAgregarProfesor());

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
            modalStage.initOwner(btnAgregarProfesor.getScene().getWindow());
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

    public void actualizarTabla() {
        cargarDatos();
    }

    public Object getProfesorSeleccionado() {
        return tablaProfesores.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void mostrarTodosProfesores() {
        cargarDatos();
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