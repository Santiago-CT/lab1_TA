package com.example;

import com.example.models.Automatizacion;

public class Main {

    public static void main(String[] args) {
        try {
            Automatizacion.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
