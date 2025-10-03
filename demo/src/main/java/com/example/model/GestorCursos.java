package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorCursos {
    private static GestorCursos instancia;
    private final List<Curso> cursosObservables = new ArrayList<>();

    private GestorCursos() {}

    public static synchronized GestorCursos getInstancia() {
        if (instancia == null) {
            instancia = new GestorCursos();
        }
        return instancia;
    }

    public void agregarCurso(Curso curso) {
        if (cursosObservables.stream().noneMatch(c -> c.getID() == curso.getID())) {
            cursosObservables.add(curso);
        }
    }

    public List<Curso> getCursos() {
        return cursosObservables;
    }

    public Optional<Curso> getCursoPorId(int id) {
        return cursosObservables.stream()
                .filter(c -> c.getID() == id)
                .findFirst();
    }
}