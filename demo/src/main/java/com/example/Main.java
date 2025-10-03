package com.example;

import com.example.controllerFXML.SceneManager;
import com.example.factory.ExternalFactory;
import com.example.services.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ExternalFactory eFactory = new ExternalFactory();

        View consola = eFactory.createCliConsola();
        new Thread(consola::iniciar).start();

        View observador = eFactory.createObserverGUI();
        observador.iniciar();

        SceneManager gui = eFactory.createGUI();
        gui.start(primaryStage);
    }
}