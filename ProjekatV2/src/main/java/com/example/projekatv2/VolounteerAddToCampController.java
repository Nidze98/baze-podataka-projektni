package com.example.projekatv2;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.projekatv2.AdminUserAssignController.getDate;
import static com.example.projekatv2.DBUtils.addPersonToCamp;
import static com.example.projekatv2.DBUtils.changeSceneVolounteer;

public class VolounteerAddToCampController implements Initializable {

    @FXML
    private TextField tf_jmb;

    @FXML
    private TextField tf_ime;

    @FXML
    private TextField tf_prezime;

    @FXML
    private DatePicker dp_datumRodjenja;

    @FXML
    private ChoiceBox<String> cb_pol;

    @FXML
    private TextField tf_drzavljanstvo;

    @FXML
    private TextField tf_mjestoprebivalista;

    @FXML
    private TextField tf_tipNesrece;

    @FXML
    private ChoiceBox<String> cb_status;

    @FXML
    private DatePicker dp_datumDolaska;

    @FXML
    private DatePicker dp_datumOdlaska;

    @FXML
    private Button button_back;

    @FXML
    private Button button_enterData;

    private ObservableList<String> data;

    private String username;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setParams();
        button_enterData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (data != null) {
                    try {

                        String ime, prezime, pol, drzavljanstvo, mjestoPrebivalista, tipNesrece, status;
                        int JMB;
                        LocalDate datumDolaska, datumOdlaska, datumRodjenja;

                        JMB = Integer.parseInt(tf_jmb.getText());

                        ime = tf_ime.getText();
                        prezime = tf_prezime.getText();

                        datumRodjenja = dp_datumRodjenja.getValue();
                        if (datumRodjenja == null) throw new DateTimeException("");

                        pol = (String) cb_pol.getValue();
                        drzavljanstvo = tf_drzavljanstvo.getText();
                        mjestoPrebivalista = tf_mjestoprebivalista.getText();
                        tipNesrece = tf_tipNesrece.getText();
                        status = (String) cb_status.getValue();

                        datumDolaska = dp_datumDolaska.getValue();
                        if (datumDolaska == null) throw new DateTimeException("");

                        datumOdlaska = dp_datumOdlaska.getValue();
                        if (datumOdlaska == null) throw new DateTimeException("");

                        String campName = data.get(4);
                        addPersonToCamp(JMB, ime, prezime, datumRodjenja, pol, drzavljanstvo, mjestoPrebivalista, tipNesrece, status, datumDolaska, datumOdlaska, campName, username);
                    } catch (DateTimeException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Pogresni podaci,datumi moraju biti odabrani");
                        alert.show();
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Pogresni podaci, JMB je tipa int");
                        alert.show();
                    }
                }
            }
        });
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openMenu(event);
            }
        });


    }

    public void setData(ObservableList<String> s, String username) {
        this.username = username;
        this.data = s;
    }

    public void setParams() {
        cb_status.getItems().addAll("Aktivan", "Neaktivan");
        // Set an initial selection
        cb_status.setValue("Aktivan");
        cb_pol.getItems().addAll("M", "F");
        // Set an initial selection
        cb_pol.setValue("M");
        //   System.out.println("Jaje"+data);
    }

    public void openMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("volounteer_menu.fxml"));
            Parent root = loader.load();
            VolounteerMenuController controllerB = loader.getController();

            controllerB.setData(data, username);
            //  controllerB.setParams();
            //  System.out.println("jaje"+labelList);
            // Set the data before showing the stage

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            //Stage stage=new Stage();
            stage.setScene(new Scene(root));
            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
