package com.example.controller;

import com.example.model.ConexionBD;
import com.example.model.Profesor;
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

public class AgregarProfesorController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbTipoContrato;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Label lblMensaje;

    private PersonaController parentController;

    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inicializarComboBoxes();
        configurarValidaciones();
    }

    /**
     * Inicializa los datos de los ComboBox
     */
    private void inicializarComboBoxes() {
        // Tipos de contrato basados en tu modelo
        ObservableList<String> tiposContrato = FXCollections.observableArrayList(
                "Fijo",
                "Temporal",
                "Catedra",
                "Medio Tiempo",
                "Tiempo Completo"
        );
        cmbTipoContrato.setItems(tiposContrato);
        // Seleccionar "Fijo" por defecto como en tu ejemplo
        cmbTipoContrato.setValue("Fijo");
    }

    /**
     * Configura las validaciones en tiempo real de los campos
     */
    private void configurarValidaciones() {
        // Validación del email en tiempo real
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !EMAIL_PATTERN.matcher(newValue).matches()) {
                txtEmail.setStyle("-fx-border-color: #e74c3c;");
            } else {
                txtEmail.setStyle("");
            }
        });

        // Validación para solo números en ID
        txtId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtId.setText(oldValue);
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
                // Convertir ID a double como en tu modelo
                double id = Double.parseDouble(txtId.getText().trim());

                // Verificar si el ID ya existe
                if (idExiste(id)) {
                    mostrarMensajeError("El ID " + id + " ya está registrado para otro profesor");
                    return;
                }

                // Crear objeto Profesor usando tu constructor
                Profesor nuevoProfesor = new Profesor(
                        id,
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtEmail.getText().trim().toLowerCase(),
                        cmbTipoContrato.getValue()
                );

                // Guardar en la base de datos usando tu método
                ConexionBD.insertProfesor(nuevoProfesor);

                mostrarMensajeExito("Profesor guardado exitosamente");

                // Actualizar la tabla en el controlador padre
                if (parentController != null) {
                    parentController.actualizarTablaProfesores();
                }

                // Cerrar ventana después de un momento
                javafx.concurrent.Task<Void> task = new javafx.concurrent.Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep(1500);
                        return null;
                    }
                };
                task.setOnSucceeded(e -> cerrarVentana());
                new Thread(task).start();

            } catch (NumberFormatException e) {
                mostrarMensajeError("El ID debe ser un número válido");
            } catch (Exception e) {
                mostrarMensajeError("Error al guardar el profesor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si un ID ya existe en la base de datos
     */
    private boolean idExiste(double id) {
        try {
            var profesores = ConexionBD.getProfesores();
            return profesores.stream().anyMatch(p -> p.getID() == id);
        } catch (Exception e) {
            System.err.println("Error al verificar ID existente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida todos los campos del formulario
     */
    private boolean validarFormulario() {
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
        } else if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            errores.append("• El formato del email no es válido\n");
        } else if (emailExiste(txtEmail.getText().trim())) {
            errores.append("• El email ya está registrado para otro profesor\n");
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
     * Verifica si un email ya existe
     */
    private boolean emailExiste(String email) {
        try {
            var profesores = ConexionBD.getProfesores();
            return profesores.stream().anyMatch(p ->
                    p.getEmail().equalsIgnoreCase(email));
        } catch (Exception e) {
            System.err.println("Error al verificar email existente: " + e.getMessage());
            return false;
        }
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
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtEmail.clear();
        cmbTipoContrato.setValue("Fijo");
        lblMensaje.setText("");
    }

    /**
     * Establece el controlador padre para poder actualizar la tabla
     */
    public void setParentController(PersonaController parentController) {
        this.parentController = parentController;
    }
}