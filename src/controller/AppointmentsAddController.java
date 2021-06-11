/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.TableLists;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class AppointmentsAddController implements Initializable {

    public void switchScenes(ActionEvent event, String view) throws IOException{
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }

    @FXML
    private DatePicker dateBox;

    @FXML
    private ComboBox<String> startHrComboBox;

    @FXML
    private ComboBox<String> startMinuteComboBox;

    @FXML
    private ComboBox<String> endHrComboBox;

    @FXML
    private ComboBox<String> endMinuteComboBox;

     @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private ComboBox<String> userComboBox;

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        switchScenes(event, "AppointmentsView");
    }

    @FXML
    void onActionSave(ActionEvent event) throws IOException, SQLException {
        LocalDate date = dateBox.getValue();
        String startHour = startHrComboBox.getValue();
        String startMinute = startMinuteComboBox.getValue();
        String endHour = endHrComboBox.getValue();
        String endMinute = endMinuteComboBox.getValue();
        String type = typeComboBox.getValue();
        String customerName = customerComboBox.getValue();
        String userName = userComboBox.getValue();
        try {
                //convert date, hour, minute into LocalDateTime format
            LocalDateTime dateTimeStart = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                    date.getDayOfMonth(), Integer.parseInt(startHour), Integer.parseInt(startMinute));
            LocalDateTime dateTimeEnd = LocalDateTime.of(date.getYear(), date.getMonthValue(),
                    date.getDayOfMonth(), Integer.parseInt(endHour), Integer.parseInt(endMinute));                        
            if(dateTimeStart.isBefore(dateTimeEnd)){            
                String startTime = Data.convertToUTC(dateTimeStart);
                String endTime = Data.convertToUTC(dateTimeEnd);
                // get customerId
                ResultSet customerRS = QueryDB.selectAllFromTbl("customer");
                int customerId = Data.retrieveCustomerId(customerName, customerRS);
                // get userId
                ResultSet userRS = QueryDB.selectAllFromTbl("user");
                int userId = Data.retrieveUserId(userName, userRS);
                //checking existing appointments
                ResultSet appointmentRS = QueryDB.selectAllFromTbl("appointment");
                LocalTime localStartTime = dateTimeStart.toLocalTime();
                LocalTime localEndTime = dateTimeEnd.toLocalTime();
                LocalTime businessTimeStart = LocalTime.of(8, 00);
                LocalTime businessTimeEnd = LocalTime.of(17, 00);
                int dayOfWeek = dateTimeStart.getDayOfWeek().getValue();
                try {
                    if(Data.isConflictingAppt(customerId, 0, appointmentRS, "customerId", dateTimeStart, dateTimeEnd)
                            || Data.isConflictingAppt(userId, 0, appointmentRS, "userId", dateTimeStart, dateTimeEnd)) {
                        throw new IllegalArgumentException("conflicts with an existing schedule");
                    }
                    try {
                        if(localStartTime.isBefore(businessTimeStart) || localEndTime.isAfter(businessTimeEnd) ||
                                dayOfWeek == 6 || dayOfWeek == 7){
                            throw new IllegalArgumentException("Scheduled time is not "
                                    + "within business hours (Mon-Fri 08:00 - 17:00)");
                        }
                        QueryDB.insertAppointmentTbl(customerId, userId, type, startTime, endTime);
                        switchScenes(event, "AppointmentsView");
                    } catch (IllegalArgumentException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Please schedule a time within business hours(Mon-Fri 08:00-17:00)");
                        alert.showAndWait();
                    }
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("conflicts with an existing schedule");
                    alert.showAndWait();
                }                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Please choose an end time that is after the start time");
                alert.showAndWait();                
            }                                    
        } catch(NullPointerException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please fill in all empty fields");
            alert.showAndWait();
        }
    }
    
    @FXML
    void onActionTypeCombo(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startHrComboBox.setItems(TableLists.getHours());
        startMinuteComboBox.setItems(TableLists.getMinutes());
        endHrComboBox.setItems(TableLists.getHours());
        endMinuteComboBox.setItems(TableLists.getMinutes());
        typeComboBox.setItems(TableLists.getMeetingTypes());
        try {   //build the customer combo box
            ResultSet customerRS = dataAccess.QueryDB.selectAllFromTbl("customer");
            Data.buildCustomerComboBoxList(customerRS);
            customerComboBox.setItems(TableLists.getCustomerComboBoxList());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            //build the user combo box
            ResultSet userRS = dataAccess.QueryDB.selectAllFromTbl("user");
            Data.buildUserComboBoxList(userRS);
            userComboBox.setItems(TableLists.getUserComboBoxList());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsAddController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}