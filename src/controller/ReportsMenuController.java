/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class ReportsMenuController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
    
    @FXML
    void onActionDisplayAppointmentTypes(ActionEvent event) throws IOException {
        switchScenes(event, "ReportByAppointmentType");
    }

    @FXML
    void onActionDisplayConsultantSchedule(ActionEvent event) throws IOException {
        switchScenes(event, "ReportConsultantSchedule");
    }

    @FXML
    void onActionDisplayConsultantTotalAppointments(ActionEvent event) throws IOException {
        switchScenes(event, "ReportTotalAppointmentsByConsultant");
    }

    @FXML
    void onActionAppointments(ActionEvent event) throws IOException {
        switchScenes(event, "AppointmentsView");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }        
}