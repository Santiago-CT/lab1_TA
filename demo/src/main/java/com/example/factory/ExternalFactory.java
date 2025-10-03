package com.example.factory;

import com.example.InterfazObservador.ObserverView;
import com.example.cli.MenuPrincipal;
import com.example.controllerFXML.SceneManager;
import com.example.services.View;

public class ExternalFactory {

    public SceneManager createGUI() {
        return new SceneManager();
    }

    public View createCliConsola() {
        return new MenuPrincipal();
    }

    public View createObserverGUI() {
        return new ObserverView();
    }
}