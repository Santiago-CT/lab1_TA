package com.example.cli;

import com.example.DTO.ProfesorDTO;
import com.example.controller.ProfesorController;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ProfesorCLI {
    private final ProfesorController profesorController = new ProfesorController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("           👨‍🏫 GESTIÓN DE PROFESORES          ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar profesores");
            System.out.println("2. Agregar profesor");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> listarProfesores();
                    case 2 -> agregarProfesor();
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

    private void listarProfesores() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("           📋 LISTA DE PROFESORES           ");
        System.out.println("---------------------------------------------");
        try {
            List<ProfesorDTO> lista = profesorController.getAll();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay profesores registrados.");
            } else {
                for (ProfesorDTO prof : lista) {
                    System.out.println("ID: " + prof.getID() + ", Nombre: " + prof.getNombres() + " " + prof.getApellidos() + ", Contrato: " + prof.getTipoContrato());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarProfesor() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("         ➕ AGREGAR NUEVO PROFESOR          ");
        System.out.println("---------------------------------------------");
        try {
            System.out.print("ID: ");
            double id = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Nombres: ");
            String nombres = scanner.nextLine();
            System.out.print("Apellidos: ");
            String apellidos = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Tipo de contrato: ");
            String tipoContrato = scanner.nextLine();

            ProfesorDTO profesor = new ProfesorDTO(id, nombres, apellidos, email, tipoContrato);
            if (profesorController.insert(profesor)) {
                System.out.println("\n✅ Profesor agregado con éxito.");
            } else {
                System.out.println("\n❌ No se pudo agregar al profesor.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en el ingreso de datos: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}