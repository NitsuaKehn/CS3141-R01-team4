package com.team_software.cs3141_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
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
        //root.getChildren().add(btn);


        VBox messagesField = new VBox();//vbox to hold sent and received messages
        messagesField.setSpacing(10);
        messagesField.prefWidthProperty().bind(stage.widthProperty());//makes the vbox always as big as the stage
        messagesField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#26007A"), null, null)));//sets the background color



        ScrollPane scroller = new ScrollPane();//pane that allows scrolling
        scroller.fitToHeightProperty().set(true);//makes the scrollpane be the total vertical size
        scroller.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);//makes the side scrollbar show up
        scroller.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);//makes the bottom scroll bar hidden

        //file that holds the messages info
        File infile = new File("test_text_info.txt");

        //sets up the pane with all the messages in it
        displayText(infile, messagesField);

        //adds the vbox with the messages to the scroller
        scroller.setContent(messagesField);

        stage.setScene(new Scene(scroller, 300, 250));
        stage.show();
    }

    //gets the messages from the text file and adds them to the feild
    public void displayText(File file, Pane root)
    {
        //opens new scanner
        try(Scanner in = new Scanner(file))
        {
            //loops until file is empty
            while(in.hasNext()) {

                //stores whether the message should be left aligned
                Boolean leftAlign;

                //checks whether there is an R(received) at the beginning of the line
                if (in.next().equals("R")) {
                    leftAlign = true;
                }
                //means there is a S(sent)
                else
                {
                    leftAlign = false;
                }

                //creates temp label
                Label temp = new Label(in.nextLine());
                //adds padding so the scroll bar doesn't cover words
                temp.setPadding(new Insets(5,50,5,15));
                //adds the background
                temp.setBackground(new Background(new BackgroundFill(Paint.valueOf("#4B0082"), new CornerRadii(20), new Insets(0,45,0,10))));
                //sets the text color
                temp.setTextFill(Paint.valueOf("#006FFF"));

                //creates an hbox to store the label so we can set the alignment
                HBox hbox = new HBox();
                //adds the label to the hbox
                hbox.getChildren().add(temp);

                //checks the alignment type and set the appropriate one
                if(!leftAlign)
                {
                    hbox.setAlignment(Pos.BASELINE_RIGHT);
                }
                else
                {
                    hbox.setAlignment(Pos.BASELINE_LEFT);
                }

                //adds the hbox to the given pane
                root.getChildren().add(hbox);

            }

        }
        //file not found exception
        catch(FileNotFoundException e)
        {
            System.out.println("file not found");
            e.printStackTrace();

        }
    }
}