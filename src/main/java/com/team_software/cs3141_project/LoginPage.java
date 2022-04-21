package com.team_software.cs3141_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

import static javafx.application.Application.launch;

public class LoginPage extends Application{
    private Client client = new Client();
    public static void main(String[] args) {
        launch(args);
    }
    private Crypt magicMachine = null;

    public void start(Stage stage) throws Exception {
        //client.startListener();
        //client.startUp("");





        stage.setTitle("Crezant");
        GridPane root = new GridPane();
        //root.setGridLinesVisible(true);
        root.setVgap(5);

        File infile = new File("login.txt");

        TextField usernameField = new TextField();
        TextField passwordField = new TextField();
        usernameField.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        passwordField.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));

        Label errorLabel = new Label("Error");
        errorLabel.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 15));
        errorLabel.textFillProperty().set(Paint.valueOf("red"));
        errorLabel.setVisible(false);

        Label newuser = new Label("New User: enter \n new credentials!");
        newuser.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 15));
        newuser.textFillProperty().set(Paint.valueOf("white"));
        newuser.setVisible(false);

        //String buffer;

        boolean hastext = false;
        if(!infile.createNewFile()) {
            hastext = true;
        }


        if(!hastext) { //if username-pass file does not exists
            newuser.setVisible(true);
            passwordField.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent actionEvent) {
                    //create username and password

                    String givenUser = usernameField.getCharacters().toString();
                    String givenPass = passwordField.getCharacters().toString();

                    if(checkUsername(givenUser)) { // check if username taken
                        if (givenUser.length() != 0 && givenPass.length() != 0
                                && !givenUser.contains(" ") && !givenPass.contains(" ")) {//check if user name taken and field not blank no space in username
                            try {
                                byte[] salt = // for learning purposes
                                        { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                                                (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

                                PBEKeySpec keySpec = new PBEKeySpec(givenPass.toCharArray(), salt, 65536, 256);
                                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                                SecretKey tmp = factory.generateSecret(keySpec);
                                SecretKey key = new SecretKeySpec(tmp.getEncoded(),"AES");

                                magicMachine = new Crypt(key, "AES/CBC/PKCS5Padding");

                                String buffer = givenUser + " \n" + givenPass;

                                magicMachine.encrypt(buffer, "login.txt");


                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InvalidKeyException e) {
                                e.printStackTrace();
                            } catch (NoSuchPaddingException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (InvalidKeySpecException e) {
                                e.printStackTrace();
                            }
                            System.out.println("open UI!");
                            CrezantUI app = new CrezantUI(client, givenUser);
                            stage.close();
                            try {
                                app.start(stage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            errorLabel.setText("Username and password \nmust not be blank or \ncontain any spaces.");
                            errorLabel.setVisible(true);
                        }
                    }
                    else{
                        errorLabel.setText("Username taken!");
                        errorLabel.setVisible(true);
                    }
                }
            });
        }
        else{
            passwordField.setOnAction(new EventHandler<ActionEvent>() {


                public void handle(ActionEvent actionEvent) {

                    String buffer = null;

                    try
                    {
                        byte[] salt = // for learning purposes
                                {(byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
                                        (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};

                        PBEKeySpec keySpec = new PBEKeySpec(passwordField.getCharacters().toString().toCharArray(), salt, 65536, 256);
                        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                        SecretKey tmp = factory.generateSecret(keySpec);
                        SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");

                        magicMachine = new Crypt(key, "AES/CBC/PKCS5Padding");

                        buffer = magicMachine.decrypt("login.txt");
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }

                    //check username and password
                    try(Scanner in = new Scanner(buffer))
                    {

                        System.out.println(buffer);

                        String username = in.next();
                        String password = in.next();
                        if (usernameField.getCharacters().toString().equals(username)
                                && passwordField.getCharacters().toString().equals(password)) {//check if user name taken and field not blank
                            System.out.println("open UI!");
                            CrezantUI app = new CrezantUI(client, username);
                            stage.close();
                            app.start(stage);
                        }
                        else{
                            System.out.println("wrong info!");
                            errorLabel.setText("Username or password \ndoes not match!");
                            errorLabel.setVisible(true);
                            usernameField.clear();
                            passwordField.clear();
                        }
                        in.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //labels
        Label title = new Label("Crezant Login");
        title.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 20));
        title.setAlignment(Pos.CENTER);
        title.setMinHeight(60);
        title.textFillProperty().set(Paint.valueOf("white"));
        Label username = new Label("  Username: ");
        Label password = new Label("  Password: ");
        username.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        password.setFont(Font.loadFont("file:src/main/resources/fonts/Ubuntu-Medium.ttf", 13));
        username.textFillProperty().set(Paint.valueOf("white"));
        password.textFillProperty().set(Paint.valueOf("white"));

        root.add(title, 1, 0);
        root.add(username, 0, 1);
        root.add(password, 0, 2);
        root.add(errorLabel,1, 3);
        root.add(newuser, 1, 4);
        root.add(usernameField, 1, 1);
        root.add(passwordField, 1, 2);
        root.setStyle(" -fx-background-color: grey;");


        Scene scene = new Scene(root, 250, 250); //Size of opened window.
        stage.getIcons().add(new Image("file:src/main/resources/logo/CrezantLogo.png"));
        stage.setMinWidth(250);
        stage.setMinHeight(250);
        stage.setScene(scene);
        stage.show();
    }

    public boolean checkUsername(String userName)
    {
        String temp = null;
        try
        {
            temp = client.deafaultStartUp();
            Scanner in = new Scanner(temp);


            while(in.hasNextLine())
            {
                if(in.nextLine().startsWith(userName))
                {
                    return false;
                }
            }

            return true;

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}