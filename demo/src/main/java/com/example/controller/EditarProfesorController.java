package com.example.controller;


import com.example.dao.ProfesorDAO;
import com.example.model.Profesor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarProfesorController implements Initializable {

    @FXML private TextField txtID;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbTipoContrato;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private PersonaController parentController;
    private Profesor profesorOriginal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar ComboBox
        cmbTipoContrato.getItems().addAll("Fijo", "Temporal", "Catedra", "Medio tiempo");

        // Hacer el ID no editable
        txtID.setEditable(false);

        configurarEventos();
    }

    private void configurarEventos() {
        btnGuardar.setOnAction(event -> guardarCambios());
        btnCancelar.setOnAction(event -> cerrarVentana());
    }

    public void setParentController(PersonaController parentController) {
        this.parentController = parentController;
    }

    public void setProfesor(Profesor profesor) {
        this.profesorOriginal = profesor;
        cargarDatosProfesor();
    }

    private void cargarDatosProfesor() {
        if (profesorOriginal != null) {
            txtID.setText(String.valueOf(profesorOriginal.getID()));
            txtNombres.setText(profesorOriginal.getNombres());
            txtApellidos.setText(profesorOriginal.getApellidos());
            txtEmail.setText(profesorOriginal.getEmail());
            cmbTipoContrato.setValue(profesorOriginal.getTipoContrato());
        }
    }

    @FXML
    private void guardarCambios() {
        if (!validarCampos()) {
            return;
        }

        try {
            // Crear profesor con los datos modificados
            Profesor profesorModificado = new Profesor(
                    profesorOriginal.getID(),
                    txtNombres.getText().trim(),
                    txtApellidos.getText().trim(),
                    txtEmail.getText().trim(),
                    cmbTipoContrato.getValue()
            );

            // Actualizar en la base de datos
            boolean actualizado = ProfesorDAO.actualizar(profesorModificado);

            if (actualizado) {
                //mostrarMensaje(Alert.AlertType.INFORMATION, "Éxito","Profesor actualizado correctamente");
                // Actualizar tabla en el controlador padre
                if (parentController != null) {
                    parentController.actualizarTablaProfesores();
                    parentController.seleccionarProfesor(profesorOriginal.getID());
                }

                cerrarVentana();
            } else {
                mostrarMensaje(Alert.AlertType.ERROR, "Error",
                        "No se pudo actualizar el profesor");
            }

        } catch (Exception e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error",
                    "Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String errores = "";

        if (txtNombres.getText().trim().isEmpty()) {
            errores += "- El nombre es obligatorio\n";
        }

        if (txtApellidos.getText().trim().isEmpty()) {
            errores += "- Los apellidos son obligatorios\n";
        }

        if (txtEmail.getText().trim().isEmpty()) {
            errores += "- El email es obligatorio\n";
        } else if (!esEmailValido(txtEmail.getText().trim())) {
            errores += "- El formato del email no es válido\n";
        }

        if (cmbTipoContrato.getValue() == null) {
            errores += "- Debe seleccionar un tipo de contrato\n";
        }

        if (!errores.isEmpty()) {
            mostrarMensaje(Alert.AlertType.WARNING, "Campos Requeridos", errores);
            return false;
        }

        return true;
    }

    private boolean esEmailValido(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
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