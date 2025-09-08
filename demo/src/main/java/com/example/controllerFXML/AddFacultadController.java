package com.example.controllerFXML;
import com.example.dao.FacultadDao;
import com.example.dao.ProfesorDAO;
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

public class AddFacultadController implements Initializable {

    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<Profesor> cmbDecano;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Label lblMensaje;

    private ShowFacultadController parentController;

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
        ObservableList<Profesor> decanos = FXCollections.observableArrayList(
                ProfesorDAO.getAll()
        );
        cmbDecano.setItems(decanos);
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

    }

    @FXML
    private void guardar() {
        if (formularioValido()) {
            try {
                double id = Double.parseDouble(txtId.getText().trim());

                // Verificar si el ID ya existe
                if (idExiste(id)) {
                    mostrarMensajeError("El ID " + id + " ya está registrado");
                    return;
                }

                // Actualizar la tabla en el controlador padre
                if (parentController != null) {
                    parentController.actualizarTabla();
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
                mostrarMensajeError("Error al guardar: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si un ID ya existe en la base de datos
     */
    private boolean idExiste(double id) {
        try {
            var profesores = FacultadDao.getAll();
            return profesores.stream().anyMatch(p -> p.getID() == id);
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

        // Validar tipo de contrato
        if (cmbDecano.getValue() == null) {
            errores.append("• Debe seleccionar un decano\n");
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
    public void setParentController(ShowFacultadController parentController) {
        this.parentController = parentController;
    }
}