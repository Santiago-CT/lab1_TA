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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador de Vista para la gestión de estudiantes
 * Se encarga únicamente de la interfaz gráfica y la interacción con el usuario
 */
public class EstudianteControllerV extends SceneManager implements Initializable {

    // Componentes FXML
    @FXML private TableView<Object> tablaEstudiantes; // Usa Object en lugar de Estudiante
    @FXML private TableColumn<Object, Double> colCodigo;
    @FXML private TableColumn<Object, String> colNombre;
    @FXML private TableColumn<Object, String> colEmail;
    @FXML private TableColumn<Object, String> colPrograma;
    @FXML private TableColumn<Object, String> colEstado;
    @FXML private TableColumn<Object, String> colFechaIngreso;
    @FXML private Button btnAgregarEstudiante;
   // @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVerDetalles;

    private com.example.controller.EstudianteController businessController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializar el controlador de lógica de negocio
        businessController = new com.example.controller.EstudianteController();

        configurarColumnasTabla();
        actualizarTablaEstudiantes();
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

        colFechaIngreso.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(businessController.obtenerFechaIngreso(cellData.getValue())));
    }

    /**
     * Configura los eventos de los botones
     */
    private void configurarEventosBotones() {
        btnAgregarEstudiante.setOnAction(event -> mostrarVentanaAgregarEstudiante());
        //btnEditar.setOnAction(event -> editarEstudiante());
        //btnEliminar.setOnAction(event -> eliminarEstudiante());
        if (btnVerDetalles != null) {
            btnVerDetalles.setOnAction(event -> verDetallesEstudiante());
        }

        configurarEstadoBotones();
    }

    /**
     * Configura el estado inicial de los botones y sus listeners
     */
    private void configurarEstadoBotones() {
        // Deshabilitar botones que requieren selección
        //btnEditar.setDisable(true);
        //btnEliminar.setDisable(true);
        if (btnVerDetalles != null) {
            btnVerDetalles.setDisable(true);
        }

        // Habilitar botones cuando se seleccione una fila
        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
           // btnEditar.setDisable(!haySeleccion);
            //btnEliminar.setDisable(!haySeleccion);
            if (btnVerDetalles != null) {
                btnVerDetalles.setDisable(!haySeleccion);
            }
        });
    }

    /**
     * Muestra la ventana modal para agregar un nuevo estudiante
     */
    private void mostrarVentanaAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregarEstudiante.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AgregarEstudianteController controller = loader.getController();
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
     * Muestra la ventana modal para editar un estudiante
     */
    @FXML
    private void editarEstudiante() {
        Object estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editarEstudiante.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y configurarlo
                EditarEstudianteController controller = loader.getController();
                controller.setParentController(this);
                // Pasar el objeto estudiante a través del business controller
               // controller.setEstudianteData(estudianteSeleccionado);

                // Crear y configurar la ventana modal
                String titulo = "Editar Estudiante - " + businessController.obtenerNombreCompleto(estudianteSeleccionado);
               // Stage modalStage = crearVentanaModal(titulo, root, btnEditar);
               // modalStage.showAndWait();

            } catch (IOException e) {
                mostrarError("Error", "No se pudo abrir la ventana de edición: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina un estudiante con confirmación del usuario
     */
    @FXML
    private void eliminarEstudiante() {
        Object estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            if (mostrarConfirmacionEliminacion(estudianteSeleccionado)) {
                try {
                    double estudianteId = businessController.obtenerIdEstudiante(estudianteSeleccionado);
                    if (businessController.eliminarEstudiante(estudianteId)) {
                        actualizarTablaEstudiantes();
                    } else {
                        mostrarError("Error", "No se pudo eliminar el estudiante. Puede que no exista en la base de datos.");
                    }
                } catch (Exception e) {
                    mostrarError("Error de Base de Datos",
                            "Error al eliminar el estudiante: " + e.getMessage() +
                                    "\n\nPosibles causas:\n" +
                                    "• Problemas de conectividad con la base de datos\n" +
                                    "• Referencias en otras tablas que impiden la eliminación");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Muestra los detalles completos de un estudiante
     */
    private void verDetallesEstudiante() {
        Object estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            String detalles = businessController.generarDetallesEstudiante(estudianteSeleccionado);
            mostrarDetalles("Detalles del Estudiante", "Información Completa", detalles);
        }
    }

    /**
     * Actualiza los datos de la tabla desde el modelo
     */
    public void actualizarTablaEstudiantes() {
        try {
            ObservableList<Object> lista = businessController.obtenerListaEstudiantes();
            tablaEstudiantes.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de estudiantes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Muestra un diálogo de confirmación para eliminar estudiante
     */
    private boolean mostrarConfirmacionEliminacion(Object estudiante) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar Estudiante?");

        String mensaje = businessController.generarMensajeConfirmacionEliminacion(estudiante);
        confirmacion.setContentText(mensaje);

        // Personalizar botones
        ButtonType btnEliminarBtn = new ButtonType("Eliminar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacion.getButtonTypes().setAll(btnEliminarBtn, btnCancelar);

        // Configurar el botón por defecto como Cancelar para mayor seguridad
        Button defaultButton = (Button) confirmacion.getDialogPane().lookupButton(btnCancelar);
        defaultButton.setDefaultButton(true);

        Button deleteButton = (Button) confirmacion.getDialogPane().lookupButton(btnEliminarBtn);
        deleteButton.setDefaultButton(false);

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        return resultado.isPresent() && resultado.get() == btnEliminarBtn;
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

    /**
     * Muestra un diálogo de error
     */
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo informativo
     */
    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo con detalles
     */
    private void mostrarDetalles(String titulo, String header, String contenido) {
        Alert detallesAlert = new Alert(Alert.AlertType.INFORMATION);
        detallesAlert.setTitle(titulo);
        detallesAlert.setHeaderText(header);
        detallesAlert.setContentText(contenido);
        detallesAlert.setResizable(true);
        detallesAlert.getDialogPane().setPrefWidth(500);
        detallesAlert.showAndWait();
    }

    // Métodos públicos para interacción con otros controladores
    public Object getEstudianteSeleccionado() {
        return tablaEstudiantes.getSelectionModel().getSelectedItem();
    }

    /**
     * Selecciona un estudiante en la tabla por su código
     */
    public void seleccionarEstudiante(double codigo) {
        for (Object estudiante : tablaEstudiantes.getItems()) {
            if (businessController.obtenerCodigoEstudiante(estudiante) == codigo) {
                tablaEstudiantes.getSelectionModel().select(estudiante);
                tablaEstudiantes.scrollTo(estudiante);
                break;
            }
        }
    }
}