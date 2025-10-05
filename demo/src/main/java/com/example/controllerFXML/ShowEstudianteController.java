package com.example.controllerFXML;
import com.example.dataTransfer.EstudianteDTO;
import com.example.controller.EstudianteController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de Vista para la gestión de estudiantes
 * Se encarga únicamente de la interfaz gráfica y la interacción con el usuario
 */
public class ShowEstudianteController extends SceneManager implements Initializable {

    // Componentes FXML
    @FXML private TableView<EstudianteDTO> tablaEstudiantes;
    @FXML private TableColumn<EstudianteDTO, Double> colCodigo;
    @FXML private TableColumn<EstudianteDTO, String> colNombre;
    @FXML private TableColumn<EstudianteDTO, String> colEmail;
    @FXML private TableColumn<EstudianteDTO, String> colPrograma;
    @FXML private TableColumn<EstudianteDTO, String> colEstado;
    @FXML private TableColumn<EstudianteDTO, Double> colPromedio;
    @FXML private Button btnAgregarEstudiante;

    private final EstudianteController estudianteController = EstudianteController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBotonActivo(btnEstudiantes);
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

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPrograma.setCellValueFactory(new PropertyValueFactory<>("nombrePrograma"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("activo"));
        colPromedio.setCellValueFactory(new PropertyValueFactory<>("promedio"));
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
            List<EstudianteDTO> lista = estudianteController.getAll();
            ObservableList<EstudianteDTO> observableList = FXCollections.observableList(lista);
            tablaEstudiantes.setItems(observableList);
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