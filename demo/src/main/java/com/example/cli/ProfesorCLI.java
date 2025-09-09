package com.example.cli;

import com.example.controller.ProfesorController;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProfesorCLI {

    private final ProfesorController profesorController;
    private final Scanner scanner;
    public ProfesorCLI(ProfesorController profesorController) {
        this.profesorController = profesorController;
        this.scanner = new Scanner(System.in);
    }
    public ProfesorCLI() {
        this.profesorController = new ProfesorController();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcion = -1;

        do {
            System.out.println("\n=== GESTIÓN DE PROFESORES ===");
            System.out.println("1. Listar profesores");
            System.out.println("2. Agregar profesor");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1 -> listarProfesores();
                    case 2-> agregarProfesor();
                    case 0 -> System.out.println("🔙 Volviendo al menú principal...");
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingresa un número válido");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void listarProfesores() {
        try {
            var lista = profesorController.obtenerListaProfesores();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay profesores registrados.");
            } else {
                lista.forEach(prof ->
                        System.out.println(profesorController.generarDetallesProfesor(prof))
                );
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar profesores: " + e.getMessage());
        }
    }



    private void agregarProfesor() {
        try {
            System.out.print("Nombres: ");
            String nombres = scanner.nextLine();

            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Tipo de contrato: ");
            String tipoContrato = scanner.nextLine();

            boolean insertado = profesorController.insertarProfesor(nombres, apellidos, email, tipoContrato);

            if (insertado) {
                System.out.println("✅ Profesor agregado exitosamente.");
            } else {
                System.out.println("⚠️ No se pudo agregar al profesor.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al agregar profesor: " + e.getMessage());
        }
    }


}
