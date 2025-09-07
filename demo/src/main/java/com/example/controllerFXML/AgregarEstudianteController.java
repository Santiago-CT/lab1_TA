package com.example.controllerFXML;

import com.example.dao.EstudianteDao;
import com.example.model.ConexionBD;
import com.example.dao.ProgramaDao;
import com.example.model.Estudiante;
import com.example.model.Programa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AgregarEstudianteController implements Initializable {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefono; // Este campo no se usa en tu modelo, se puede quitar del FXML
    @FXML private ComboBox<Programa> cmbPrograma;
    @FXML private ComboBox<Integer> cmbSemestre; // Este campo no se usa en tu modelo, se puede quitar del FXML
    @FXML private ComboBox<String> cmbEstado; // Mapearemos a boolean activo
    @FXML private DatePicker dpFechaIngreso; // Este campo no se usa en tu modelo, se puede quitar del FXML
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Label lblMensaje;

    private EstudianteControllerV parentController;

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
        try {
            // Cargar programas desde la base de datos
            var programas = ProgramaDao.get();
            ObservableList<Programa> listaProgramas = FXCollections.observableArrayList(programas);
            cmbPrograma.setItems(listaProgramas);

            // Configurar cómo se muestra el programa en el ComboBox
            cmbPrograma.setConverter(new javafx.util.StringConverter<Programa>() {
                @Override
                public String toString(Programa programa) {
                    return programa != null ? programa.getNombre() : "";
                }

                @Override
                public Programa fromString(String string) {
                    return cmbPrograma.getItems().stream()
                            .filter(programa -> programa.getNombre().equals(string))
                            .findFirst()
                            .orElse(null);
                }
            });
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

        // Semestres (si decides mantener este campo)
        if (cmbSemestre != null) {
            ObservableList<Integer> semestres = FXCollections.observableArrayList(
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10
            );
            cmbSemestre.setItems(semestres);
        }
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

        // Validación para solo números en código (tu modelo usa double)
        txtCodigo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtCodigo.setText(oldValue);
            }
        });

        // Validación para teléfono (si decides mantenerlo)
        if (txtTelefono != null) {
            txtTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("[\\d\\s\\-\\+\\(\\)]*")) {
                    txtTelefono.setText(oldValue);
                }
            });
        }

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
    private void guardarEstudiante() {
        if (validarFormulario()) {
            try {
                double codigo = Double.parseDouble(txtCodigo.getText().trim());

                // Verificar si el código ya existe
                if (codigoExiste(codigo)) {
                    mostrarMensajeError("El código " + codigo + " ya está registrado para otro estudiante");
                    return;
                }

                // Generar un ID único para la persona (puedes usar el mismo código o generar uno diferente)
                double personaId = codigo; // O usar un generador de ID diferente

                // Convertir estado a boolean
                boolean activo = "ACTIVO".equals(cmbEstado.getValue());

                // Crear objeto Estudiante usando tu constructor
                Estudiante nuevoEstudiante = new Estudiante(
                        personaId,  // ID de persona
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtEmail.getText().trim().toLowerCase(),
                        codigo,
                        cmbPrograma.getValue(),
                        activo,
                        0.0  // Promedio inicial
                );

                // Guardar en la base de datos
                EstudianteDao.insert(nuevoEstudiante);

                mostrarMensajeExito("Estudiante guardado exitosamente");

                // Actualizar la tabla en el controlador padre
                if (parentController != null) {
                    parentController.actualizarTablaEstudiantes();
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
                mostrarMensajeError("El código debe ser un número válido");
            } catch (Exception e) {
                mostrarMensajeError("Error al guardar el estudiante: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si un código ya existe en la base de datos
     */
    private boolean codigoExiste(double codigo) {
        try {
            var estudiantes = EstudianteDao.get();
            return estudiantes.stream().anyMatch(e -> e.getCodigo() == codigo);
        } catch (Exception e) {
            System.err.println("Error al verificar código existente: " + e.getMessage());
            return false;
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
        } else if (!EMAIL_PATTERN.matcher(txtEmail.getText().trim()).matches()) {
            errores.append("• El formato del email no es válido\n");
        } else if (emailExiste(txtEmail.getText().trim())) {
            errores.append("• El email ya está registrado para otra persona\n");
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
     * Verifica si un email ya existe
     */
    private boolean emailExiste(String email) {
        try {
            var estudiantes = ConexionBD.getEstudiantes();
            return estudiantes.stream().anyMatch(e ->
                    e.getEmail().equalsIgnoreCase(email));
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
        txtCodigo.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtEmail.clear();
        if (txtTelefono != null) txtTelefono.clear();
        cmbPrograma.setValue(null);
        if (cmbSemestre != null) cmbSemestre.setValue(null);
        cmbEstado.setValue("ACTIVO");
        if (dpFechaIngreso != null) dpFechaIngreso.setValue(null);
        lblMensaje.setText("");
    }

    public void setParentController(EstudianteControllerV parentController) {
        this.parentController = parentController;
    }
}