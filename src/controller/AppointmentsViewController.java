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
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.TableLists;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class AppointmentsViewController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
        
    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, Integer> idCol;
    
    @FXML
    private TableColumn<Appointment, LocalDate> dateCol;

     @FXML
    private TableColumn<Appointment, LocalTime> startTimeCol;

    @FXML
    private TableColumn<Appointment, LocalTime> endTimeCol;
    
    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, String> customerCol;

    @FXML
    private TableColumn<Appointment, String> consultantCol;
    
    @FXML
    void onActionMonthView(ActionEvent event) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonth = today.plusMonths(1);
        FilteredList<Appointment> monthViewList = new FilteredList<>(TableLists.
                getAllAppointmentRows());
        // Lambda expression used to filter appointments efficiently
        monthViewList.setPredicate(row -> {
            LocalDate day = row.getDate() ;
            return day.isAfter(today) && day.isBefore(oneMonth);
        });
        appointmentsTableView.setItems(monthViewList);
    }
    
     @FXML
    void onActionViewAll(ActionEvent event) {
        appointmentsTableView.setItems(TableLists.getAllAppointmentRows());
    }

    @FXML
    void onActionWeekView(ActionEvent event) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeek = today.plusDays(7);
        FilteredList<Appointment> weekViewList = new FilteredList<>(TableLists.
                getAllAppointmentRows());
        // Lambda expression used to filter appointments efficiently
        weekViewList.setPredicate(row -> { 
            LocalDate day = row.getDate() ;
            return day.isAfter(today) && day.isBefore(oneWeek);
        });
        appointmentsTableView.setItems(weekViewList);
    }
    
    @FXML
    void onActionAdd(ActionEvent event) throws IOException {
        switchScenes(event, "AppointmentsAdd");
    }

    @FXML
    void onActionDelete(ActionEvent event) throws SQLException, IOException {
        try {
            Appointment appointmentRow = appointmentsTableView.getSelectionModel().getSelectedItem();
            int appointmentId = appointmentRow.getAppointmentId();
            QueryDB.deleteFromTbl("appointment", "appointmentId", appointmentId);
            //reload the scene to show that row has been removed from table
            switchScenes(event, "AppointmentsView");            
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a row to delete");
            alert.showAndWait();
        }
    }

    @FXML
    void OnActionDisplayCustomerRecords(ActionEvent event) throws IOException {
        switchScenes(event, "CustomerRecordsView");
    }
    
    @FXML
    void onActionReports(ActionEvent event) throws IOException {
        switchScenes(event, "ReportsMenu");
    }

    @FXML
    void onActionUpdate(ActionEvent event) throws IOException {
        // transfer data to update screen
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentsUpdate.fxml"));
        loader.load();
        AppointmentsUpdateController aUController = loader.getController(); 
        try {
            aUController.sendAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());
            //switch scene
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a row to update");
            alert.showAndWait();
        }
    }
    
    @FXML
    void onActionExit(ActionEvent event) {
        ConnectDB.DBClose();
        System.exit(0);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ResultSet appointmentRS = QueryDB.allSchedules();
            Data.buildAppointmentRows(appointmentRS);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        appointmentsTableView.setItems(TableLists.getAllAppointmentRows());
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));                        
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));                                        
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        consultantCol.setCellValueFactory(new PropertyValueFactory<>("userName"));        
    }       
}