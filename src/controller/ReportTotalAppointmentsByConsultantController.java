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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ReportTotalAppointments;
import model.TableLists;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class ReportTotalAppointmentsByConsultantController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
    
    @FXML
    private TableView<ReportTotalAppointments> reportTable;

    @FXML
    private TableColumn<ReportTotalAppointments, String> consultantCol;

    @FXML
    private TableColumn<ReportTotalAppointments, Integer> numberOfAppointmentsCol;
    
    @FXML
    void onActionBack(ActionEvent event) throws IOException {
        switchScenes(event, "ReportsMenu");
    }
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ResultSet reportRS = QueryDB.appointmentsForEachConsultant();
            Data.buildReportTotalAppointments(reportRS);
        } catch (SQLException ex) {
            Logger.getLogger(ReportTotalAppointmentsByConsultantController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        reportTable.setItems(TableLists.getReportTotalAppointmentRows());
        consultantCol.setCellValueFactory(new PropertyValueFactory<>("consultant"));
        numberOfAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("numberOfAppointments"));
    }        
}
