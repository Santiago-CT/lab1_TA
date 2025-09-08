package com.example.controllerFXML;

import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import com.example.model.Persona;
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

public class ShowFacultadController extends SceneManager implements Initializable {

    @FXML private TableView<Facultad> tablaFacultades;
    @FXML private TableColumn<Facultad, Double> colID;
    @FXML private TableColumn<Facultad, String> colNombre;
    @FXML private TableColumn<Facultad, String> colDecano;

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

        // Mostrar nombre completo del decano
        colDecano.setCellValueFactory(cellData -> {
            Persona decano = cellData.getValue().getDecano();
            String nombreDecano = decano != null ?
                    decano.getNombres() + " " + decano.getApellidos() : "Sin asignar";
            return new SimpleStringProperty(nombreDecano);
        });
        tablaFacultades.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones() {
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarFacultad());

        // Listener para habilitar/deshabilitar botones según selección
        tablaFacultades.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean haySeleccion = newSel != null;
        });

    }

    private void mostrarVentanaAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addFacultad.fxml"));
            Parent root = loader.load();

            // Asumo que tienes un controlador para agregar facultad
            AddFacultadController controller = loader.getController();
            controller.setParentController(this);

            Stage modalStage = new Stage();
            modalStage.setTitle("Agregarg");
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
            List<Facultad> listaFacultades = FacultadDao.getAll();
            ObservableList<Facultad> observableList = FXCollections.observableArrayList(listaFacultades);
            tablaFacultades.setItems(observableList);

            System.out.println("Facultades cargadas: " + listaFacultades.size());

        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de facultades: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaFacultades.setItems(FXCollections.observableArrayList());
        }
    }



}