package com.example.controller;

import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import com.example.model.Persona;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarFacultadController implements Initializable {

    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<Persona> cmbDecano;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private FacultadController parentController;
    private Facultad facultadOriginal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtID.setEditable(false); // ID solo lectura
        configurarEventos();

        // ⚠️ Aquí deberías cargar la lista de posibles decanos desde la BD
        // Ejemplo: cmbDecano.setItems(FXCollections.observableArrayList(PersonaDao.getDecanos()));
    }

    private void configurarEventos() {
        btnGuardar.setOnAction(event -> guardarCambios());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    public void setParentController(FacultadController parentController) {
        this.parentController = parentController;
    }

    public void setFacultad(Facultad facultad) {
        this.facultadOriginal = facultad;
        cargarDatosFacultad();
    }

    private void cargarDatosFacultad() {
        if (facultadOriginal != null) {
            txtID.setText(String.valueOf(facultadOriginal.getID()));
            txtNombre.setText(facultadOriginal.getNombre());
            cmbDecano.setValue(facultadOriginal.getDecano());
        }
    }

    @FXML
    private void guardarCambios() {
        if (!validarCampos()) {
            return;
        }

        try {
            // Crear facultad con los datos modificados
            Facultad facultadModificada = new Facultad(
                    facultadOriginal.getID(),
                    txtNombre.getText().trim(),
                    cmbDecano.getValue()
            );

            // Actualizar en la base de datos
            boolean actualizado = FacultadDao.update(facultadModificada);

            if (actualizado) {
                if (parentController != null) {
                    parentController.actualizarTablaFacultades();
                    // Opcional: seleccionar de nuevo la facultad editada
                }
                cerrarVentana();
            } else {
                mostrarMensaje(Alert.AlertType.ERROR, "Error",
                        "No se pudo actualizar la facultad");
            }

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error",
                    "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio\n");
        }

        if (cmbDecano.getValue() == null) {
            errores.append("- Debe seleccionar un decano\n");
        }

        if (!errores.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos Requeridos", errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
