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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.*;

public class AdminCampMenuController implements Initializable {

    @FXML
    Button button_back;
    @FXML
    Button button_createCamp;
    @FXML
    Button button_updateCamp;
    @FXML
    Button button_delete;
    @FXML
    Button button_campDetails;
    @FXML
    ListView<String> list_camps;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        list_camps.setItems( getCampNames("KAMP"));
      //  System.out.println(getCampNames("KAMP"));
        button_campDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                openSecondScene(event);

               // if(list_CampDetails!=null)
            }
            });
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event,"loggedIn_admin.fxml",null,null);
            }
        });
        button_createCamp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event,"admin_camp_createCamp.fxml",null,null);
            }
        });
        button_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedItem = list_camps.getSelectionModel().getSelectedItem();
                deleteCamp(selectedItem);
                list_camps.getItems().clear();
                list_camps.setItems( getCampNames("KAMP"));

            }
        });
        button_updateCamp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                openSecondSceneUpdate(event);

                // if(list_CampDetails!=null)
            }
        });



    }
    private void openSecondScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_camp_CampDetails.fxml"));
            Parent root = loader.load();
            AdminCampDetailsControles controllerB = loader.getController();

            String selectedItem = list_camps.getSelectionModel().getSelectedItem();
            ObservableList<String> labelList = getCampDetails(selectedItem);
           // System.out.println(labelList);
            controllerB.setData(labelList);

            // Set the data before showing the stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openSecondSceneUpdate(ActionEvent event) {
        try {
          //  System.out.println("s");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_camp_updateCamp.fxml"));
         //   System.out.println("s");
            Parent root = loader.load();
         //   System.out.println("s");
            AdminCampUpdateCampController controllerB = loader.getController();
        //    System.out.println("s");
            String selectedItem = list_camps.getSelectionModel().getSelectedItem();
            ObservableList<String> labelList = getCampDetails(selectedItem);
         //   System.out.println(labelList);
            controllerB.setData(labelList);
        //    System.out.println("jaje"+labelList);
            // Set the data before showing the stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
