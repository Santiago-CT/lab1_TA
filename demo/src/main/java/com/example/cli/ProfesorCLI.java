package com.example.cli;

import com.example.controller.ProfesorController;
import com.example.model.Profesor;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ProfesorCLI {

    private final ProfesorController profesorController;
    private final Scanner scanner;

    public ProfesorCLI() {
        this.profesorController = new ProfesorController();
        this.scanner = new Scanner(System.in);
    }

    public void menu() {
        int opcion = -1;

        do {
            System.out.println("\n=== GESTIÓN DE PROFESORES ===");
            System.out.println("1. Listar profesores");
            System.out.println("2. Buscar profesor por ID");
            System.out.println("3. Agregar profesor");
            System.out.println("4. Eliminar profesor");
            System.out.println("0. Volver");
            System.out.print("Elige una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1 -> listarProfesores();
                    case 2 -> buscarProfesorPorId();
                    case 3 -> agregarProfesor();
                    case 4 -> eliminarProfesor();
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
                System.out.println("\n📋 Lista de Profesores:");
                lista.forEach(prof -> {
                    Profesor p = (Profesor) prof;
                    System.out.println("ID: " + p.getID() +
                            " | Nombre: " + p.getNombres() + " " + p.getApellidos() +
                            " | Email: " + p.getEmail() +
                            " | Contrato: " + p.getTipoContrato());
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Error al listar profesores: " + e.getMessage());
        }
    }

    private void buscarProfesorPorId() {
        try {
            System.out.print("Ingrese el ID del profesor: ");
            double id = scanner.nextDouble();
            scanner.nextLine();

            if (profesorController.existeProfesor(id)) {
                System.out.println("✅ Profesor con ID " + id + " encontrado en el sistema.");
            } else {
                System.out.println("⚠️ No existe un profesor con ese ID.");
            }

        } catch (InputMismatchException e) {
            System.out.println("❌ Error: ID inválido");
            scanner.nextLine();
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

    private void eliminarProfesor() {
        try {
            System.out.print("Ingrese el ID del profesor a eliminar: ");
            double id = scanner.nextDouble();
            scanner.nextLine();

            if (profesorController.existeProfesor(id)) {
                System.out.println("✅ Profesor eliminado con éxito (simulación, implementar en DAO si es necesario).");
                // Aquí deberías implementar ProfesorDAO.delete(id)
            } else {
                System.out.println("⚠️ No existe un profesor con ese ID.");
            }

        } catch (InputMismatchException e) {
            System.out.println("❌ Error: ID inválido");
            scanner.nextLine();
        }
    }
}
