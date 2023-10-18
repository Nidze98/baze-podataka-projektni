package com.example.projekatv2;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.changeScene;


public class AdminCampDetailsControles implements Initializable {

    @FXML
    private TableView<String> table_CampDetails;
    @FXML
    Button button_back;
    @FXML
    Button button_userDetails;
    private ObservableList<String> data;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (data != null && data.size() >= 4) {
            TableColumn<String, String> column1 = (TableColumn<String, String>) table_CampDetails.getColumns().get(0);
            TableColumn<String, String> column2 = (TableColumn<String, String>) table_CampDetails.getColumns().get(1);
            TableColumn<String, String> column3 = (TableColumn<String, String>) table_CampDetails.getColumns().get(2);
            TableColumn<String, String> column4 = (TableColumn<String, String>) table_CampDetails.getColumns().get(3);
            TableColumn<String, String> column5 = (TableColumn<String, String>) table_CampDetails.getColumns().get(4);

            column1.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(0)));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(1)));
            column3.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(2)));
            column4.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(3)));
            column5.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(4)));

            table_CampDetails.getItems().add(String.format("%s,%s,%s,%s %s", data.get(0), data.get(1), data.get(2), data.get(3),data.get(4)));
        }
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event,"admin_camp_menu.fxml",null,null);
            }
        });
        button_userDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openSecondScenePersonInCamp(event);
            }
        });
    }

    public void setData(ObservableList<String> data) {
        this.data = data;
    }

    public void openSecondScenePersonInCamp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("volounteer_personInCampDetails.fxml"));
            Parent root = loader.load();
            VolounteerPersonInCampDetailsController controllerB = loader.getController();
            controllerB.setData(data,false);
            //  controllerB.setParams();
            //  System.out.println("jaje"+labelList);
            // Set the data before showing the stage
          //  Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage stage=new Stage();
            stage.setScene(new Scene(root));
            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

