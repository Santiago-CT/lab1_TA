package com.example.controllerFXML;

import com.example.dataTransfer.CursoDTO;
import com.example.controller.CursoController;
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


public class ShowCursoController extends SceneManager implements Initializable {
    @FXML
    private TableView<CursoDTO> tablaCursos;
    @FXML
    private TableColumn<CursoDTO, Double> colID;
    @FXML
    private TableColumn<CursoDTO, String> colNombre;
    @FXML
    private TableColumn<CursoDTO, Double> colIdPrograma;
    @FXML
    private TableColumn<CursoDTO, String> colNombrePrograma;
    @FXML
    private TableColumn<CursoDTO, Boolean> colActivo;

    private CursoController cursoController;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cursoController = CursoController.getInstance();

        configurarColumnas();
        configurarEventosBotones();
        actualizarTabla();
        initVars();
    }

    private void configurarColumnas(){
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdPrograma.setCellValueFactory(new PropertyValueFactory<>("idPrograma"));
        colNombrePrograma.setCellValueFactory(new PropertyValueFactory<>("nombrePrograma"));
        colActivo.setCellValueFactory(new PropertyValueFactory<>("activo"));

        tablaCursos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configurarEventosBotones(){
        btnAgregar.setOnAction(event -> mostrarVentanaAgregarFacultad());
    }
    public void actualizarTabla(){
        try {
            List<CursoDTO> cursosDAO = cursoController.getAll();
            ObservableList<CursoDTO> observableList = FXCollections.observableArrayList(cursosDAO);
            tablaCursos.setItems(observableList);
        } catch (Exception e) {
            mostrarError("Error", "No se pudieron cargar los datos de facultades: " + e.getMessage());
            e.printStackTrace();

            // En caso de error, mostrar lista vacía
            tablaCursos.setItems(FXCollections.observableArrayList());
        }
    }

    private void mostrarVentanaAgregarFacultad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addCurso.fxml"));
            Parent root = loader.load();

            AddCursoController controller = loader.getController();
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
}
