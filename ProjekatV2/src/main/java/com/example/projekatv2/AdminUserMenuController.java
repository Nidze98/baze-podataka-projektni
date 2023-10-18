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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.*;

public class AdminUserMenuController implements Initializable {


    @FXML
    Button button_back;
    @FXML
    Button button_assign;
    @FXML
    Button button_volonteerDetails;
    @FXML
    ListView<String> list_volonteers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        list_volonteers.setItems(getVolunteerNames());

        button_assign.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openSecondScene(event);
            }
        });
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event, "loggedin_admin.fxml", "admin", null);
            }
        });
        button_volonteerDetails.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openSecondSceneDetails(event);
            }
        });
    }

    private void openSecondScene(ActionEvent event) {
        try {
            String selectedItem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_user_assign.fxml"));
            Parent root = loader.load();
            AdminUserAssignController controllerB = loader.getController();

            selectedItem = list_volonteers.getSelectionModel().getSelectedItem();
            if (selectedItem == null) throw new NullPointerException();
            controllerB.setData(selectedItem);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            // Call initialize manually after setting the data
            controllerB.initialize(null, null);
            //  System.out.println("s");
            stage.show();
            //  System.out.println("s");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Morate odabrati jedan element");
            a.show();
        }
    }

    private void openSecondSceneDetails(ActionEvent event) {
        try {
            String selectedItem = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_user_volounteerDetails.fxml"));
            Parent root = loader.load();
            AdminUserVolounteerDetailsController controllerB = loader.getController();

            selectedItem = list_volonteers.getSelectionModel().getSelectedItem();
            if (selectedItem == null) throw new NullPointerException();
            ObservableList<String> labelList = getVolonteerDetails(selectedItem.split(" ", 2)[0]);
            controllerB.setData(labelList);


            //  System.out.println("DETEALJI"+labelList);

            //  System.out.println("jaje"+labelList);
            // Set the data before showing the stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            // Call initialize manually after setting the data
            controllerB.initialize(null, null);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Morate odabrati jedan element");
            a.show();
        }
    }
}
