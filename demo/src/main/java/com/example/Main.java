package com.example;

import com.example.factory.ExternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {

        ExternalFactory eFactory = new ExternalFactory();

        View consola = eFactory.createCliConsola();
        Thread consoleThread = new Thread(consola::iniciar);
        consoleThread.start();

        View gui = eFactory.createGUI();
        gui.iniciar();
    }
}