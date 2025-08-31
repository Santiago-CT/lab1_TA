/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.controller;

import com.example.Main;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 *
 * @author gon
 */
public class SceneManager {
    
    private static Scene scene;

    public static void showMainView(Stage primaryStage) throws IOException{
        scene = new Scene(loadFXML("persona"), 800, 600);
        
        primaryStage.setTitle("SISTEMA DE GESTION DE LA UNIVERSIDAD");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
