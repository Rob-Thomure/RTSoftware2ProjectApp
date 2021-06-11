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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.TableLists;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class ReportConsultantScheduleController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
    
    @FXML
    private TableView<Appointment> reportTable;

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
    private ComboBox<String> consultantComboBox;

    @FXML
    void onActionconsultantComboBox(ActionEvent event) throws SQLException {
        String consultant = consultantComboBox.getValue();
        ResultSet appointmentRS = QueryDB.scheduleForEachConsultant(consultant);
        Data.buildConsultantAppointmentRows(appointmentRS);
        reportTable.setItems(TableLists.getAllAppointmentRows());        
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));                        
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));                                        
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        consultantCol.setCellValueFactory(new PropertyValueFactory<>("userName"));        
    }
    
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        switchScenes(event, "ReportsMenu");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResultSet userRS;
        try {
            userRS = dataAccess.QueryDB.selectAllFromTbl("user");
            Data.buildUserComboBoxList(userRS);
            consultantComboBox.setItems(TableLists.getUserComboBoxList());            
        } catch (SQLException ex) {
            Logger.getLogger(ReportConsultantScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }            
    }        
}