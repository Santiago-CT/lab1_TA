package com.example.controllerFXML;

import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import com.example.model.Persona;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class FacultadController extends SceneManager implements Initializable {

    @FXML private TableView<Facultad> tablaFacultades;
    @FXML private TableColumn<Facultad, Double> colCodigo;
    @FXML private TableColumn<Facultad, String> colNombre;
    @FXML private TableColumn<Facultad, String> colDecano;
    @FXML private TableColumn<Facultad, String> colTelefono;
    @FXML private TableColumn<Facultad, String> colEmail;
    @FXML private TableColumn<Facultad, String> colProgramas;
    @FXML private TableColumn<Facultad, String> colEstado;

    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnVerDetalles;
    @FXML private Button btnGestionarProgramas;
    @FXML private Button btnReporte;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpiar;
    @FXML private TextField txtBusqueda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        actualizarTablaFacultades();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        // Mostrar nombre completo del decano
        colDecano.setCellValueFactory(cellData -> {
            Persona decano = cellData.getValue().getDecano();
            String nombreDecano = decano != null ?
                    decano.getNombres() + " " + decano.getApellidos() : "Sin asignar";
            return new SimpleStringProperty(nombreDecano);
        });

        // Mostrar email del decano
        colEmail.setCellValueFactory(cellData -> {
            Persona decano = cellData.getValue().getDecano();
            String emailDecano = decano != null && decano.getEmail() != null ?
                    decano.getEmail() : "N/A";
            return new SimpleStringProperty(emailDecano);
        });

        // Campos que necesitarías agregar al modelo Facultad para que funcionen completamente
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty("N/A"));
        colProgramas.setCellValueFactory(c -> new SimpleStringProperty("0"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty("Activo"));
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarFacultad());
        btnEditar.setOnAction(event -> editarFacultad());
        btnEliminar.setOnAction(event -> eliminarFacultad());
        btnVerDetalles.setOnAction(event -> verDetallesFacultad());

        // Deshabilitar botones hasta que haya selección
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnVerDetalles.setDisable(true);

        // Listener para habilitar/deshabilitar botones según selección
        tablaFacultades.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean haySeleccion = newSel != null;
            btnEditar.setDisable(!haySeleccion);
            btnEliminar.setDisable(!haySeleccion);
            btnVerDetalles.setDisable(!haySeleccion);
        });

        btnBuscar.setOnAction(e -> buscarFacultades());
        btnLimpiar.setOnAction(e -> {
            txtBusqueda.clear();
            actualizarTablaFacultades();
        });
    }

    private void mostrarVentanaAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/agregarFacultad.fxml"));
            Parent root = loader.load();

            // Asumo que tienes un controlador para agregar facultad
            AgregarFacultadController controller = loader.getController();
            controller.setParentController(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar Nueva Facultad");
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

    public void actualizarTablaFacultades() {
        try {
            // Obtener todas las facultades de la base de datos
            List<Facultad> listaFacultades = FacultadDao.getAll();
            ObservableList<Facultad> observableList = FXCollections.observableArrayList(listaFacultades);
            tablaFacultades.setItems(observableList);

            System.out.println("Facultades cargadas: " + listaFacultades.size());

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de facultades: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaFacultades.setItems(FXCollections.observableArrayList());
        }
    }

    private void editarFacultad() {
        Facultad facultadSeleccionada = tablaFacultades.getSelectionModel().getSelectedItem();
        if (facultadSeleccionada == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editarFacultad.fxml"));
            Parent root = loader.load();

            // Asumo que tienes un controlador para editar facultad
            EditarFacultadController controller = loader.getController();
            controller.setParentController(this);
            controller.setFacultad(facultadSeleccionada);

            Stage modalStage = new Stage();
            modalStage.setTitle("Editar Facultad - " + facultadSeleccionada.getNombre());
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnEditar.getScene().getWindow());
            modalStage.setResizable(false);

            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de edición: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * MÉTODO CORREGIDO - Verificar resultado del delete
     */
    private void eliminarFacultad() {
        Facultad seleccionada = tablaFacultades.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("¿Eliminar Facultad?");
        confirmacion.setContentText("¿Está seguro que desea eliminar la facultad '" +
                seleccionada.getNombre() + "'?\n\nEsta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                // Verificar el resultado del delete
                boolean eliminado = FacultadDao.delete(seleccionada.getID());
                if (eliminado) {
                    actualizarTablaFacultades();
                    mostrarMensaje("Éxito", "Facultad eliminada correctamente.");
                } else {
                    mostrarError("Error", "No se pudo eliminar la facultad. Es posible que no exista.");
                }
            } catch (Exception e) {
                mostrarError("Error", "No se pudo eliminar la facultad: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void verDetallesFacultad() {
        Facultad seleccionada = tablaFacultades.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        String detalles = String.format("""
            Detalles de la Facultad:
            
            ID: %.0f
            Nombre: %s
            Decano: %s
            Email del Decano: %s
            """,
                seleccionada.getID(),
                seleccionada.getNombre(),
                seleccionada.getDecano() != null ?
                        seleccionada.getDecano().getNombres() + " " + seleccionada.getDecano().getApellidos() : "Sin asignar",
                seleccionada.getDecano() != null && seleccionada.getDecano().getEmail() != null ?
                        seleccionada.getDecano().getEmail() : "Sin email"
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles de la Facultad");
        alert.setHeaderText("Información Completa");
        alert.setContentText(detalles);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(450);
        alert.showAndWait();
    }

    private void buscarFacultades() {
        String filtro = txtBusqueda.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            actualizarTablaFacultades();
            return;
        }

        try {
            // Obtener todas las facultades y filtrar
            List<Facultad> todasLasFacultades = FacultadDao.getAll();
            ObservableList<Facultad> filtradas = FXCollections.observableArrayList();

            for (Facultad f : todasLasFacultades) {
                // Buscar por nombre de facultad
                if (f.getNombre().toLowerCase().contains(filtro)) {
                    filtradas.add(f);
                }
                // También buscar por nombre del decano
                else if (f.getDecano() != null) {
                    String nombreCompleto = (f.getDecano().getNombres() + " " +
                            f.getDecano().getApellidos()).toLowerCase();
                    if (nombreCompleto.contains(filtro)) {
                        filtradas.add(f);
                    }
                }
            }

            tablaFacultades.setItems(filtradas);

        } catch (Exception e) {
            mostrarError("Error", "Error al buscar facultades: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método auxiliar para mostrar mensajes de éxito
     */
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}