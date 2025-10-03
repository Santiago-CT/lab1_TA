package com.example;

import com.example.factory.ExternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {
        ExternalFactory eFactory = ExternalFactory.getInstance();

        View gui = eFactory.createGUI();
        View cli = eFactory.createCliConsola();
        View observador = eFactory.createObserverGUI();

        Thread hilo1 = new Thread(cli::iniciar);
      
        hilo1.start();
        gui.iniciar();
        observador.iniciar();

    }
}