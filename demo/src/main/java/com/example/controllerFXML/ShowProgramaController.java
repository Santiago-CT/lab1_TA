package com.example.controllerFXML;

import com.example.dao.ProgramaDao;
import com.example.model.Facultad;
import com.example.model.Programa;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ShowProgramaController extends SceneManager implements Initializable {

    @FXML private TableView<Programa> tablaProgramas;
    @FXML private TableColumn<Programa, Double> colID;
    @FXML private TableColumn<Programa, String> colNombre;
    @FXML private TableColumn<Programa, Double> colDuracion;
    @FXML private TableColumn<Programa, String> colRegistro;
    @FXML private TableColumn<Programa, String> colFacultad;

    @FXML private Button btnAgregar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarColumnas();
        actualizarTabla();
        configurarEventosBotones();
        initVars();
    }

    private void configurarColumnas() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        colRegistro.setCellValueFactory(new PropertyValueFactory<>("registro"));

        // Mostrar nombre completo del decano
        colFacultad.setCellValueFactory(cellData -> {
            Facultad facultad = cellData.getValue().getFacultad();
            String nombreFacultad = facultad != null ? facultad.getNombre() : "Sin asignar";
            return new SimpleStringProperty(nombreFacultad);
        });
        tablaProgramas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarPrograma());

        // Listener para habilitar/deshabilitar botones según selección
        tablaProgramas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean haySeleccion = newSel != null;
        });

    }

    private void mostrarVentanaAgregarPrograma() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addPrograma.fxml"));
            Parent root = loader.load();

            // Asumo que tienes un controlador para agregar facultad
            AddProgramaController controller = loader.getController();
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
            List<Programa> listaProgramas = ProgramaDao.get();
            ObservableList<Programa> observableList = FXCollections.observableArrayList(listaProgramas);
            tablaProgramas.setItems(observableList);

            System.out.println("Programas cargados: " + listaProgramas.size());

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaProgramas.setItems(FXCollections.observableArrayList());
        }
    }



}