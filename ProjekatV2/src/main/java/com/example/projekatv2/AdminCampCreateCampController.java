package com.example.projekatv2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.projekatv2.DBUtils.*;

public class AdminCampCreateCampController implements Initializable {

    @FXML
    ChoiceBox cb_campPlace;
    @FXML
    ChoiceBox cb_campStatus;
    @FXML
    Button button_back;
    @FXML
    Button button_createCamp;

    @FXML
    TextField tf_campName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event, "admin_camp_menu.fxml", null, null);
            }
        });

        cb_campStatus.getItems().addAll("Aktivan", "Neaktivan");
        // Set an initial selection
        cb_campStatus.setValue("Aktivan");

        // List<String> listMjesta=getMjesto();
        //System.out.println(listMjesta);
        cb_campPlace.getItems().addAll(getMjesto());
        cb_campPlace.setValue(getMjesto().get(0));

        button_createCamp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int kampStatus = 0;
                boolean trg = true;
                String campName = tf_campName.getText();
                ObservableList<String> s = getCampNames("KAMP");
                for (String list : s) {
                    if (list.equals(campName)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setContentText("Kamp sa tim imenom vec postoji");
                        trg = false;
                        alert.show();
                    }
                }
                if (trg) {
                    String status1 = (String) cb_campStatus.getValue();

                    if (status1.equals("Aktivan")) kampStatus = 1;
                    else kampStatus = 2;

                    String place = (String) cb_campPlace.getValue();
                    createCamp(campName, kampStatus, place);
                    //System.out.println(campName +" "+ status1+kampStatus+" "+ place);
                }
            }
        });
    }
}

