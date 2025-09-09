package com.example;

import com.example.cli.MenuPrincipal;
import com.example.controllerFXML.Automatizacion;

public class MainCLI {

    public static void main(String[] args) {
        Automatizacion.run();

        // Iniciar menú principal en consola
        new MenuPrincipal().iniciar();
    }
}
