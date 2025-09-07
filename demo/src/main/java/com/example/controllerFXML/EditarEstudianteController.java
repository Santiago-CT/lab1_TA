package com.example.controllerFXML;

import com.example.dao.EstudianteDao;
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

public class EditarEstudianteController implements Initializable {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPromedio;
    @FXML private ComboBox<Programa> cmbPrograma;
    @FXML private CheckBox chkActivo;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private EstudianteControllerV parentController;
    private Estudiante estudianteActual;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarProgramas();
        configurarValidaciones();
        configurarEventosBotones();
    }

    private void cargarProgramas() {
        try {
            ObservableList<Programa> programas = FXCollections.observableArrayList(ProgramaDao.get());
            cmbPrograma.setItems(programas);

            // Configurar cómo se muestra cada programa en el ComboBox
            cmbPrograma.setCellFactory(param -> new ListCell<Programa>() {
                @Override
                protected void updateItem(Programa item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre() + " - " + item.getFacultad().getNombre());
                    }
                }
            });

            cmbPrograma.setButtonCell(new ListCell<Programa>() {
                @Override
                protected void updateItem(Programa item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre() + " - " + item.getFacultad().getNombre());
                    }
                }
            });

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los programas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarValidaciones() {
        // Validación para código (solo números)
        txtCodigo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCodigo.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Validación para nombres (solo letras y espacios)
        txtNombres.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                txtNombres.setText(newValue.replaceAll("[^a-zA-ZÀ-ÿ\\s]", ""));
            }
        });

        // Validación para apellidos (solo letras y espacios)
        txtApellidos.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                txtApellidos.setText(newValue.replaceAll("[^a-zA-ZÀ-ÿ\\s]", ""));
            }
        });

        // Validación para promedio (números decimales)
        txtPromedio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPromedio.setText(oldValue);
            }
        });
    }

    private void configurarEventosBotones() {
        btnGuardar.setOnAction(event -> guardarCambios());
        btnCancelar.setOnAction(event -> cancelar());
    }

    public void setParentController(EstudianteControllerV parentController) {
        this.parentController = parentController;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudianteActual = estudiante;
        cargarDatosEstudiante();
    }

    private void cargarDatosEstudiante() {
        if (estudianteActual != null) {
            txtCodigo.setText(String.valueOf((long)estudianteActual.getCodigo()));
            txtNombres.setText(estudianteActual.getNombres());
            txtApellidos.setText(estudianteActual.getApellidos());
            txtEmail.setText(estudianteActual.getEmail());
            txtPromedio.setText(String.valueOf(estudianteActual.getPromedio()));
            chkActivo.setSelected(estudianteActual.isActivo());

            // Seleccionar el programa actual
            for (Programa programa : cmbPrograma.getItems()) {
                if (programa.getID() == estudianteActual.getPrograma().getID()) {
                    cmbPrograma.setValue(programa);
                    break;
                }
            }
        }
    }

    @FXML
    private void guardarCambios() {
        if (validarCampos()) {
            try {
                // Crear estudiante actualizado
                Estudiante estudianteActualizado = new Estudiante(
                        estudianteActual.getID(),
                        txtNombres.getText().trim(),
                        txtApellidos.getText().trim(),
                        txtEmail.getText().trim(),
                        Double.parseDouble(txtCodigo.getText()),
                        cmbPrograma.getValue(),
                        chkActivo.isSelected(),
                        Double.parseDouble(txtPromedio.getText())
                );

                // Actualizar en la base de datos
                if (EstudianteDao.actualizar(estudianteActualizado)) {
                   // mostrarInformacion("Éxito", "Estudiante actualizado correctamente");

                    // Actualizar la tabla en el controlador padre
                    if (parentController != null) {
                        parentController.actualizarTablaEstudiantes();
                    }

                    cerrarVentana();
                } else {
                    mostrarError("Error", "No se pudo actualizar el estudiante. Verifique que el código y email no estén en uso por otro estudiante.");
                }

            } catch (Exception e) {
                mostrarError("Error", "Error al actualizar el estudiante: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtCodigo.getText().trim().isEmpty()) {
            errores.append("- El código es obligatorio\n");
        } else {
            try {
                double codigo = Double.parseDouble(txtCodigo.getText());
                if (codigo <= 0) {
                    errores.append("- El código debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El código debe ser un número válido\n");
            }
        }

        if (txtNombres.getText().trim().isEmpty()) {
            errores.append("- Los nombres son obligatorios\n");
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            errores.append("- Los apellidos son obligatorios\n");
        }

        if (txtEmail.getText().trim().isEmpty()) {
            errores.append("- El email es obligatorio\n");
        } else if (!esEmailValido(txtEmail.getText().trim())) {
            errores.append("- El email no tiene un formato válido\n");
        }

        if (txtPromedio.getText().trim().isEmpty()) {
            errores.append("- El promedio es obligatorio\n");
        } else {
            try {
                double promedio = Double.parseDouble(txtPromedio.getText());
                if (promedio < 0 || promedio > 5) {
                    errores.append("- El promedio debe estar entre 0 y 5\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El promedio debe ser un número válido\n");
            }
        }

        if (cmbPrograma.getValue() == null) {
            errores.append("- Debe seleccionar un programa\n");
        }

        if (errores.length() > 0) {
            mostrarError("Campos Inválidos", "Por favor corrija los siguientes errores:\n\n" + errores.toString());
            return false;
        }

        return true;
    }

    private boolean esEmailValido(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}