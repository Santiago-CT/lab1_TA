package com.example.controllerFXML;

import com.example.dataTransfer.InscripcionDTO;
import com.example.controller.InscripcionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowInscripcionController extends SceneManager implements Initializable {
    @FXML
    private TableView<InscripcionDTO> tablaInscripciones;
    @FXML
    private TableColumn<InscripcionDTO, Double> colEstudianteID;
    @FXML
    private TableColumn<InscripcionDTO, String> colEstudianteNombre;
    @FXML
    private TableColumn<InscripcionDTO, Integer> colCursoID;
    @FXML
    private TableColumn<InscripcionDTO, String> colCursoNombre;
    @FXML
    private TableColumn<InscripcionDTO, Integer> colAnio;
    @FXML
    private TableColumn<InscripcionDTO, Integer> colSemestre;

    private InscripcionController inscripcionController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inscripcionController = InscripcionController.getInstance();
        configurarColumnas();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colEstudianteID.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        colEstudianteNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEstudiante"));
        colCursoID.setCellValueFactory(new PropertyValueFactory<>("idCurso"));
        colCursoNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCurso"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));

        tablaInscripciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregar());

    }

    private void mostrarVentanaAgregar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addInscripcion.fxml"));
            Parent root = loader.load();

            AddInscripcionController controller = loader.getController();
            controller.setParentController(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Agregar");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(btnAgregar.getScene().getWindow());
            modalStage.setResizable(false);

            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            mostrarError("Error", "No se pudo abrir la ventana de agregar facultad: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarTabla() {
        try {
            // Obtener todas las facultades de la base de datos
            List<InscripcionDTO> listaFacultades = inscripcionController.getAll();
            ObservableList<InscripcionDTO> observableList = FXCollections.observableArrayList(listaFacultades);
            tablaInscripciones.setItems(observableList);

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de facultades: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaInscripciones.setItems(FXCollections.observableArrayList());
        }
    }



}