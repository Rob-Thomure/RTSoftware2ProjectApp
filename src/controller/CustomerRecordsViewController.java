/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.CustomerViewTable;
import model.TableLists;

/**
 * FXML Controller class
 *
 * @author robertthomure
 */
public class CustomerRecordsViewController implements Initializable {
    
    public void switchScenes(ActionEvent event, String view) throws IOException{    
    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
    Parent scene = FXMLLoader.load(getClass().getResource("/view/" + view + ".fxml"));
    stage.setScene(new Scene(scene));
    stage.show();
    }
    
    @FXML
    private TableView<CustomerViewTable> customerRecordsTableView;

    @FXML
    private TableColumn<CustomerViewTable, String> nameCol;

    @FXML
    private TableColumn<CustomerViewTable, String> addressCol;

    @FXML
    private TableColumn<CustomerViewTable, String> phoneNumberCol;

    @FXML
    void onActionAdd(ActionEvent event) throws IOException {
        switchScenes(event, "CustomerRecordsAdd");
    }

    @FXML
    void onActionDelete(ActionEvent event) throws SQLException, IOException {                
        try {
            //get selected row on the customer table view
            CustomerViewTable customer = customerRecordsTableView.getSelectionModel().getSelectedItem();
            int customerId = customer.getCustomerId();
            int addressId = customer.getAddressId();                                                                        
            try {
                ResultSet appointmentRS = QueryDB.allSchedules();
                boolean appointmentIsScheduled = Data.hasAppointmentScheduled(appointmentRS, customerId);
                if (appointmentIsScheduled) {
                    throw new MySQLIntegrityConstraintViolationException("has an appointment scheduled");
                }
                //using cusotmer id delete row from customer table
                QueryDB.deleteFromTbl("customer", "customerId", customerId);        
                //using addressId , get cityId from address table
                ResultSet addressTblRS = QueryDB.selectAllFromTbl("address");
                int cityId = Data.retrieveCityId(addressTblRS, addressId);        
                //using address id delete row from address table
                QueryDB.deleteFromTbl("address", "addressId", addressId);        
                //using city id, get country id from city table
                ResultSet cityTblRS = QueryDB.selectAllFromTbl("city");
                int countryId = Data.retrieveCountryId(cityTblRS, cityId);        
                //using city id delete row from city table
                QueryDB.deleteFromTbl("city", "cityId" ,cityId);        
                //using country id delete from country table
                QueryDB.deleteFromTbl("country", "countryId" ,countryId);
                //delete the selected row from viewing on the tableview
                switchScenes(event, "CustomerRecordsView");
            } catch(MySQLIntegrityConstraintViolationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("customer has appts scheduled, please delete "
                        + "associated appointments before delecting customer");
                alert.showAndWait();                
            }                        
        } catch(NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please select a row to delete");
            alert.showAndWait();            
        }                                
    }

    @FXML
    void OnActionDisplayAppointments(ActionEvent event) throws IOException {
        switchScenes(event, "AppointmentsView");
    }

    @FXML
    void onActionUpdate(ActionEvent event) throws IOException {                
        try {
            // transfer data to update screen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CustomerRecordsUpdate.fxml"));
            loader.load();
            CustomerRecordsUpdateController cRUController = loader.getController();
            cRUController.sendCustomer(customerRecordsTableView.getSelectionModel().getSelectedItem());
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        try {
            ResultSet customerRS = QueryDB.retrieveCustomerRecords();
            Data.buildCustomerTableView(customerRS);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerRecordsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }                
        customerRecordsTableView.setItems(TableLists.getAllCustomerViews());
        nameCol.setCellValueFactory( new PropertyValueFactory<>("customerName") );
        addressCol.setCellValueFactory( new PropertyValueFactory<>("address") );
        phoneNumberCol.setCellValueFactory( new PropertyValueFactory<>("phone") );                        
    }        
}