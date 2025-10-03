package com.example.cli;

import com.example.dataTransfer.FacultadDTO;
import com.example.dataTransfer.ProgramaDTO;
import com.example.controller.FacultadController;
import com.example.controller.ProgramaController;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ProgramaCLI {
    private final ProgramaController programaController = ProgramaController.getInstance();
    private final FacultadController facultadController = FacultadController.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("      📚 GESTIÓN DE PROGRAMAS ACADÉMICOS      ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar programas");
            System.out.println("2. Agregar programa");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> listarProgramas();
                    case 2 -> agregarPrograma();
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

    private void listarProgramas() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("           📋 LISTA DE PROGRAMAS             ");
        System.out.println("---------------------------------------------");
        try {
            List<ProgramaDTO> lista = programaController.get();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay programas registrados.");
            } else {
                for (ProgramaDTO prog : lista) {
                    System.out.println("ID: " + prog.getID() + ", Nombre: " + prog.getNombre() + ", Facultad: " + prog.getNombreFacultad());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarPrograma() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("         ➕ AGREGAR NUEVO PROGRAMA          ");
        System.out.println("---------------------------------------------");
        try {
            System.out.print("ID: ");
            double id = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Nombre del programa: ");
            String nombre = scanner.nextLine();
            System.out.print("Duración (semestres): ");
            double duracion = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("\n--- Facultades Disponibles ---");
            List<FacultadDTO> facultades = facultadController.getAll();
            for (int i = 0; i < facultades.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, facultades.get(i).getNombre());
            }
            System.out.print("Elige la facultad (número): ");
            int opcionFacultad = scanner.nextInt();
            scanner.nextLine();
            FacultadDTO facultadSel = facultades.get(opcionFacultad - 1);

            ProgramaDTO programa = new ProgramaDTO(id, nombre, duracion, Date.valueOf(LocalDate.now()), facultadSel.getID(), facultadSel.getNombre());
            if (programaController.insert(programa)) {
                System.out.println("\n✅ Programa agregado con éxito.");
            } else {
                System.out.println("\n❌ No se pudo agregar el programa.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}