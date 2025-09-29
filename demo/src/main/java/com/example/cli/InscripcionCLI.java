package com.example.cli;

import com.example.DTO.CursoDTO;
import com.example.DTO.EstudianteDTO;
import com.example.DTO.InscripcionDTO;
import com.example.controller.CursoController;
import com.example.controller.EstudianteController;
import com.example.controller.InscripcionController;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class InscripcionCLI {
    private final InscripcionController inscripcionController = new InscripcionController();
    private final EstudianteController estudianteController = new EstudianteController();
    private final CursoController cursoController = new CursoController();
    private final Scanner scanner = new Scanner(System.in);

    public void menu() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("---------------------------------------------");
            System.out.println("         📝 GESTIÓN DE INSCRIPCIONES         ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar inscripciones");
            System.out.println("2. Realizar una inscripción");
            System.out.println("0. Volver al Menú Principal");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1 -> listarInscripciones();
                    case 2 -> agregarInscripcion();
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

    private void listarInscripciones() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("         📋 LISTA DE INSCRIPCIONES           ");
        System.out.println("---------------------------------------------");
        try {
            List<InscripcionDTO> lista = inscripcionController.getAll();
            if (lista.isEmpty()) {
                System.out.println("⚠️ No hay inscripciones registradas.");
            } else {
                for (InscripcionDTO ins : lista) {
                    System.out.println("Estudiante: " + ins.getNombreEstudiante() + " -> Curso: " + ins.getNombreCurso() + " [Año: " + ins.getAnio() + ", Semestre: " + ins.getSemestre() + "]");
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }

    private void agregarInscripcion() {
        ConsolaUtils.limpiarPantalla();
        System.out.println("---------------------------------------------");
        System.out.println("       ➕ AGREGAR NUEVA INSCRIPCIÓN         ");
        System.out.println("---------------------------------------------");
        try {
            System.out.println("\n--- Seleccione un Estudiante ---");
            List<EstudianteDTO> estudiantes = estudianteController.getAll();
            for (int i = 0; i < estudiantes.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, estudiantes.get(i).getNombres() + " " + estudiantes.get(i).getApellidos());
            }
            System.out.print("Elige el estudiante (número): ");
            int opcionEstudiante = scanner.nextInt();
            scanner.nextLine();
            EstudianteDTO estudianteSel = estudiantes.get(opcionEstudiante - 1);

            System.out.println("\n--- Seleccione un Curso ---");
            List<CursoDTO> cursos = cursoController.getAll();
            for (int i = 0; i < cursos.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, cursos.get(i).getNombre());
            }
            System.out.print("Elige el curso (número): ");
            int opcionCurso = scanner.nextInt();
            scanner.nextLine();
            CursoDTO cursoSel = cursos.get(opcionCurso - 1);

            System.out.print("Año de la inscripción: ");
            int anio = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Semestre de la inscripción: ");
            int semestre = scanner.nextInt();
            scanner.nextLine();

            InscripcionDTO inscripcion = new InscripcionDTO(estudianteSel.getID(), estudianteSel.getNombres(), cursoSel.getID(), cursoSel.getNombre(), anio, semestre);
            if (inscripcionController.insert(inscripcion)) {
                System.out.println("\n✅ Inscripción realizada con éxito.");
            } else {
                System.out.println("\n❌ No se pudo realizar la inscripción.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        ConsolaUtils.presionarEnterParaContinuar(scanner);
    }
}