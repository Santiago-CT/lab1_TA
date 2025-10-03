package com.example;

import com.example.database.DataBase;
import com.example.database.H2;
import com.example.factory.ExternalFactory;
import com.example.factory.InternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {

        ExternalFactory eFactory = ExternalFactory.getInstance();

        View gui = eFactory.createGUI();
        View cli = eFactory.createCliConsola();

        Thread hilo1 = new Thread(cli::iniciar);

        hilo1.start();
        gui.iniciar();
    }
}