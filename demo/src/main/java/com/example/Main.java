package com.example;

import com.example.factory.ExternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {
        ExternalFactory eFactory = new ExternalFactory();

        View cli = eFactory.createCliConsola();
        View gui = eFactory.createGUI();

        Thread hilo1 = new Thread(cli::iniciar);
        hilo1.start();

        gui.iniciar();
    }
}