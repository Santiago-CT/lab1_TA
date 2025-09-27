package com.example.cli;

import java.util.Scanner;
import com.example.controller.EstudianteController;
import com.example.controller.ProfesorController;
import com.example.services.View;

public class MenuPrincipal implements View {
    private final Scanner scanner = new Scanner(System.in);
    private final EstudianteController estudianteController = new EstudianteController();
    private final ProfesorController profesorController = new ProfesorController();

    @Override
    public void iniciar() {
        int opcion;
        do {
            System.out.println("=== MENÚ PRINCIPAL ===");
            System.out.println("1. Gestionar estudiantes");
            System.out.println("2. Gestionar profesores");
            System.out.println("3. Gestionar programas");
            System.out.println("4. Gestionar facultades");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    new EstudianteCLI(estudianteController).menu();
                    break;
                case 2:
                    new ProfesorCLI(profesorController).menu();
                    break;
            }
        } while (opcion != 0);
    }
}
