package com.example.controllerFXML;

import com.example.dataTransfer.*;
import com.example.controller.*;
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

public class AddInscripcionController implements Initializable {
    @FXML
    private ComboBox<PersonaDTO> cmbEstudiante;
    @FXML
    private ComboBox<CursoDTO> cmbCurso;
    @FXML
    private TextField txtAnio;
    @FXML
    private ComboBox<Integer> cmbSemestre;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Label lblMensaje;

    private ShowInscripcionController parentController;
    private InscripcionController inscripcionController;
    private EstudianteController estudianteController;
    private CursoController cursoController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inscripcionController = InscripcionController.getInstance();
        estudianteController = EstudianteController.getInstance();
        cursoController = CursoController.getInstance();
        inicializarComboBoxes();
        configurarValidaciones();
    }

    /**
     * Inicializa los datos de los ComboBox
     */
    private void inicializarComboBoxes() {
        try {
            List<EstudianteDTO> estudiantes = estudianteController.getAll();
            ObservableList<PersonaDTO> observableList = FXCollections.observableArrayList(
                    estudiantes
            );
            cmbEstudiante.setItems(observableList);

            List<CursoDTO> cursos = cursoController.getAll();
            ObservableList<CursoDTO> observableList1Cursos = FXCollections.observableArrayList(
                    cursos
            );
            cmbCurso.setItems(observableList1Cursos);

            ObservableList<Integer> observableList1Semestre = FXCollections.observableArrayList(
                    1,
                    2
            );
            cmbSemestre.setItems(observableList1Semestre);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Configura las validaciones en tiempo real de los campos
     */
    private void configurarValidaciones() {
        // Validación para solo números en anio
        txtAnio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtAnio.setText(oldValue);
            }
        });

    }

    @FXML
    private void save() {
        if (formularioValido()) {
            try {
                InscripcionDTO inscripcion = getInscripcionDTO();

                // Verificar si el ID ya existe
                if (idExiste(inscripcion)) {
                    mostrarMensajeError("Ya ha sido registrado con anterioridad.");
                    return;
                }
                inscripcionController.insert(inscripcion);

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

    private InscripcionDTO getInscripcionDTO() {
        double idEstudiante = cmbEstudiante.getValue().getID();
        String nombreEstudiante = cmbEstudiante.getValue().getNombres();
        int idCurso = cmbCurso.getValue().getID();
        String nombreCurso = cmbCurso.getValue().getNombre();
        int anio = Integer.parseInt(txtAnio.getText().trim());
        int semestre = cmbSemestre.getValue();

        InscripcionDTO inscripcion = new InscripcionDTO(
                idEstudiante, 
                nombreEstudiante, 
                idCurso, 
                nombreCurso, 
                anio, 
                semestre
        );
        return inscripcion;
    }

    /**
     * Verifica si un ID ya existe en la base de datos
     */
    private boolean idExiste(InscripcionDTO inscripcion) {
        try {
            return inscripcionController.alreadyExist(inscripcion);
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
        if (txtAnio.getText() == null || txtAnio.getText().trim().isEmpty()) {
            errores.append("• El campo AÑO es requerido\n");
        } else {
            try {
                double anio = Double.parseDouble(txtAnio.getText().trim());
                if (anio <= 0) {
                    errores.append("• El AÑO debe ser un número positivo\n");
                }
            } catch (NumberFormatException e) {
                errores.append("• El AÑO debe ser un número válido\n");
            }
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
        parentController.actualizarTabla();
    }

    private void mostrarMensajeExito(String mensaje) {
        lblMensaje.setText("✅ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
    }

    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    /**
     * Establece el controlador padre para poder actualizar la tabla
     */
    public void setParentController(ShowInscripcionController parentController) {
        this.parentController = parentController;
    }
}
