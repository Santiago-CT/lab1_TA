package com.example.factory;

import com.example.cli.MenuPrincipal;
import com.example.controllerFXML.SceneManager;
import com.example.services.View;

public class ExternalFactory {
    public View createGUI(){
        return new SceneManager();
    }

    public View createCliConsola(){
        return new MenuPrincipal();
    }
}
