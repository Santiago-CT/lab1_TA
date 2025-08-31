package com.example.controller;

import com.example.model.ConexionBD;
import com.example.model.Profesor;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    @FXML private Button btnAgregarProfesor;
    @FXML private Button btnEditarProfesor;
    @FXML private Button btnEliminarProfesor;
    @FXML private Button btnVerDetallesProfesor;

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



        configurarEventosBotones();
        initVars();
    }
    private void configurarEventosBotones() {
        btnAgregarProfesor.setOnAction(event -> mostrarVentanaAgregarProfesor());

        // Deshabilitar botones que requieren selección
        btnEditarProfesor.setDisable(true);
        btnEliminarProfesor.setDisable(true);
        btnVerDetallesProfesor.setDisable(true);

        // Habilitar botones cuando se seleccione una fila
        tablaProfesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            btnEditarProfesor.setDisable(!haySeleccion);
            btnEliminarProfesor.setDisable(!haySeleccion);
            btnVerDetallesProfesor.setDisable(!haySeleccion);
        });
    }
    private void mostrarVentanaAgregarProfesor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregarProfesor.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AgregarProfesorController controller = loader.getController();
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
    public void actualizarTablaProfesores() {
        try {
            ObservableList<Profesor> lista = FXCollections.observableArrayList(ConexionBD.getProfesores());
            tablaProfesores.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de profesores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void editarProfesor() {
        Profesor profesorSeleccionado = tablaProfesores.getSelectionModel().getSelectedItem();
        if (profesorSeleccionado != null) {
            // TODO: Implementar ventana de edición
            mostrarInformacion("Información", "Funcionalidad de editar en desarrollo");
        }
    }
    @FXML
    private void eliminarProfesor() {
        Profesor profesorSeleccionado = tablaProfesores.getSelectionModel().getSelectedItem();
        if (profesorSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Eliminar Profesor?");
            confirmacion.setContentText("¿Está seguro que desea eliminar al profesor " +
                    profesorSeleccionado.getNombres() + " " +
                    profesorSeleccionado.getApellidos() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                // TODO: Implementar eliminación lógica en la base de datos
                mostrarInformacion("Información", "Funcionalidad de eliminar en desarrollo");
            }
        }
    }
    @FXML
    private void verDetallesProfesor() {
        Profesor profesorSeleccionado = tablaProfesores.getSelectionModel().getSelectedItem();
        if (profesorSeleccionado != null) {
            String detalles = String.format("""
                Detalles del Profesor:
                
                ID: %.0f
                Nombres: %s
                Apellidos: %s
                Email: %s
                Tipo de Contrato: %s
                """,
                    profesorSeleccionado.getID(),
                    profesorSeleccionado.getNombres(),
                    profesorSeleccionado.getApellidos(),
                    profesorSeleccionado.getEmail(),
                    profesorSeleccionado.getTipoContrato()
            );

            Alert detallesAlert = new Alert(Alert.AlertType.INFORMATION);
            detallesAlert.setTitle("Detalles del Profesor");
            detallesAlert.setHeaderText("Información Completa");
            detallesAlert.setContentText(detalles);
            detallesAlert.showAndWait();
        }
    }
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public Profesor getProfesorSeleccionado() {
        return tablaProfesores.getSelectionModel().getSelectedItem();
    }

    /**
     * Selecciona un profesor en la tabla por su ID
     */
    public void seleccionarProfesor(double id) {
        for (Profesor profesor : tablaProfesores.getItems()) {
            if (profesor.getID() == id) {
                tablaProfesores.getSelectionModel().select(profesor);
                tablaProfesores.scrollTo(profesor);
                break;
            }
        }
    }
}

