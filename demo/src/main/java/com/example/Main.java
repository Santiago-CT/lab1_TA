package com.example;

import com.example.cli.ConsolaUtils;
import com.example.factory.ExternalFactory;
import com.example.services.View;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConsolaUtils.limpiarPantalla();
        System.out.println("=============================================");
        System.out.println("  🚀 BIENVENIDO AL SISTEMA UNIVERSITARIO 🚀");
        System.out.println("=============================================");
        System.out.println("Por favor, seleccione la base de datos a utilizar:");
        System.out.println("  1. H2 (En memoria, para pruebas rápidas)");
        System.out.println("  2. MySQL");
        System.out.println("  3. Oracle");
        System.out.println("---------------------------------------------");
        System.out.print("Seleccione una opción: ");

        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Opción inválida, se usará H2 por defecto.");
            choice = 1;
        }
        scanner.nextLine();

        String selectedDb = switch (choice) {
            case 2 -> "MYSQL";
            case 3 -> "ORACLE";
            default -> "H2";
        };

        System.setProperty("DB_TYPE", selectedDb);

        ExternalFactory eFactory = new ExternalFactory();
        View cli = eFactory.createCliConsola();
        View gui = eFactory.createGUI();

        Thread hilo1 = new Thread(cli::iniciar);
        hilo1.start();

        gui.iniciar();
    }
}