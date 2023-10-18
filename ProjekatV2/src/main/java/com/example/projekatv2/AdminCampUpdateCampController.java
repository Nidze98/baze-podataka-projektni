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
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.*;

public class AdminCampUpdateCampController implements Initializable {

    @FXML
    ChoiceBox cb_UcampPlace;
    @FXML
    ChoiceBox cb_UcampStatus;
    @FXML
    Button button_Uback;
    @FXML
    Button button_UupdateCamp;

    @FXML
    TextField tf_UcampName;
    public ObservableList<String> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_Uback.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event,"admin_camp_menu.fxml",null,null);
            }
        });
        if (data != null && data.size() >= 4) {
            cb_UcampStatus.getItems().addAll("Aktivan", "Neaktivan");
            // Set an initial selection
               cb_UcampStatus.setValue(data.get(3));

            // List<String> listMjesta=getMjesto();
            //System.out.println(listMjesta);
            cb_UcampPlace.getItems().addAll(getMjesto());
            cb_UcampPlace.setValue(data.get(1));
            tf_UcampName.appendText(data.get(0));
        }
        button_UupdateCamp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int kampStatus = 0;
                boolean trg=true;
                String campName = tf_UcampName.getText();
                ObservableList<String> s = getCampNames("KAMP");
                for (String list : s) {
                    if (list.equals(campName)&&(!list.equals(data.get(0)))) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Kamp sa tim imenom vec postoji");
                        trg = false;
                        alert.show();
                    }
                }
                if (trg) {

                    String status1 = (String) cb_UcampStatus.getValue();
                 //   System.out.println(status1);
                    if ("Aktivan".equals(status1)) kampStatus = 1;
                    else kampStatus = 2;
              //      System.out.println(kampStatus);
                    String place = (String) cb_UcampPlace.getValue();
                    updateCamp(campName, kampStatus, place);
                    //System.out.println(campName +" "+ status1+kampStatus+" "+ place);
                }
            }
        });
    }
    public void setData(ObservableList<String> data) {
        this.data= data;
    }
}
