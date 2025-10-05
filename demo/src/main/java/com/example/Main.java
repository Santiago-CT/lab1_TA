package com.example;

import com.example.controllerFXML.Automatizacion;
import com.example.factory.ExternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {
        Automatizacion automatizacion = new Automatizacion();
        automatizacion.run();

        ExternalFactory eFactory = ExternalFactory.getInstance();

        View gui = eFactory.createGUI();
        View cli = eFactory.createCliConsola();

        Thread hilo1 = new Thread(cli::iniciar);

        hilo1.start();
        gui.iniciar();

    }
}