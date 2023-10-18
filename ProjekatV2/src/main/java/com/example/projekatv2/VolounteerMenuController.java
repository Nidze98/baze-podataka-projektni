package com.example.projekatv2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static com.example.projekatv2.DBUtils.*;

public class VolounteerMenuController implements Initializable {


    @FXML
    Label label_volounteer;
    @FXML
    Button button_addInCamp;
    @FXML
    Button button_pregledaj;
    @FXML
    Button button_back;
    private String username;
    private ObservableList<String> data = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*if (data != null ) {
            setLabel(data);
        }*/
        button_addInCamp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (data.get(4) != null) {
                    openSecondScene(event);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Korisnik nije rasporedjen ni u jedan kamp");
                    alert.show();
                }
            }
        });
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event, "logIn.fxml", null, null);
            }
        });
        button_pregledaj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (data.get(4) != null) {
                    openSecondScenePersonInCamp();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Korisnik nije rasporedjen ni u jedan kamp");
                    alert.show();
                }
            }
        });

    }

    public void setUserInformation(String username) {
        this.username = username;
        data = getVolonteerDetailsUsername(username);
        //System.out.println(data);
        setLabel(data);
    }

    public void setData(ObservableList<String> data,String username) {
        this.data = data;
        this.username=username;
    }

    private void openSecondScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("volounteer_addToCamp.fxml"));
            Parent root = loader.load();
            VolounteerAddToCampController controllerB = loader.getController();
            controllerB.setData(data, username);
            //  controllerB.setParams();
            //  System.out.println("jaje"+labelList);
            // Set the data before showing the stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Call initialize manually after setting the data
            //  controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSecondScenePersonInCamp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("volounteer_personInCampDetails.fxml"));
            Parent root = loader.load();
            VolounteerPersonInCampDetailsController controllerB = loader.getController();

            controllerB.setData(data, true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLabel(ObservableList<String> data) {
        if(data.get(4) != null)
        label_volounteer.setText(data.get(4));
        else label_volounteer.setText("Empty");
    }

}
