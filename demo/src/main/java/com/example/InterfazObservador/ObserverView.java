package com.example.InterfazObservador;

import com.example.controllerFXML.Automatizacion;
import com.example.controllerFXML.NotificadorController;
import com.example.model.Curso;
import com.example.services.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ObserverView implements View {

    public static NotificadorController notificadorGlobal;

    @Override
    public void iniciar() {
        Platform.runLater(() -> {
            try {
                crearVentanaNotificadorUnica();

                for (Curso curso : Automatizacion.cursosObservables) {
                    curso.agregarObservador(notificadorGlobal);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void crearVentanaNotificadorUnica() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NotificadorView.fxml"));
        Parent root = loader.load();

        notificadorGlobal = loader.getController();
        notificadorGlobal.inicializar();

        Stage stage = new Stage();
        stage.setTitle("Monitor de Cursos");
        stage.setScene(new Scene(root));
        stage.show();
    }
}