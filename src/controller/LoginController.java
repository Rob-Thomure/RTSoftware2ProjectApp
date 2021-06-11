/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dataAccess.ConnectDB;
import dataAccess.Data;
import dataAccess.QueryDB;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.TableLists;

/**
 *
 * @author robertthomure
 */
public class LoginController implements Initializable {
    
    ResourceBundle rb;
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
            
    @FXML
    private TextField usernameTxt;

     @FXML
    private PasswordField passwordTxt;
     
     @FXML
    private Label titleLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label passwordLabel;
    
    @FXML
    private Button loginButton;

    @FXML
    private Button exitButton;
    
    @FXML
    void onActionExit(ActionEvent event) {
        ConnectDB.DBClose();
        System.exit(0);
    }

    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        String userName = usernameTxt.getText();
        String password = passwordTxt.getText();                
        ResultSet userRS = QueryDB.selectAllFromTbl("user");        
        boolean credentials = false;        
        try {
            while (userRS.next()){
                if ( userName.equals(userRS.getString("userName")) && password.equals(userRS.getString("password")) 
                        && credentials == false){
                    ResultSet appointmentRS = QueryDB.allSchedules();
                    Data.buildAppointmentRows(appointmentRS);                                                                                                                                           
                    LocalTime rightNow = LocalTime.now();
                    LocalDate today = LocalDate.now();
                    LocalTime fifteenMinute = rightNow.plusMinutes(15);
                    FilteredList<Appointment> fifteenMinuteList = new FilteredList<>(TableLists.
                            getAllAppointmentRows());
                    // Lambda expression used to filter appointments efficiently
                    fifteenMinuteList.setPredicate(row -> {
                        LocalTime time = row.getStartTime();
                        LocalDate day = row.getDate();
                        return day.equals(today) && time.isAfter(rightNow) && time.isBefore(fifteenMinute);
                    });
                    if(!fifteenMinuteList.isEmpty()){                                           
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Alert!");
                        alert.setHeaderText("Appointment Alert");
                        alert.setContentText("There is an appointment within 15 minutes");
                        alert.showAndWait();
                    }
                    Data.recordLogin(userName, "successfully");                                                                              
                    credentials = true;
                    switchScenes(event, "AppointmentsView");
                }             
            }
            if (credentials == false) {
                Data.recordLogin(userName, "failed");
                throw new IllegalArgumentException("incorrect username or password");
            }                        
        } catch(IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, rb.getString("message"));
            alert.setTitle(rb.getString("warning"));
            alert.setHeaderText(rb.getString("warning"));
            // Lambda expression used to display alert message efficiently
            alert.showAndWait().filter(response -> response == (ButtonType.OK)     );            
        }                                 
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        this.rb = rb;                
        titleLabel.setText(rb.getString("title"));
        userNameLabel.setText(rb.getString("userName"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));                        
    }        
}