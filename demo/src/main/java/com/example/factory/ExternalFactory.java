package com.example.factory;

import com.example.cli.MenuPrincipal;
import com.example.controllerFXML.SceneManager;
import com.example.services.View;

public class ExternalFactory {
    private static ExternalFactory instance;
    private ExternalFactory(){}
    public static ExternalFactory getInstance(){
        //System.out.println(instance);
        if(instance == null) return instance = new ExternalFactory();
        //System.out.println(instance);
        return instance;
    }
    public View createGUI(){
        return new SceneManager();
    }

    public View createCliConsola(){
        return new MenuPrincipal();
    }
}
