/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controllerFXML;

import com.example.Main;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author gon
 */
public class SceneManager {

    private static Scene scene;
    
    @FXML
    private Label usuarioActual;
    @FXML
    private Label fechaActual;

    public static void showMainView(Stage primaryStage) throws IOException {
        scene = new Scene(loadFXML("inicio"), 800, 600);

        primaryStage.setTitle("SGA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @FXML
    public void mostrarInicio() throws IOException {
        SceneManager.setRoot("inicio");
    }
    
    @FXML
    public void mostrarProfesores() throws IOException {
        SceneManager.setRoot("profesorShow");
    }

    @FXML
    public void mostrarEstudiantes() throws IOException {
        SceneManager.setRoot("estudianteShow");
    }

    @FXML
    public void mostrarFacultades() throws IOException {
        SceneManager.setRoot("facultadShow");
    }

    @FXML
    public void mostrarProgramas() throws IOException {
        SceneManager.setRoot("programaShow");
    }

    @FXML
    public void mostrarCursos() throws IOException {
        SceneManager.setRoot("cursoShow");
    }

    @FXML
    public void mostrarInscripciones() throws IOException {
        SceneManager.setRoot("inscripcionShow");
    }
    
    public void initVars(){
        // Configurar fecha actual
        if (fechaActual != null) {
            LocalDate fecha = LocalDate.now();
            fechaActual.setText(fecha.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
        }

        if (usuarioActual != null) {
            usuarioActual.setText("Usuario: Administrador");
        }
    }
    
    @FXML
    public void cerrarSesion() {
        // Aquí implementarías la lógica para cerrar sesión
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("¿Está seguro que desea cerrar sesión?");
        alert.setContentText("Se cerrará la aplicación y deberá iniciar sesión nuevamente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.exit(0);
            }
        });
    }
}
