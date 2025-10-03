package com.example.controllerFXML;

import com.example.model.Curso;
import com.example.model.Observador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NotificadorController implements Observador {

    @FXML
    private TextArea logTextArea;

    public void inicializar() {
        logTextArea.appendText("Monitor iniciado. Esperando actividad...\n");
    }

    @Override
    public void actualizar(Curso curso) {
        Platform.runLater(() -> {
            String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String estado = curso.isActivo() ? "Activo" : "Inactivo";
            String log = String.format("[%s] CAMBIO: El curso '%s' (ID: %d) ahora está %s.\n",
                    hora, curso.getNombre(), curso.getID(), estado);

            logTextArea.appendText(log);
        });
    }
}