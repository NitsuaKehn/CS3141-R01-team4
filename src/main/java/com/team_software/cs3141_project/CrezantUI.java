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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class CrezantUI extends Application {
    private String currentConvoFileName = "";
    private Client client = new Client();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        client.startListener();
        //client.startUp("");

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
        //Button optBtn = new Button("Options");
        Button sendMessageBtn = new Button("Send");
        GridPane.setHalignment(sendMessageBtn, HPos.RIGHT);    //move send btn to right side


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


        //root.add(optBtn, 0,0);
        root.add(textField1, 1,2, 3, 1);
        root.add(sendMessageBtn, 1, 2, 3, 1);

        //makes messages vbox
        VBox messagesField = new VBox();//vbox to hold sent and received messages
        messagesField.setSpacing(10);
        messagesField.prefWidthProperty().bind(stage.widthProperty());//makes the vbox always as big as the stage
        messagesField.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color

        //makes contacts vbox
        VBox contactsField = new VBox();//vbox to hold conversations
        contactsField.setSpacing(3);
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

        //Set button fonts
        //optBtn.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        sendMessageBtn.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));

        //get convos
        ArrayList<Button> convoButton = new ArrayList<Button>();
        //get convo directory
        File directoryPath = new File("conversations");
        String contents[] = directoryPath.list();

        //for loop that reads through each file in a folder
        //and creates a button with the file name and its listener
        for(int i=0; i<contents.length; i++) {
            String fileName = contents[i];
            //create a cool button
            createConvoButton(stage, contactsField, messagesField, convoButton, fileName);
        }
        //pop for convo button
        //create pop up
        Popup popup = new Popup();
        GridPane poppane = new GridPane();
        poppane.setStyle(" -fx-background-color: grey;");
        //popup label
        Label label = new Label("Create New Conversation");
        label.setAlignment(Pos.TOP_CENTER);
        label.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 20));
        label.setStyle(" -fx-background-color: grey;");
        label.textFillProperty().set(Paint.valueOf("white"));
        label.setMinHeight(150);
        //textbox label
        Label userlabel = new Label("  User:  ");
        userlabel.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        userlabel.textFillProperty().set(Paint.valueOf("white"));
        userlabel.setMinHeight(150);
        Label paddingLabel = new Label("              ");
        //error label
        Label errorLabel = new Label("error");
        errorLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 18));
        errorLabel.textFillProperty().set(Paint.valueOf("red"));
        if (!popup.isShowing()) //trigger
            errorLabel.setVisible(false);

        TextField newContactName = new TextField();
        //newContactName.setAlignment(Pos.CENTER);
        newContactName.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        newContactName.setMaxWidth(250);
        newContactName.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                createConversation(stage, contactsField, messagesField, errorLabel, convoButton, newContactName.getCharacters());
            }
        });

        poppane.add(label, 1,0);
        poppane.add(userlabel, 0,0);
        poppane.add(paddingLabel, 2,0);
        poppane.add(errorLabel, 1,1);
        poppane.add(newContactName, 1,0);
        popup.getContent().add(poppane);
        popup.setAutoHide(true);
        //add convo button
        Button newConversation = new Button("New Conversation");
        GridPane.setHalignment(newConversation, HPos.RIGHT); //move + to right side
        newConversation.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        newConversation.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if (!popup.isShowing())
                    popup.show(stage);
            }
        });

        //sendMessageBtn updates the messages field with the text put in the textfield
        sendMessageBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if (textField1.getText().equals("") || currentConvoFileName.equals("")) {
                    //Do nothing
                }
                else {
                    //Run sendMessage()
                    try {
                        sendMessage(textField1.getText(), currentConvoFileName, messagesField);
                        textField1.clear();
                    } catch (IOException e) {
                        System.out.println("ERROR: Could not send message");
                        e.printStackTrace();
                    }
                }
            }
        });

        //When you press the enter key in the textfield, it'll do the same thing as
        //clicking on the send button
        textField1.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    if (textField1.getText().equals("") || currentConvoFileName.equals("")) {
                        //Do nothing
                    }
                    else {
                        //Run sendMessage()
                        try {
                            sendMessage(textField1.getText(), currentConvoFileName, messagesField);
                            textField1.clear();
                        } catch (IOException e) {
                            System.out.println("ERROR: Could not send message");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        root.add(newConversation, 0, 0);

        //adds the vbox with the messages to the scroller
        scroller.setContent(messagesField);
        contacts.setContent(contactsField);


        //add scrollpanes to the gridpane
        root.add(scroller,1,0,1,2);
        root.add(contacts, 0,1,1,3);

        Scene scene = new Scene(root, 750, 750); //Size of opened window.

        stage.getIcons().add(new Image("file:src/main/resources/logo/CrezantLogo.png"));
        stage.setMinWidth(250);
        stage.setMinHeight(150);
        stage.setScene(scene);
        stage.show();


    }

    /**
     * When user clicks sends or presses enter in text field,
     * message is written to the specified text file
     * Then call displayText to update texts in the field
     * Then call method from client to send message over network?
     * @param message
     * @param fileName
     * @param root
     * @throws IOException
     */
    public void sendMessage(String message, String fileName, Pane root) throws IOException
    {
        String peerIP = "";

        //Write message to file
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file, true);
        Scanner fileIn = new Scanner(file);

        if (fileIn.hasNextLine()) {
            peerIP = fileIn.nextLine();
        }

        writer.append("\nS " + message);
        writer.close();

        root.getChildren().clear();

        //Update texts with display text
        displayText(file, root);

        //Call Client's sendMessage to send msg to peer
        client.sendMessage(peerIP, message);

    }

    /**
     * Gets the messages from the text file and adds them to the field
     * @param file
     * @param root
     */
    public void displayText(File file, Pane root)
    {
        //opens new scanner
        try(Scanner in = new Scanner(file))
        {

            //skips first line that holds the ip of that peer
            in.nextLine();

            //loops until file is empty
            while(in.hasNext()) {

                //stores whether the message should be left aligned
                boolean leftAlign;

                //checks whether there is an R(received) at the beginning of the line
                //means there is a S(sent)
                leftAlign = in.next().equals("R");

                //creates temp label
                Label temp = new Label(in.nextLine());
                //wraps the text in the label
                temp.setWrapText(true);
                //adds padding so the scroll bar doesn't cover words
                temp.setPadding(new Insets(5,15,5,15));
                //set the font to ubuntu-medium
                //temp.setFont(Font.font("Comic Sans MS", FontWeight.SEMI_BOLD, 12));
                temp.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
                //sets the text color
                temp.setTextFill(Paint.valueOf("#1f1f1f"));


                //creates an hbox to store the label so we can set the alignment
                HBox hbox = new HBox();
                //adds the label to the hbox
                hbox.getChildren().add(temp);

                //checks the alignment type and set the appropriate one
                if(!leftAlign)
                {
                    hbox.setAlignment(Pos.BASELINE_RIGHT);
                    temp.setBackground(new Background(new BackgroundFill(Paint.valueOf("#e0e0e0"), new CornerRadii(20), new Insets(0,10,0,10))));
                }
                else
                {
                    hbox.setAlignment(Pos.BASELINE_LEFT);
                    temp.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bea8e0"), new CornerRadii(20), new Insets(0,10,0,10))));

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

    public void createConvoButton(Stage stage, VBox contactsField, VBox messagesField, ArrayList<Button> convoButton, String fileName){
        Button convo = new Button();
        convo.setText(fileName.replace(".txt", "")); // set convo button name
        convo.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        convo.setTextAlignment(TextAlignment.CENTER);
        convo.textFillProperty().set(Paint.valueOf("white"));
        convo.setWrapText(true);
        convo.setMaxWidth(contactsField.getMaxWidth());
        convo.setMinHeight(contactsField.getMinWidth());
        convo.setPrefHeight(45);
        convo.prefWidthProperty().bind(stage.widthProperty());//makes the vbox always as big as the stage
        convo.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color
        convo.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                convo.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bea8e0"), null, null)));//sets the background color
            }
        });
        convo.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                convo.setBackground(new Background(new BackgroundFill(Paint.valueOf("#3C3C3C"), null, null)));//sets the background color
            }
        });

        // action event
        convo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                //open conversation
                //file that holds the messages info
                File infile = new File("conversations/"+fileName);

                //Update currently used convo file
                currentConvoFileName = "conversations/" + fileName;

                //empty out the message field
                messagesField.getChildren().clear();

                //sets up the pane with all the messages in it
                displayText(infile, messagesField);
            }
        });
        //create delete button for convo
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("DELETE");
        contextMenu.setStyle("-fx-background-color: grey; -fx-font-weight: bold; -fx-font-size: 13px;");
        delete.setStyle("-fx-text-fill: white;");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                File infile = new File("conversations/"+fileName);
                infile.delete(); //delete file
                convoButton.remove(convo); // remove from arraylist
                contactsField.getChildren().remove(convo); //remove from ui
            }
        });
        contextMenu.getItems().add(delete);
        convo.setContextMenu(contextMenu);

        //add convo to UI
        convoButton.add(convo);
        contactsField.getChildren().add(convo); //add to ui
    }

    public void createConversation(Stage stage, VBox contactsField, VBox messagesField, Label errorLabel, ArrayList<Button> convoButton, CharSequence userName)
    {
        //get as string
        String newFileName = userName.toString();
        //check if user exists on server
        String ip = client.getIP(newFileName);
        if (!ip.equals("null")){
            //check if convo already exists
            try {
                // Get the file
                File infile = new File("conversations/"+newFileName+".txt");
                if (infile.createNewFile()) {
                    //file created
                    FileWriter writer = new FileWriter(infile, true);
                    //get user ip address
                    writer.append(ip);
                    writer.close();
                    //empty out the message field
                    messagesField.getChildren().clear();
                    //sets up the pane with new file
                    displayText(infile, messagesField);
                    createConvoButton(stage, contactsField, messagesField, convoButton, newFileName + ".txt");
                }
                else {
                    errorLabel.setText("Conversation Already Exists!");
                    errorLabel.setVisible(true);
                }
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
        else{
            errorLabel.setText("User Does Not Exist!");
            errorLabel.setVisible(true);
        }
    }
}