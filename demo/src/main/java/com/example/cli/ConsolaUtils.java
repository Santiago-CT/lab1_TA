package com.example.cli;

import java.util.Scanner;

public final class ConsolaUtils {

    private ConsolaUtils() {}

    public static void limpiarPantalla() {
        try {
            String sistemaOperativo = System.getProperty("os.name");
            if (sistemaOperativo.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static void presionarEnterParaContinuar(Scanner scanner) {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}