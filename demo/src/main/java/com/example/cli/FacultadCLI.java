package com.example.cli;

import com.example.dataTransfer.FacultadDTO;
import com.example.dataTransfer.PersonaDTO;
import com.example.controller.FacultadController;
import com.example.controller.ProfesorController;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class FacultadCLI {
    private final FacultadController facultadController = FacultadController.getInstance();
    private final ProfesorController profesorController = ProfesorController.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("           🏛️ GESTIÓN DE FACULTADES          ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar facultades");
            System.out.println("2. Agregar facultad");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> listarFacultades();
                    case 2 -> agregarFacultad();
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

    private void listarFacultades() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("           📋 LISTA DE FACULTADES            ");
        System.out.println("---------------------------------------------");
        try {
            List<FacultadDTO> lista = facultadController.getAll();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay facultades registradas.");
            } else {
                for (FacultadDTO fac : lista) {
                    System.out.println("ID: " + fac.getID() + ", Nombre: " + fac.getNombre() + ", Decano: " + fac.getNombreDecano());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarFacultad() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("         ➕ AGREGAR NUEVA FACULTAD          ");
        System.out.println("---------------------------------------------");
        try {
            System.out.print("ID: ");
            double id = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Nombre de la facultad: ");
            String nombre = scanner.nextLine();

            System.out.println("\n--- Profesores Disponibles para Decano ---");
            List<PersonaDTO> profesores = profesorController.getNombreProfesores();
            for (int i = 0; i < profesores.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, profesores.get(i).getNombres() + " " + profesores.get(i).getApellidos());
            }
            System.out.print("Elige el decano (número): ");
            int opcionDecano = scanner.nextInt();
            scanner.nextLine();
            PersonaDTO decanoSel = profesores.get(opcionDecano - 1);

            FacultadDTO facultad = new FacultadDTO(id, nombre, decanoSel.getID(), decanoSel.getNombres());
            if (facultadController.insert(facultad)) {
                System.out.println("\n✅ Facultad agregada con éxito.");
            } else {
                System.out.println("\n❌ No se pudo agregar la facultad.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}