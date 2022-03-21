package com.team_software.cs3141_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class CrezantUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Crezant");

        //creates the gridpane
        GridPane root = new GridPane();
        root.setGridLinesVisible(true);//use this for debugging/prototyping
        root.setHgap(5);
        root.setVgap(5);
        root.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color

        //makes and adds all of the cols to the gridpane with the appropriate spacing
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(75);
        root.getColumnConstraints().addAll(col1,col2);

        //makes and adds all of the rows to the gridpane with the appropriate spacing
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(5);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(80);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(15);
        root.getRowConstraints().addAll(row1,row2,row3);



        //Buttons
        Button optBtn = new Button("Options");
        Button newConversation = new Button("+");
        Button sendMessage = new Button("Send");
        GridPane.setHalignment(sendMessage, HPos.RIGHT);    //move send btn to right side
        GridPane.setHalignment(newConversation, HPos.RIGHT); //move + to right side

        //On screen text
        Text contactName = new Text("Contact Name");
        contactName.setStroke(Color.WHITE);



        //Text field
        TextField textField1 = new TextField("Enter text here...");
        textField1.setLayoutX(202.0);
        textField1.setLayoutY(652.0);
        textField1.setPrefSize(550,100);
        textField1.setPrefHeight(1000);
        textField1.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color
        textField1.setStyle("-fx-text-fill: white;");


        //makes the gridpane resize with window
        root.prefHeightProperty().bind(stage.heightProperty());
        root.prefWidthProperty().bind((stage.widthProperty()));
        root.setStyle("-fx-background-color: 3C3C3C"); //sets background to gray.


        root.add(optBtn, 0,0);
        root.add(newConversation, 0, 0);
        root.add(textField1, 1,2, 3, 1);
        root.add(sendMessage, 1, 2, 3, 1);

        //makes messages vbox
        VBox messagesField = new VBox();//vbox to hold sent and received messages
        messagesField.setSpacing(10);
        messagesField.prefWidthProperty().bind(stage.widthProperty());//makes the vbox always as big as the stage
        messagesField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color

        //makes contacts vbox
        VBox contactsField = new VBox();//vbox to hold sent and received messages
        contactsField.setSpacing(10);
        contactsField.prefWidthProperty().bind(stage.widthProperty());//makes the vbox always as big as the stage
        contactsField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));

        contactsField.getChildren().add(contactName);

        ScrollPane scroller = new ScrollPane();//pane that allows scrolling
        scroller.fitToHeightProperty().set(true);//makes the scrollpane be the total vertical size
        scroller.fitToWidthProperty().set(true);
        scroller.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);//makes the side scrollbar show up
        scroller.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);//makes the bottom scroll bar hidden

        //makes scroller for contacts
        ScrollPane contacts = new ScrollPane();
        contacts.fitToHeightProperty().set(true);//makes the scrollpane be the total vertical size
        contacts.fitToWidthProperty().set(true);
        contacts.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.ALWAYS);//makes the side scrollbar show up
        contacts.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);//makes the bottom scroll bar hidden
        contacts.setPrefWidth(100);

        //get convos
        ArrayList<Button> convoButton = new ArrayList<Button>();
        //get convo directory
        File directoryPath = new File("conversations");
        String contents[] = directoryPath.list();
        //for loop that reads through each file in a folder
        //and creates a button with the file name and its listener
        for(int i=0; i<contents.length; i++) {
            String fileName = contents[i];
            //create a simple button for now. spice things up later.
            convoButton.add(new Button());
            convoButton.get(i).setText(fileName.replace(".txt", "")); // set convo button name
            // action event
            convoButton.get(i).setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent actionEvent) {
                    //open conversation
                    //file that holds the messages info
                    File infile = new File("conversations/"+fileName);

                    //empty out the message field
                    messagesField.getChildren().clear();

                    //sets up the pane with all the messages in it
                    displayText(infile, messagesField);
                }
            });
            contactsField.getChildren().add(convoButton.get(i)); //add to ui
        }

        //adds the vbox with the messages to the scroller
        scroller.setContent(messagesField);
        contacts.setContent(contactsField);


        //add scrollpanes to the gridpane
        root.add(scroller,1,0,1,2);
        root.add(contacts, 0,1,1,3);

        Scene scene = new Scene(root, 750, 750); //Size of opened window.
        stage.setScene(scene);
        stage.show();


    }

    //gets the messages from the text file and adds them to the field
    public void displayText(File file, Pane root)
    {
        //opens new scanner
        try(Scanner in = new Scanner(file))
        {
            //loops until file is empty
            while(in.hasNext()) {

                //stores whether the message should be left aligned
                boolean leftAlign;

                //checks whether there is an R(received) at the beginning of the line
                //means there is a S(sent)
                leftAlign = in.next().equals("R");

                //creates temp label
                Label temp = new Label(in.nextLine());
                //adds padding so the scroll bar doesn't cover words
                temp.setPadding(new Insets(5,15,5,15));
                //adds the background
                temp.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FBFAF5"), new CornerRadii(20), new Insets(0,10,0,10))));
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

    public void createConversation()
    {
        //this method is for the + button to use.
        //should use a popup window.
    }

}