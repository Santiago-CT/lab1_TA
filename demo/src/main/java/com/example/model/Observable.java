package com.example.model;

import java.util.ArrayList;
import java.util.List;

public class Observable {
    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    public void removerObservador(Observador o) {
        observadores.remove(o);
    }

    public void notificarObservadores(Curso curso) {
        for (Observador o : observadores) {
            o.actualizar(curso);
        }
    }
}