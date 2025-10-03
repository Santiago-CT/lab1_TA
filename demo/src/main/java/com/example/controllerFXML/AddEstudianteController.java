package com.example.controllerFXML;

import com.example.dataTransfer.EstudianteDTO;
import com.example.dataTransfer.ProgramaDTO;
import com.example.controller.EstudianteController;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddEstudianteController implements Initializable {
    // Patrón para validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    @FXML
    private TextField txtID;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<ProgramaDTO> cmbPrograma;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private TextField txtPromedio;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblMensaje;
    private ShowEstudianteController parentController;
    private EstudianteController estudianteController;
    private ProgramaController programaController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        estudianteController = EstudianteController.getInstance();
        programaController = ProgramaController.getInstance();
        inicializarComboBoxes();
        configurarValidaciones();
    }

    /**
     * Inicializa los datos de los ComboBox
     */
    private void inicializarComboBoxes() {
        try {
            List<ProgramaDTO> listaProgramas = programaController.get();
            ObservableList<ProgramaDTO> observableList = FXCollections.observableArrayList(
                    listaProgramas
            );
            cmbPrograma.setItems(observableList);

        } catch (Exception e) {
            System.err.println("Error al cargar programas: " + e.getMessage());
            mostrarMensajeError("Error al cargar los programas disponibles");
        }

        // Estados del estudiante (mapear a boolean activo)
        ObservableList<String> estados = FXCollections.observableArrayList(
                "ACTIVO",
                "INACTIVO"
        );
        cmbEstado.setItems(estados);
        cmbEstado.setValue("ACTIVO"); // Por defecto activo

    }

    /**
     * Configura las validaciones en tiempo real de los campos
     */
    private void configurarValidaciones() {
        // Validación del email en tiempo real
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !estudianteController.validarFormatoEmailPublico(newValue)) {
                txtEmail.setStyle("-fx-border-color: #e74c3c;");
            } else {
                txtEmail.setStyle("");
            }
        });

        // Validación para solo números en código (tu modelo usa double)
        txtCodigo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtCodigo.setText(oldValue);
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
     * Maneja el evento de guardar estudiante
     */

    @FXML
    private void save() {
        if (validarFormulario()) {
            try {
                EstudianteDTO estudiante = new EstudianteDTO(
                        Double.parseDouble(txtID.getText().trim()),
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtEmail.getText().trim().toLowerCase(),
                        Double.parseDouble(txtCodigo.getText().trim()),
                        cmbPrograma.getValue().getID(),
                        cmbPrograma.getValue().getNombre(),
                        cmbEstado.getValue().equals("ACTIVO"),
                        Double.parseDouble(txtPromedio.getText().trim())
                );

                // Verificar si el código ya existe usando EstudianteController
                if (estudianteController.alreadyExist(estudiante)) {
                    mostrarMensajeError("El código " + estudiante.getCodigo() + " ya está registrado para otro estudiante");
                    return;
                }

                boolean resultado = estudianteController.insert(estudiante);

                if (resultado) {
                    mostrarMensajeExito("Estudiante guardado exitosamente");

                    // Actualizar la tabla en el controlador padre
                    if (parentController != null) {
                        parentController.actualizarTabla();
                    }

                    cerrarVentana();
                } else {
                    mostrarMensajeError("No se pudo guardar el estudiante");
                }

            } catch (NumberFormatException e) {
                mostrarMensajeError("El código debe ser un número válido");
            } catch (Exception e) {
                mostrarMensajeError("Error al guardar el estudiante: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Valida todos los campos del formulario
     */
    private boolean validarFormulario() {
        StringBuilder errores = new StringBuilder();

        // Validar código
        if (txtCodigo.getText() == null || txtCodigo.getText().trim().isEmpty()) {
            errores.append("• El campo Código es requerido\n");
        } else {
            try {
                double codigo = Double.parseDouble(txtCodigo.getText().trim());
                if (codigo <= 0) {
                    errores.append("• El código debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("• El código debe ser un número válido\n");
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
        } else if (!estudianteController.validarFormatoEmailPublico(txtEmail.getText().trim())) {
            errores.append("• El formato del email no es válido\n");
        }

        // Validar programa
        if (cmbPrograma.getValue() == null) {
            errores.append("• Debe seleccionar un programa académico\n");
        }

        // Validar estado
        if (cmbEstado.getValue() == null) {
            errores.append("• Debe seleccionar un estado\n");
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
        txtCodigo.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtEmail.clear();
        cmbPrograma.setValue(null);
        cmbEstado.setValue("ACTIVO");
        lblMensaje.setText("");
    }

    public void setParentController(ShowEstudianteController parentController) {
        this.parentController = parentController;
    }
}