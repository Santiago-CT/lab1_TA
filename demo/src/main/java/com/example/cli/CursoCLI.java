package com.example.cli;

import com.example.DTO.CursoDTO;
import com.example.DTO.ProgramaDTO;
import com.example.controller.CursoController;
import com.example.controller.ProgramaController;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class CursoCLI {
    private final CursoController cursoController = new CursoController();
    private final ProgramaController programaController = new ProgramaController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("             📖 GESTIÓN DE CURSOS             ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar cursos");
            System.out.println("2. Agregar curso");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> listarCursos();
                    case 2 -> agregarCurso();
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

    private void listarCursos() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("             📋 LISTA DE CURSOS              ");
        System.out.println("---------------------------------------------");
        try {
            List<CursoDTO> lista = cursoController.getAll();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay cursos registrados.");
            } else {
                for (CursoDTO curso : lista) {
                    System.out.println("ID: " + curso.getID() + ", Nombre: " + curso.getNombre() + ", Programa: " + curso.getNombrePrograma());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarCurso() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("           ➕ AGREGAR NUEVO CURSO           ");
        System.out.println("---------------------------------------------");
        try {
            System.out.print("ID del curso: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Nombre del curso: ");
            String nombre = scanner.nextLine();

            System.out.println("\n--- Programas Disponibles ---");
            List<ProgramaDTO> programas = programaController.get();
            for (int i = 0; i < programas.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, programas.get(i).getNombre());
            }
            System.out.print("Elige el programa (número): ");
            int opcionPrograma = scanner.nextInt();
            scanner.nextLine();
            ProgramaDTO programaSel = programas.get(opcionPrograma - 1);

            System.out.print("¿Está activo? (s/n): ");
            boolean activo = scanner.nextLine().equalsIgnoreCase("s");

            CursoDTO curso = new CursoDTO(id, nombre, programaSel.getID(), programaSel.getNombre(), activo);
            if (cursoController.insert(curso)) {
                System.out.println("\n✅ Curso agregado con éxito.");
            } else {
                System.out.println("\n❌ No se pudo agregar el curso.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}