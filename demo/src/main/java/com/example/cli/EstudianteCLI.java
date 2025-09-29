package com.example.cli;

import com.example.DTO.EstudianteDTO;
import com.example.DTO.ProgramaDTO;
import com.example.controller.EstudianteController;
import com.example.controller.ProgramaController;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class EstudianteCLI {
    private final EstudianteController estudianteController = new EstudianteController();
    private final ProgramaController programaController = new ProgramaController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("           👨‍🎓 GESTIÓN DE ESTUDIANTES          ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar estudiantes");
            System.out.println("2. Agregar estudiante");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1 -> listarEstudiantes();
                    case 2 -> agregarEstudiante();
                    case 0 -> {}
                    default -> {
                        System.out.println("❌ Opción no válida.");
                        ConsolaUtils.presionarEnterParaContinuar(scanner);
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("❌ Error: Ingresa un número válido.");
                scanner.nextLine();
                ConsolaUtils.presionarEnterParaContinuar(scanner);
            }
        } while (opcion != 0);
    }

    private void listarEstudiantes() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("           📋 LISTA DE ESTUDIANTES           ");
        System.out.println("---------------------------------------------");
        try {
            List<EstudianteDTO> lista = estudianteController.getAll();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay estudiantes registrados.");
            } else {
                for (EstudianteDTO est : lista) {
                    System.out.println("ID: " + est.getID() + ", Nombre: " + est.getNombres() + " " + est.getApellidos() + ", Programa: " + est.getNombrePrograma());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarEstudiante() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("        ➕ AGREGAR NUEVO ESTUDIANTE         ");
        System.out.println("---------------------------------------------");
        try {
            System.out.print("ID: ");
            double id = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Código: ");
            double codigo = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.println("\n--- Programas Disponibles ---");
            List<ProgramaDTO> programas = programaController.get();
            for (int i = 0; i < programas.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, programas.get(i).getNombre());
            }
            System.out.print("Elige el programa (número): ");
            int opcionPrograma = scanner.nextInt();
            scanner.nextLine();
            ProgramaDTO programaSel = programas.get(opcionPrograma - 1);

            System.out.print("¿Activo? (s/n): ");
            boolean activo = scanner.nextLine().equalsIgnoreCase("s");
            System.out.print("Promedio: ");
            double promedio = scanner.nextDouble();
            scanner.nextLine();

            EstudianteDTO estudiante = new EstudianteDTO(id, nombres, apellidos, email, codigo, programaSel.getID(), programaSel.getNombre(), activo, promedio);
            if (estudianteController.insert(estudiante)) {
                System.out.println("\n✅ Estudiante agregado con éxito.");
            } else {
                System.out.println("\n❌ No se pudo agregar el estudiante.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en el ingreso de datos: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}