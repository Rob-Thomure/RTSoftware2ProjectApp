/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author robertthomure
 */
public class TableLists {
    
    private static ObservableList<CustomerViewTable> allCustomerViews = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointmentRows = FXCollections.observableArrayList();
    private static ObservableList<String> hours = FXCollections.observableArrayList("01", 
            "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", 
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
    private static ObservableList<String> minutes = FXCollections.observableArrayList("00", 
            "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55");
    private static ObservableList<String> meetingTypes = FXCollections.observableArrayList(
            "Planning", "Decision Making", "Progress Update");
    private static ObservableList<String> customerComboBoxList = FXCollections.observableArrayList();
    private static ObservableList<String> userComboBoxList = FXCollections.observableArrayList();
    private static ObservableList<ReportByAppointmentType> reportByAppointmentTypeRows = FXCollections.observableArrayList();
    private static ObservableList<ReportTotalAppointments> reportTotalAppointmentRows = FXCollections.observableArrayList();
    
    public static  void addCustomerViews(CustomerViewTable customerRow){
        allCustomerViews.add(customerRow);
    }
    
    public static ObservableList<CustomerViewTable> getAllCustomerViews(){
        return allCustomerViews;
    }
    
    public static void clearAllCustomerViews(){
        allCustomerViews.clear();
    }
    
    public static boolean deleteCustomerViews(CustomerViewTable selectedRow){
        return allCustomerViews.remove(selectedRow);
    }
    
    public static void addAppointmentRows(Appointment appointmentRow){
        allAppointmentRows.add(appointmentRow);
    }
    
    public static ObservableList<Appointment> getAllAppointmentRows(){
        return allAppointmentRows;
    }
    
    public static void clearAllAppointmentRows(){
        allAppointmentRows.clear();
    }
    
    public static void deleteAppointmentRows(Appointment selectedRow){
        allAppointmentRows.remove(selectedRow);
    }

    public static ObservableList<String> getHours() {
        return hours;
    }

    public static ObservableList<String> getMinutes() {
        return minutes;
    }

    public static ObservableList<String> getMeetingTypes() {
        return meetingTypes;
    }
    
    public static void addCustomerComboBoxList(String row){
        customerComboBoxList.add(row);
    }
    
    public static ObservableList<String> getCustomerComboBoxList(){
        return customerComboBoxList;
    }
    
    public static void clearCustomerComboBoxList(){
        customerComboBoxList.clear();
    }
    
    public static void addUserComboBoxList(String row){
        userComboBoxList.add(row);
    }
    
    public static ObservableList<String> getUserComboBoxList(){
        return userComboBoxList;
    }
    
    public static void clearUserComboBoxList(){
        userComboBoxList.clear();
    }
    
    public static  void addReportByAppointmentTypeRows(ReportByAppointmentType row){
        reportByAppointmentTypeRows.add(row);
    }
    
    public static ObservableList<ReportByAppointmentType> getReportByAppointmentTypeRows(){
        return reportByAppointmentTypeRows;
    }
    
    public static void clearAllReportByAppointmentTypeRows(){
        reportByAppointmentTypeRows.clear();
    }
    
    public static  void addReportTotalAppointmentRows(ReportTotalAppointments row){
        reportTotalAppointmentRows.add(row);
    }
    
    public static ObservableList<ReportTotalAppointments> getReportTotalAppointmentRows(){
        return reportTotalAppointmentRows;
    }
    
    public static void clearReportTotalAppointmentRows(){
        reportTotalAppointmentRows.clear();
    }
}