package com.example.cli;

import com.example.services.View;
import java.util.Scanner;
import java.util.InputMismatchException;

public class MenuPrincipal implements View {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void iniciar() {
        int opcion = -1;
        do {
            ConsolaUtils.limpiarPantalla();
            System.out.println("=============================================");
            System.out.println("   🎓 SISTEMA DE GESTIÓN UNIVERSITARIA 🎓");
            System.out.println("=============================================");
            System.out.println("                MENÚ PRINCIPAL               ");
            System.out.println("---------------------------------------------");
            System.out.println("1. Gestionar Estudiantes");
            System.out.println("2. Gestionar Profesores");
            System.out.println("3. Gestionar Programas Académicos");
            System.out.println("4. Gestionar Facultades");
            System.out.println("5. Gestionar Cursos");
            System.out.println("6. Gestionar Inscripciones");
            System.out.println("0. Salir de la Aplicación");
            System.out.println("---------------------------------------------");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1: new EstudianteCLI().menu(); break;
                    case 2: new ProfesorCLI().menu(); break;
                    case 3: new ProgramaCLI().menu(); break;
                    case 4: new FacultadCLI().menu(); break;
                    case 5: new CursoCLI().menu(); break;
                    case 6: new InscripcionCLI().menu(); break;
                    case 0: System.out.println("\n✅ Saliendo del sistema. ¡Hasta pronto!"); break;
                    default:
                        System.out.println("\n❌ Opción no válida. Intente de nuevo.");
                        ConsolaUtils.presionarEnterParaContinuar(scanner);
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\n❌ Error: Debe ingresar un número.");
                scanner.nextLine();
                ConsolaUtils.presionarEnterParaContinuar(scanner);
                opcion = -1;
            }
        } while (opcion != 0);
    }
}