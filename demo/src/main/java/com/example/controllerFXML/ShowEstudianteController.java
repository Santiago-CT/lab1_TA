package com.example.controllerFXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de Vista para la gestión de estudiantes
 * Se encarga únicamente de la interfaz gráfica y la interacción con el usuario
 */
public class ShowEstudianteController extends SceneManager implements Initializable {

    // Componentes FXML
    @FXML private TableView<Object> tablaEstudiantes;
    @FXML private TableColumn<Object, Double> colCodigo;
    @FXML private TableColumn<Object, String> colNombre;
    @FXML private TableColumn<Object, String> colEmail;
    @FXML private TableColumn<Object, String> colPrograma;
    @FXML private TableColumn<Object, String> colEstado;
    @FXML private Button btnAgregarEstudiante;

    private com.example.controller.EstudianteController businessController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar el controlador de lógica de negocio
        businessController = new com.example.controller.EstudianteController();

        configurarColumnasTabla();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    /**
     * Configura las columnas de la tabla
     */
    private void configurarColumnasTabla() {
        tablaEstudiantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configurar las columnas usando el BusinessController para obtener los datos
        colCodigo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(businessController.obtenerCodigoEstudiante(cellData.getValue())).asObject());

        colNombre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(businessController.obtenerNombreCompleto(cellData.getValue())));

        colEmail.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(businessController.obtenerEmail(cellData.getValue())));

        colPrograma.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(businessController.obtenerNombrePrograma(cellData.getValue())));

        colEstado.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(businessController.obtenerEstado(cellData.getValue())));


    }

    /**
     * Configura los eventos de los botones
     */
    private void configurarEventosBotones() {
        btnAgregarEstudiante.setOnAction(event -> mostrarVentanaAgregarEstudiante());
        configurarEstadoBotones();
    }

    /**
     * Configura el estado inicial de los botones y sus listeners
     */
    private void configurarEstadoBotones() {
        // Habilitar botones cuando se seleccione una fila
        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
        });
    }

    /**
     * Muestra la ventana modal para agregar un nuevo estudiante
     */
    private void mostrarVentanaAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addEstudiante.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AddEstudianteController controller = loader.getController();
            controller.setParentController(this);

            // Crear y configurar la ventana modal
            Stage modalStage = crearVentanaModal("Agregar Nuevo Estudiante", root, btnAgregarEstudiante);
            modalStage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar estudiante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos de la tabla desde el modelo
     */
    public void actualizarTabla() {
        try {
            ObservableList<Object> lista = businessController.obtenerListaEstudiantes();
            tablaEstudiantes.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de estudiantes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crea una ventana modal estándar
     */
    private Stage crearVentanaModal(String titulo, Parent root, Button parentButton) {
        Stage modalStage = new Stage();
        modalStage.setTitle(titulo);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initOwner(parentButton.getScene().getWindow());
        modalStage.setResizable(false);

        Scene scene = new Scene(root);
        modalStage.setScene(scene);

        return modalStage;
    }

}