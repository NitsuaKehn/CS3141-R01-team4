package com.team_software.cs3141_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class CrezantUI extends Application {
        public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Crezant");
        Button btn = new Button();
        btn.setText("Say 'Hello World!");
        btn.setOnAction(actionEvent -> System.out.println("Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
    }

    //gets the messages from the text file and adds them to the feild
    public void displayText(File file)
    {
        try(Scanner in = new Scanner(file))
        {

        }
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
            e.printStackTrace();

        }
    }
}