/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controllerFXML;

import com.example.Main;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.services.DB_Services;
import com.example.services.View;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author gon
 */
public class SceneManager extends Application implements View {

    private static Scene scene;

    @FXML
    private Label usuarioActual;
    @FXML
    private Label fechaActual;
    @FXML
    public Button btnAgregar;

    @Override
    public void iniciar(){
        launch();
    }

    @Override
    public void init(){
        Automatizacion automatizacion = new Automatizacion();
        automatizacion.run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Delegamos la carga de la vista a otra clase
        SceneManager.showMainView(stage);
    }

    public static void showMainView(Stage primaryStage) throws IOException {
        scene = new Scene(loadFXML("inicio"), 900, 600);

        primaryStage.setTitle("SGA");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void setRoot(String fxml) throws IOException {
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
        SceneManager.setRoot("showProfesor");
    }

    @FXML
    public void mostrarEstudiantes() throws IOException {
        SceneManager.setRoot("showEstudiante");
    }

    @FXML
    public void mostrarFacultades() throws IOException {
        SceneManager.setRoot("showFacultad");
    }

    @FXML
    public void mostrarProgramas() throws IOException {
        SceneManager.setRoot("showPrograma");
    }

    @FXML
    public void mostrarCursos() throws IOException {
        SceneManager.setRoot("showCurso");
    }

    @FXML
    public void mostrarInscripciones() throws IOException {
        SceneManager.setRoot("showInscripcion");
    }
    
    public void initVars(){
        // Configurar fecha actual
        if (fechaActual != null) {
            //LocalDate fecha = LocalDate.now();
            //fechaActual.setText(fecha.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));
            String date = DB_Services.getDate();
            fechaActual.setText("Fecha: " + date);
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

    public void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText("Ha ocurrido un error");
        alert.setContentText(mensaje);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(400);
        alert.showAndWait();
    }

    public void showMessage(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
