package com.example.controller;

import com.example.dao.EstudianteDao;

import com.example.model.Estudiante;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class EstudianteController extends SceneManager implements Initializable {

    @FXML  private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Double> colCodigo;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colEmail;
    @FXML private TableColumn<Estudiante, String> colPrograma;
    @FXML private TableColumn<Estudiante, String> colEstado;
    @FXML private TableColumn<Estudiante, String> colFechaIngreso;

    @FXML private Button btnAgregarEstudiante;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVerDetalles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Configurar columnas según tu modelo
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        // Para mostrar nombre completo (nombres + apellidos)
        colNombre.setCellValueFactory(cellData -> {
            String nombreCompleto = cellData.getValue().getNombres() + " " + cellData.getValue().getApellidos();
            return new javafx.beans.property.SimpleStringProperty(nombreCompleto);
        });

        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Para mostrar el nombre del programa
        colPrograma.setCellValueFactory(cellData -> {
            String nombrePrograma = cellData.getValue().getPrograma() != null ?
                    cellData.getValue().getPrograma().getNombre() : "Sin programa";
            return new javafx.beans.property.SimpleStringProperty(nombrePrograma);
        });
        // Para mostrar el estado basado en el boolean activo
        colEstado.setCellValueFactory(cellData -> {
            String estado = cellData.getValue().isActivo() ? "Activo" : "Inactivo";
            return new javafx.beans.property.SimpleStringProperty(estado);
        });

        // Para fecha de ingreso necesitarás agregar este campo al modelo o manejarlo diferente
        // Por ahora lo dejo vacío hasta que definas cómo manejar las fechas
        colFechaIngreso.setCellValueFactory(cellData -> {
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Cargar datos desde la función
        actualizarTablaEstudiantes();

        tablaEstudiantes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        configurarEventosBotones();
        initVars();
    }

    private void configurarEventosBotones() {
        btnAgregarEstudiante.setOnAction(event -> mostrarVentanaAgregarEstudiante());
        btnEditar.setOnAction(event -> editarEstudiante());
        btnEliminar.setOnAction(event -> eliminarEstudiante());

        // Deshabilitar botones que requieren selección
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        if (btnVerDetalles != null) {
            btnVerDetalles.setDisable(true);
        }
        // Habilitar botones cuando se seleccione una fila
        tablaEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
            btnEditar.setDisable(!haySeleccion);
            btnEliminar.setDisable(!haySeleccion);
            if (btnVerDetalles != null) {
                btnVerDetalles.setDisable(!haySeleccion);
            }
        });
    }

    private void mostrarVentanaAgregarEstudiante() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregarEstudiante.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AgregarEstudianteController controller = loader.getController();
            controller.setParentController(this);

            // Crear y configurar la ventana modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar Nuevo Estudiante");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnAgregarEstudiante.getScene().getWindow());
            modalStage.setResizable(false);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // Mostrar la ventana modal
            modalStage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar estudiante: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarTablaEstudiantes() {
        try {
            ObservableList<Estudiante> lista = FXCollections.observableArrayList(EstudianteDao.get());
            tablaEstudiantes.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de estudiantes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void editarEstudiante() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            try {
                // Cargar el FXML para la ventana de edición
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editarEstudiante.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y configurarlo
                EditarEstudianteController controller = loader.getController();
                controller.setParentController(this);
                controller.setEstudiante(estudianteSeleccionado);

                // Crear y configurar la ventana modal
                Stage modalStage = new Stage();
                modalStage.setTitle("Editar Estudiante - " + estudianteSeleccionado.getNombres() + " " +
                        estudianteSeleccionado.getApellidos());
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.initOwner(btnEditar.getScene().getWindow());
                modalStage.setResizable(false);

                Scene scene = new Scene(root);
                modalStage.setScene(scene);

                // Mostrar la ventana modal
                modalStage.showAndWait();

            } catch (IOException e) {
                mostrarError("Error", "No se pudo abrir la ventana de edición: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void eliminarEstudiante() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            // Crear confirmación personalizada con más información
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar Eliminación");
            confirmacion.setHeaderText("¿Eliminar Estudiante?");

            String mensaje = String.format("""
                ¿Está seguro que desea eliminar al estudiante?
                
                Nombre: %s %s
                Código: %.0f
                Email: %s
                Programa: %s
                
                ⚠️ ADVERTENCIA: Esta acción también eliminará:
                • Todas las inscripciones del estudiante
                • Toda la información personal asociada
                
                Esta acción NO se puede deshacer.
                """,
                    estudianteSeleccionado.getNombres(),
                    estudianteSeleccionado.getApellidos(),
                    estudianteSeleccionado.getCodigo(),
                    estudianteSeleccionado.getEmail(),
                    estudianteSeleccionado.getPrograma() != null ? estudianteSeleccionado.getPrograma().getNombre() : "Sin programa"
            );

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

            if (resultado.isPresent() && resultado.get() == btnEliminarBtn) {
                try {
                    // Eliminar de la base de datos
                    if (EstudianteDao.eliminar(estudianteSeleccionado.getID())) {
                        // Actualizar tabla
                        actualizarTablaEstudiantes();

                        /*mostrarInformacion("Éxito",
                                "El estudiante " + estudianteSeleccionado.getNombres() + " " +
                                        estudianteSeleccionado.getApellidos() + " ha sido eliminado correctamente.");
                                        */}
                    else {
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

    private void verDetallesEstudiante() {
        Estudiante estudianteSeleccionado = tablaEstudiantes.getSelectionModel().getSelectedItem();
        if (estudianteSeleccionado != null) {
            String detalles = String.format("""
                Detalles del Estudiante:
                
                ID: %.0f
                Código: %.0f
                Nombres: %s
                Apellidos: %s
                Email: %s
                Programa: %s
                Facultad: %s
                Promedio: %.2f
                Estado: %s
                """,
                    estudianteSeleccionado.getID(),
                    estudianteSeleccionado.getCodigo(),
                    estudianteSeleccionado.getNombres(),
                    estudianteSeleccionado.getApellidos(),
                    estudianteSeleccionado.getEmail(),
                    estudianteSeleccionado.getPrograma() != null ? estudianteSeleccionado.getPrograma().getNombre() : "Sin programa",
                    estudianteSeleccionado.getPrograma() != null && estudianteSeleccionado.getPrograma().getFacultad() != null ?
                            estudianteSeleccionado.getPrograma().getFacultad().getNombre() : "Sin facultad",
                    estudianteSeleccionado.getPromedio(),
                    estudianteSeleccionado.isActivo() ? "Activo" : "Inactivo"
            );

            Alert detallesAlert = new Alert(Alert.AlertType.INFORMATION);
            detallesAlert.setTitle("Detalles del Estudiante");
            detallesAlert.setHeaderText("Información Completa");
            detallesAlert.setContentText(detalles);

            // Hacer que la ventana sea redimensionable para contenido largo
            detallesAlert.setResizable(true);
            detallesAlert.getDialogPane().setPrefWidth(500);

            detallesAlert.showAndWait();
        }
    }
    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public Estudiante getEstudianteSeleccionado() {
        return tablaEstudiantes.getSelectionModel().getSelectedItem();
    }

    /**
     * Selecciona un estudiante en la tabla por su código
     */
    public void seleccionarEstudiante(double codigo) {
        for (Estudiante estudiante : tablaEstudiantes.getItems()) {
            if (estudiante.getCodigo() == codigo) {
                tablaEstudiantes.getSelectionModel().select(estudiante);
                tablaEstudiantes.scrollTo(estudiante);
                break;
            }
        }
    }
}