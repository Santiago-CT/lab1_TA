package com.example.controllerFXML;

import com.example.services.View;

public class FXML_VIEW implements View {
    private SceneManager sceneManager;

    @Override
    public void iniciar(){
        sceneManager.launch();
    }
}
