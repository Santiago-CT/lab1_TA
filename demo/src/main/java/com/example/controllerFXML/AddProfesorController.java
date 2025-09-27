package com.example.controllerFXML;

import com.example.DTO.ProfesorDTO;
import com.example.controller.ProfesorController;
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
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddProfesorController implements Initializable {

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<String> cmbTipoContrato;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblMensaje;
    private ShowProfesorController parentController;
    private ProfesorController profesorController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profesorController = new ProfesorController();
        inicializarComboBoxes();
        configurarValidaciones();
    }

    /**
     * Inicializa los datos de los ComboBox
     */
    private void inicializarComboBoxes() {
        // Tipos de contrato disponibles
        ObservableList<String> tiposContrato = FXCollections.observableArrayList(
                "TIEMPO COMPLETO",
                "MEDIO TIEMPO",
                "CATEDRA"
        );
        cmbTipoContrato.setItems(tiposContrato);
        cmbTipoContrato.setValue("Seleccione el tipo"); // Por defecto
    }

    /**
     * Configura las validaciones en tiempo real de los campos
     */
    private void configurarValidaciones() {
        // Validación del email en tiempo real
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !profesorController.validarFormatoEmailPublico(newValue)) {
                txtEmail.setStyle("-fx-border-color: #e74c3c;");
            } else {
                txtEmail.setStyle("");
            }
        });

        // Validación para nombres (solo letras y espacios)
        txtNombres.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtNombres.setText(oldValue);
            }
        });

        // Validación para apellidos (solo letras y espacios)
        txtApellidos.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                txtApellidos.setText(oldValue);
            }
        });
    }

    /**
     * Maneja el evento de guardar profesor
     */
    @FXML
    private void guardarProfesor() {
        if (validarFormulario()) {
            try {
                // Guardar usando ProfesorController
                boolean resultado = profesorController.insert(
                        new ProfesorDTO(
                                Double.parseDouble(txtID.getText().trim()),
                                txtNombres.getText().trim(),
                                txtApellidos.getText().trim(),
                                txtEmail.getText().trim().toLowerCase(),
                                cmbTipoContrato.getValue()
                        )
                );

                if (resultado) {
                    mostrarMensajeExito("Profesor guardado exitosamente");

                    // Actualizar la tabla en el controlador padre
                    if (parentController != null) {
                        parentController.actualizarTabla();
                    }

                    cerrarVentana();
                } else {
                    mostrarMensajeError("No se pudo guardar el profesor");
                }

            } catch (Exception e) {
                mostrarMensajeError("Error al guardar el profesor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Valida todos los campos del formulario
     */
    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();

        // Validar nombres
        if (txtNombres.getText() == null || txtNombres.getText().trim().isEmpty()) {
            errores.append("• El campo Nombres es requerido\n");
        } else if (txtNombres.getText().trim().length() < 2) {
            errores.append("• Los nombres deben tener al menos 2 caracteres\n");
        }

        // Validar apellidos
        if (txtApellidos.getText() == null || txtApellidos.getText().trim().isEmpty()) {
            errores.append("• El campo Apellidos es requerido\n");
        } else if (txtApellidos.getText().trim().length() < 2) {
            errores.append("• Los apellidos deben tener al menos 2 caracteres\n");
        }

        // Validar email
        if (txtEmail.getText() == null || txtEmail.getText().trim().isEmpty()) {
            errores.append("• El campo Email es requerido\n");
        } else if (!profesorController.validarFormatoEmailPublico(txtEmail.getText().trim())) {
            errores.append("• El formato del email no es válido\n");
        }


        // Validar tipo de contrato
        if (cmbTipoContrato.getValue() == null) {
            errores.append("• Debe seleccionar un tipo de contrato\n");
        }

        if (errores.length() > 0) {
            mostrarMensajeError("Por favor corrija los siguientes errores:\n" + errores.toString());
            return false;
        }

        return true;
    }

    /**
     * Maneja el evento de cancelar
     */
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana modal
     */
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra un mensaje de éxito
     */
    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText("✅ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }

    /**
     * Muestra un mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtEmail.clear();
        cmbTipoContrato.setValue("TIEMPO_COMPLETO");
        lblMensaje.setText("");
    }

    public void setParentController(ShowProfesorController parentController) {
        this.parentController = parentController;
    }
}