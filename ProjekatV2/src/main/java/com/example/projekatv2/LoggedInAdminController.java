package com.example.projekatv2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInAdminController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Button button_camps;
    @FXML
    public  Label label1;
    @FXML
    private Button button_volonteers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"logIn.fxml","Log in",null);
            }
        });
        button_camps.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"admin_camp_menu.fxml","Admin",null);
            }
        });
        button_volonteers.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event,"admin_user_menu.fxml","Admin",null);
            }
        });

    }
    public void setUserInformation(String username)
    {

    }

}
