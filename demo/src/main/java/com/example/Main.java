package com.example;

import com.example.factory.ExternalFactory;
import com.example.services.View;

public class Main {

    public static void main(String[] args) {

        ExternalFactory eFactory = new ExternalFactory();
        View gui = eFactory.createGUI();
        gui.iniciar();
    }
}