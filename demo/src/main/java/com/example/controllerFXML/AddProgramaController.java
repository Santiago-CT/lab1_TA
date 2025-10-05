package com.example.controllerFXML;

import com.example.dataTransfer.FacultadDTO;
import com.example.dataTransfer.ProgramaDTO;
import com.example.controller.FacultadController;
import com.example.controller.ProgramaController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AddProgramaController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<FacultadDTO> cmbFacultad;
    @FXML
    private TextField txtDuracion;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lblMensaje;

    private ProgramaController programaController;
    private FacultadController facultadController;

    private ShowProgramaController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facultadController = FacultadController.getInstance();
        programaController = ProgramaController.getInstance();
        inicializarComboBoxes();
        configurarValidaciones();
    }

    /**
     * Inicializa los datos de los ComboBox
     */
    private void inicializarComboBoxes() {
        try {
            List<FacultadDTO> facultades = facultadController.getAll();
            ObservableList<FacultadDTO> observableList = FXCollections.observableArrayList(
                    facultades
            );
            cmbFacultad.setItems(observableList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Configura las validaciones en tiempo real de los campos
     */
    private void configurarValidaciones() {

        // Validación para solo números en ID
        txtId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtId.setText(oldValue);
            }
        });

        // Validación para nombres (solo letras y espacios)
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtNombre.setText(oldValue);
            }
        });

        // Validación para solo números en Duracion
        txtDuracion.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtDuracion.setText(oldValue);
            }
        });

    }

    @FXML
    private void save() {
        if (formularioValido()) {
            try {
                double id = Double.parseDouble(txtId.getText().trim());
                ProgramaDTO programa = new ProgramaDTO(
                        id,
                        txtNombre.getText().trim(),
                        Double.parseDouble(txtDuracion.getText()),
                        Date.valueOf(LocalDate.now()),
                        cmbFacultad.getValue().getID(),
                        cmbFacultad.getValue().getNombre()
                );

                // Verificar si el ID ya existe
                if (idExiste(programa)) {
                    mostrarMensajeError("El ID " + id + " ya está registrado");
                    return;
                }

                programaController.insert(programa);

                // Actualizar la tabla en el controlador padre
                if (parentController != null) {
                    parentController.actualizarTabla();
                }

                cerrarVentana();

            } catch (NumberFormatException e) {
                mostrarMensajeError("El ID debe ser un número válido");
            } catch (Exception e) {
                mostrarMensajeError("Error al guardar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si un ID ya existe en la base de datos
     */
    private boolean idExiste(ProgramaDTO programa) {
        try {
            return programaController.existePrograma(programa);
        } catch (Exception e) {
            System.err.println("Error al verificar ID existente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida todos los campos del formulario
     */
    private boolean formularioValido() {
        StringBuilder errores = new StringBuilder();

        // Validar ID
        if (txtId.getText() == null || txtId.getText().trim().isEmpty()) {
            errores.append("• El campo ID es requerido\n");
        } else {
            try {
                double id = Double.parseDouble(txtId.getText().trim());
                if (id <= 0) {
                    errores.append("• El ID debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("• El ID debe ser un número válido\n");
            }
        }

        // Validar nombres
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            errores.append("• El campo Nombres es requerido\n");
        } else if (txtNombre.getText().trim().length() < 2) {
            errores.append("• Los nombres deben tener al menos 2 caracteres\n");
        }

        // Validar Duracion
        if (txtDuracion.getText() == null || txtDuracion.getText().trim().isEmpty()) {
            errores.append("• El campo Duracion es requerido\n");
        } else {
            try {
                double duracion = Double.parseDouble(txtDuracion.getText().trim());
                if (duracion <= 0) {
                    errores.append("• El ID debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("• El ID debe ser un número válido\n");
            }
        }

        if (cmbFacultad.getValue() == null) {
            errores.append("• El campo Facultad es requerido\n");
        }

        if (!errores.isEmpty()) {
            mostrarMensajeError("Por favor corrija los siguientes errores:\n" + errores.toString());
            return false;
        }

        return true;
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText("✅ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }

    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtNombre.clear();
        lblMensaje.setText("");
    }

    /**
     * Establece el controlador padre para poder actualizar la tabla
     */
    public void setParentController(ShowProgramaController parentController) {
        this.parentController = parentController;
    }
}