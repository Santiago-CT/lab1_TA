package com.example.controllerFXML;

import com.example.controller.CursoController;
import com.example.observer.Observable;
import com.example.observer.Observer;
import com.example.persistence.*;
import com.example.services.DB_Services;
import com.example.services.View;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SceneManager extends Application implements View{

    private static Scene scene;
    @FXML
    public Label usuarioActual;
    @FXML
    public Label db_name;
    @FXML
    public Label fechaActual;
    @FXML
    public Button btnAgregar;
    @FXML
    public Button btnInicio, btnProfesores, btnEstudiantes, btnFacultades,
            btnProgramas, btnCursos, btnInscripciones;
    private Button botonActivo;

    @FXML
    private static void showObserverView() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/view/NotificadorView.fxml"));

        Parent root = loader.load();

        ObserverController observerController = loader.getController();
        CursoController.getInstance().addObserver(observerController);

        Stage stage = new Stage();
        stage.setTitle("Observador de Cursos");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void iniciar() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        showMainView(stage);
        showObserverView();
    }

    private void showMainView(Stage primaryStage) throws IOException {
        scene = new Scene(loadFXML("inicio"), 900, 600);
        primaryStage.setTitle("SGA");
        primaryStage.setScene(scene);
        primaryStage.show();
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

    public void initVars() {
        if (fechaActual != null) {
            String date = DB_Services.getDate();
            fechaActual.setText("Fecha: " + date);
        }

        if (usuarioActual != null) {
            usuarioActual.setText("Usuario: Administrador");
        }

        if (db_name != null) {
            db_name.setText("Database: " + DB_Services.getDB_Name());
        }
    }

    @FXML
    public void cerrarSesion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar Sesión");
        alert.setHeaderText("¿Está seguro que desea cerrar sesión?");
        alert.setContentText("Se cerrará la aplicación.");

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

    public void setBotonActivo(Button boton) {
        if (botonActivo != null) {
            botonActivo.getStyleClass().remove("active");
        }
        boton.getStyleClass().add("active");
        botonActivo = boton;
    }

}