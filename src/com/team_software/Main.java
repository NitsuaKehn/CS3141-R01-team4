package com.team_software;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        //Launches window
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Creates the main window titled "Hello World!"
        primaryStage.setTitle("Hello World!");

        //Creates a button titled "Say 'Hello World!"
        //Button will then print "Hello World!" in stdout
        Button btn = new Button();
        btn.setText("Say 'Hello World!'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        //Add button to window positioned at v:300 and v1:250
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
