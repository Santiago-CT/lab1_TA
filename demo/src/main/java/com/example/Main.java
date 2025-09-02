package com.example;

import com.example.controller.Automatizacion;
import com.example.controller.SceneManager;
import com.example.dao.FacultadDao;
import com.example.model.Facultad;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

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