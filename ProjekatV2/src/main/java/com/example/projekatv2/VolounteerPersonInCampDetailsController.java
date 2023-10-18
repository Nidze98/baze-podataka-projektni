package com.example.projekatv2;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

import static com.example.projekatv2.DBUtils.changeSceneVolounteer;
import static com.example.projekatv2.DBUtils.getKorisnikKampaWithPeriodBoravka;

public class VolounteerPersonInCampDetailsController implements Initializable {


    private ObservableList<String> data=null;
    ObservableList<ObservableList<String>> list;
    @FXML
    TableView<ObservableList<String>> tb_PersonDetails;

    boolean trg=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (data != null) {
            if(trg) {
                list = getKorisnikKampaWithPeriodBoravka(data.get(4));
            }
            else if(!trg){
                list = getKorisnikKampaWithPeriodBoravka(data.get(0));
            }
         //   System.out.println(list);

            tb_PersonDetails.getItems().addAll(list);
            for (int columnIndex = 0; columnIndex < tb_PersonDetails.getColumns().size(); columnIndex++) {
                final int finalColumnIndex = columnIndex;
                TableColumn<ObservableList<String>, String> column = (TableColumn<ObservableList<String>, String>) tb_PersonDetails.getColumns().get(columnIndex);
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(finalColumnIndex)));
            }
        }

    }
    public void setData(ObservableList<String> data,boolean trg )
    {
        this.data=data;
        this.trg=trg;
    }

}
