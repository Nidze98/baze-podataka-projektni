package com.example.projekatv2;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.swing.text.TabableView;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.projekatv2.DBUtils.changeScene;

public class AdminUserVolounteerDetailsController implements Initializable {

    @FXML
    TableView<String> tb_VolounteerDetails;
    @FXML
    Button button_back;

    private ObservableList<String> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (data != null && data.size() >= 7) {
            TableColumn<String, String> column1 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(0);
            TableColumn<String, String> column2 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(1);
            TableColumn<String, String> column3 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(2);
            TableColumn<String, String> column4 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(3);
            TableColumn<String, String> column5 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(4);
            TableColumn<String, String> column6 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(5);
            TableColumn<String, String> column7 = (TableColumn<String, String>) tb_VolounteerDetails.getColumns().get(6);

            column1.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(0)));
            column2.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(1)));
            column3.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(2)));
            column4.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(3)));
            column5.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(4)));
            column6.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(5)));
            column7.setCellValueFactory(cellData -> new SimpleStringProperty(data.get(6)));

            tb_VolounteerDetails.getItems().add(String.format("%s,%s,%s,%s,%s,%s,%s", data.get(0), data.get(1), data.get(2), data.get(3), data.get(4), data.get(5), data.get(6)));
        }
        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changeScene(event,"admin_user_menu.fxml",null,null);
            }
        });
    }

    public void setData(ObservableList<String> data) {
        this.data = data;
    }
}
