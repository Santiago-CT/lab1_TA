package com.example.controllerFXML;

import com.example.controller.CursoController;
import com.example.observer.Observer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ObserverController implements Observer, Initializable {

    @FXML
    private TextArea logTextArea; // Estado del observador

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (logTextArea != null) {
            logTextArea.setText("Observador iniciado. Esperando actividad...\n");
        }
    }

    @Override
    public void update() {
        String lastInsert = CursoController.getLastInsert();
        //System.out.println("update:" + lastInsert);
        if (logTextArea != null) logTextArea.appendText("Se insertó el Curso: " + lastInsert + "\n");
    }
}