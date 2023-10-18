package com.example.projekatv2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.*;

public class AdminUserAssignController implements Initializable {


    @FXML
    Button button_back;
    @FXML
    Button button_update;
    @FXML
    DatePicker dp_from;
    @FXML
    DatePicker dp_to;
    @FXML
    ChoiceBox cb_campAssign;

    public String data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (data != null) {
            ObservableList<String> list = getCampNames("KAMP");
            cb_campAssign.getItems().addAll(list);
            ObservableList<String> details=getVolonteerDetails(data);
            if (details.size()>=4 && details.get(4)!=null) {
                cb_campAssign.setValue(details.get(4));
            }
            if (details.size()>=5&& details.get(5)!=null) dp_from.setValue(getDate(details.get(5)));
            if (details.size()>=6&& details.get(6)!=null) dp_to.setValue(getDate(details.get(6)));
        //    System.out.println(data);

        }
        button_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (data != null) {

                    String camp = (String) cb_campAssign.getValue();
                    LocalDate from = dp_from.getValue();
                    LocalDate to = dp_to.getValue();
                    updateVolounteerAssignment(to, from, camp, data);
                }

            }
        });
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            changeScene(event,"admin_user_menu.fxml","Admin",null);

            }
        });


    }
    public void setData(String s){
        this.data=s.split(" ", 2)[0];;
    }

    public static LocalDate getDate(String s) {

        if (s!=null) {
            String formatPattern = "yyyy-MM-dd";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);

            LocalDate date = LocalDate.parse(s, formatter);
            return date;
        }
        return LocalDate.now();
    }
}
