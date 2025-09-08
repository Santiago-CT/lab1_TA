package com.example.controllerFXML;


import com.example.dao.ProfesorDAO;
import com.example.model.Profesor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShowProfesorController extends SceneManager implements Initializable {
    
    @FXML
    private TableView<Profesor> tablaProfesores;
    @FXML
    private TableColumn<Profesor, String> colId;
    @FXML
    private TableColumn<Profesor, String> colNombres;
    @FXML
    private TableColumn<Profesor, String> colApellidos;
    @FXML
    private TableColumn<Profesor, String> colEmail;
    @FXML
    private TableColumn<Profesor, String> colTipoContrato;
    @FXML private Button btnAgregarProfesor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Configurar columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTipoContrato.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));

        // Cargar datos desde la función
        ObservableList<Profesor> lista = FXCollections.observableArrayList(ProfesorDAO.get());
        tablaProfesores.setItems(lista);
        tablaProfesores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        configurarEventosBotones();
        initVars();
    }
    private void configurarEventosBotones() {
        btnAgregarProfesor.setOnAction(event -> mostrarVentanaAgregarProfesor());
        // Habilitar botones cuando se seleccione una fila
        tablaProfesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean haySeleccion = newSelection != null;
        });
    }
    private void mostrarVentanaAgregarProfesor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addProfesor.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle la referencia de este controlador
            AddProfesorController controller = loader.getController();
            controller.setParentController(this);

            // Crear y configurar la ventana modal
            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar Nuevo Profesor");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnAgregarProfesor.getScene().getWindow());
            modalStage.setResizable(false);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);

            // Mostrar la ventana modal
            modalStage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar profesor: " + e.getMessage());
            e.printStackTrace();
        }

    }
    public void actualizarTabla() {
        try {
            ObservableList<Profesor> lista = FXCollections.observableArrayList(ProfesorDAO.get());
            tablaProfesores.setItems(lista);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de profesores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Profesor getProfesorSeleccionado() {
        return tablaProfesores.getSelectionModel().getSelectedItem();
    }

    public void seleccionarProfesor(double id) {
        for (Profesor profesor : tablaProfesores.getItems()) {
            if (profesor.getID() == id) {
                tablaProfesores.getSelectionModel().select(profesor);
                tablaProfesores.scrollTo(profesor);
                break;
            }
        }
    }
}

