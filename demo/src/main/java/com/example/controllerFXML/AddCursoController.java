package com.example.controllerFXML;

import com.example.dataTransfer.CursoDTO;
import com.example.dataTransfer.ProgramaDTO;
import com.example.InterfazObservador.ObserverView;
import com.example.controller.CursoController;
import com.example.controller.ProgramaController;
import com.example.model.Curso;
import com.example.model.Programa;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCursoController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<ProgramaDTO> cmbPrograma;
    @FXML
    private ComboBox<String> cmbActivo;
    @FXML
    private javafx.scene.control.Button btnGuardar;
    @FXML
    private javafx.scene.control.Button btnCancelar;
    @FXML
    private Label lblMensaje;

    private ShowCursoController showCursoController;
    private CursoController cursoController;
    private ProgramaController programaController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCursoController = new ShowCursoController();
        cursoController = CursoController.getInstance();
        programaController = ProgramaController.getInstance();

        inicializarComboBoxes();
    }

    private void inicializarComboBoxes() {
        try {
            List<ProgramaDTO> programaDTOS = programaController.get();
            ObservableList<ProgramaDTO> observableListProgramas = FXCollections.observableArrayList(programaDTOS);
            cmbPrograma.setItems(observableListProgramas);

            ObservableList<String> observableListEstado = FXCollections.observableArrayList("ACTIVO", "INACTIVO");
            cmbActivo.setItems(observableListEstado);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean idExiste(CursoDTO curso) {
        try {
            return cursoController.existeCurso(curso);
        } catch (Exception e) {
            System.err.println("Error al verificar ID existente: " + e.getMessage());
            return false;
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
        if (showCursoController != null) {
            showCursoController.actualizarTabla();
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void mostrarMensajeError(String mensaje) {
        lblMensaje.setText("❌ " + mensaje);
        lblMensaje.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    @FXML
    private void save() {
        try {
            CursoDTO curso = new CursoDTO(
                    Integer.parseInt(txtId.getText().trim()),
                    txtNombre.getText().trim(),
                    cmbPrograma.getValue().getID(),
                    cmbPrograma.getValue().getNombre(),
                    cmbActivo.getValue().equals("ACTIVO")
            );
            if (idExiste(curso)){
                return;
            }
            cursoController.insert(curso);

            if (showCursoController != null){
                showCursoController.actualizarTabla();
            }

            cerrarVentana();
        } catch (Exception e){
            mostrarMensajeError("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setParentController(ShowCursoController showCursoController) {
        this.showCursoController = showCursoController;
    }
}