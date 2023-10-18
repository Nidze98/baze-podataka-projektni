package com.example.projekatv2;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



public class LogInController implements Initializable {

    @FXML
    private Button button_login;

    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                DBUtils.logInUser(actionEvent, tf_username.getText(),tf_password.getText() );
            }
        });
    }
}
