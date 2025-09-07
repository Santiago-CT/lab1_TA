package com.example;

import com.example.controllerFXML.Automatizacion;
import com.example.controllerFXML.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void init(){
        Automatizacion.run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Delegamos la carga de la vista a otra clase
        SceneManager.showMainView(stage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}