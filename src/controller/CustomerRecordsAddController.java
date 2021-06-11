/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dataAccess.QueryDB;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class CustomerRecordsAddController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
    
    @FXML
    private TextField nameTxt;

    @FXML
    private TextField addressTxt;
    
    @FXML
    private TextField cityTxt;

    @FXML
    private TextField countryTxt;

    @FXML
    private TextField phoneNumberTxt;

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        switchScenes(event, "CustomerRecordsView");
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException {               
        String name = nameTxt.getText().trim();
        String address = addressTxt.getText().trim();
        String city = cityTxt.getText().trim();
        String country = countryTxt.getText().trim();
        String phone = phoneNumberTxt.getText().trim();         
        try {
            if(name.isEmpty() || address.isEmpty() || city.isEmpty() || country.isEmpty() || phone.isEmpty()){
                throw new IllegalArgumentException("one or more required fields are empty");
            }                
            try {
                Long.parseLong(phone); // used to verify only numbers input for phone#
                int countryId = QueryDB.insertCountryTbl(country);
                int cityId = QueryDB.insertCityTbl(countryId, city);                    
                int addressId = QueryDB.insertAddressTbl(cityId, address, phone);
                QueryDB.insertCustomerTbl(addressId, name);
                switchScenes(event, "CustomerRecordsView");                  
            } catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please enter numbers for phone number field");
                alert.showAndWait();
            }                
        } catch(IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter info for all fields");
            alert.showAndWait();
        }   
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }        
}