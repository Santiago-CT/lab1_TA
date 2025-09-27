package com.example.cli;

import com.example.controller.EstudianteController;
import com.example.controller.ProgramaController;

import java.util.Scanner;

public class EstudianteCLI {
    private final EstudianteController estudianteController = new EstudianteController();
    private final ProgramaController programaController = new ProgramaController();
    private final Scanner scanner = new Scanner(System.in);

    public EstudianteCLI(EstudianteController estudianteController) {
    }

    public void menu() {
        int opcion;
        do {
            System.out.println("\n=== GESTIÓN DE ESTUDIANTES ===");
            System.out.println("1. Listar estudiantes");
            System.out.println("2. Agregar estudiante");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1 -> listarEstudiantes();
                case 2 -> agregarEstudiante();
                case 0 -> System.out.println("Regresando al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void listarEstudiantes() {
        try {
            var lista = estudianteController.getAll();
            if (lista.isEmpty()) {
                System.out.println("No hay estudiantes registrados.");
            } else {
                lista.forEach(est ->
                        System.out.println(estudianteController.generarDetallesEstudiante(est))
                );
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void agregarEstudiante() {
        try {
            System.out.print("Código: ");
            double codigo = scanner.nextDouble();
            scanner.nextLine(); // limpiar buffer

            System.out.print("Nombres: ");
            String nombres = scanner.nextLine();

            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            // Listar programas disponibles
            System.out.println("\n--- Programas Disponibles ---");
            var programas = programaController.get();
            for (int i = 0; i < programas.size(); i++) {
                Object prog = programas.get(i);
                //System.out.printf("%d. %s%n", i + 1, estudianteController.obtenerNombrePrograma(prog));
            }
            System.out.print("Elige el programa (número): ");
            int opcionPrograma = scanner.nextInt();
            scanner.nextLine();

            Object programaSeleccionado = programas.get(opcionPrograma - 1);

            System.out.print("¿Activo? (s/n): ");
            String activoStr = scanner.nextLine();
            boolean activo = activoStr.equalsIgnoreCase("s");

            System.out.print("Promedio: ");
            double promedio = scanner.nextDouble();

            // Crear estudiante a través del controller
            /*Object estudiante = estudianteController.crearEstudiante(
                    codigo, nombres, apellidos, email, programaSeleccionado, activo, promedio
            );

            if (estudiante == null) {
                System.out.println("❌ No se pudo crear el estudiante (programa inválido).");
                return;
            }

            // Insertar estudiante
            boolean exito = estudianteController.insert(estudiante);

            if (exito) {
                System.out.println("✅ Estudiante agregado con éxito.");
            } else {
                System.out.println("❌ No se pudo agregar el estudiante.");
            }*/
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
