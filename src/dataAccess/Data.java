/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;
import model.CustomerViewTable;
import model.ReportByAppointmentType;
import model.ReportTotalAppointments;
import model.TableLists;

/**
 *
 * @author robertthomure
 */
public class Data {
    
    public static int retrieveCustomerId(String customerName, ResultSet customerRS) throws SQLException{
        int customerId = -1;
        while(customerRS.next()){
            if(customerName.equals(customerRS.getString("customerName"))){
                customerId = customerRS.getInt("customerId");            
            }            
        }                
        return customerId;
    }
    
    public static int retrieveUserId(String userName, ResultSet userRS) throws SQLException{
        int userId = -1;
        while(userRS.next()){
            if(userName.equals(userRS.getString("userName"))){
                userId = userRS.getInt("userId");
            }
        }
        return userId;
    }
    
    public static void buildCustomerTableView(ResultSet customerRS) throws SQLException{
        TableLists.clearAllCustomerViews();
        while(customerRS.next()){
            int addressId = customerRS.getInt("addressId");
            int customerId = customerRS.getInt("customerId");
            String customerName = customerRS.getString("customerName");
            String address = customerRS.getString("address");
            String phone = customerRS.getString("phone");
            CustomerViewTable row = new CustomerViewTable(customerName, customerId, address, addressId, phone);
            TableLists.addCustomerViews(row);
        }
    }

    public static void buildCustomerComboBoxList(ResultSet customerRS) throws SQLException{
        TableLists.clearCustomerComboBoxList();
        while(customerRS.next()){
            String customerName = customerRS.getString("customerName");
            TableLists.addCustomerComboBoxList(customerName);
        }
    }

    public static void buildUserComboBoxList(ResultSet userRS) throws SQLException{
        TableLists.clearUserComboBoxList();
        while(userRS.next()){
        String userName = userRS.getString("UserName");
        TableLists.addUserComboBoxList(userName);
        }
    }
    
    public static int retrieveCityId(ResultSet addressTblRS, int addressId) throws SQLException{
        int cityId = 0;
        while(addressTblRS.next()){
            if(addressId == addressTblRS.getInt("addressId")){
                cityId = addressTblRS.getInt("cityId");
            }
        }
        return cityId;
    }
    
    public static int retrieveCountryId(ResultSet cityTblRS, int cityId) throws SQLException{
        int countryId = 0;
        while(cityTblRS.next()){
            if(cityId == cityTblRS.getInt("cityId")){
                countryId = cityTblRS.getInt("countryId");
            }
        }
        return countryId;
    }

    public static void buildAppointmentRows(ResultSet appointmentRS) throws SQLException{
        TableLists.clearAllAppointmentRows();
        ZoneId zoneId = ZoneId.systemDefault();
        while(appointmentRS.next()) {
            int customerId = appointmentRS.getInt("customerId");
            int userId = appointmentRS.getInt("userId");
            int appointmentId = appointmentRS.getInt("appointmentId");
            String customer = appointmentRS.getString("customerName");
            String userName = appointmentRS.getString("userName");
            String type = appointmentRS.getString("type");
            //convert timestamp to localTime
            LocalDateTime dateTimeStart = appointmentRS.getTimestamp("start").toLocalDateTime();
            ZonedDateTime zoneDateTimeStart = dateTimeStart.atZone(zoneId.of("UTC"));
            ZonedDateTime localTimeStart = zoneDateTimeStart.withZoneSameInstant(zoneId);
            LocalDateTime dateTimeEnd = appointmentRS.getTimestamp("end").toLocalDateTime();
            ZonedDateTime zoneDateTimeEnd = dateTimeEnd.atZone(zoneId.of("UTC"));
            ZonedDateTime localTimeEnd = zoneDateTimeEnd.withZoneSameInstant(zoneId);
            LocalDate date = localTimeStart.toLocalDate();
            LocalTime startTime = localTimeStart.toLocalTime();
            LocalTime endTime = localTimeEnd.toLocalTime();
            Appointment row = new Appointment(appointmentId, customerId,
                    userId, date, startTime, endTime, dateTimeStart, dateTimeEnd,
                    type, customer, userName);
            TableLists.addAppointmentRows(row);
        }
    }
    
    public static String convertToUTC(LocalDateTime dateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime = dateTime.atZone(zoneId).withZoneSameInstant(zoneId.of("UTC"));
        //convert LocalDateTime format into mySQL dateTime format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String utcTime = zoneDateTime.format(formatter);
        return utcTime; // convert to string remove T
    }
    
    public static boolean isConflictingAppt(int id, int appointmentId, ResultSet appointmentRS,
            String idType, LocalDateTime start, LocalDateTime end) throws SQLException {
        while(appointmentRS.next()) {
            if(appointmentId != appointmentRS.getInt("appointmentId")) {
                if(id == appointmentRS.getInt(idType)){
                    LocalDateTime apptTblStartTime = convertToLocalTime(appointmentRS.getTimestamp("start")
                            .toLocalDateTime());
                    LocalDateTime apptTblEndTime = convertToLocalTime(appointmentRS.getTimestamp("end")
                            .toLocalDateTime());
                    // added (start.isEqual(apptTblStartTime)) and (end.isEqual(apptTblEndTime))
                    // to prevent scheduling same start or end times
                    if((start.isAfter(apptTblStartTime) && start.isBefore(apptTblEndTime))
                            || (end.isAfter(apptTblStartTime) && end.isBefore(apptTblEndTime))
                            || (start.isEqual(apptTblStartTime))
                            || (end.isEqual(apptTblEndTime))
                            || (apptTblStartTime.isAfter(start) && apptTblStartTime.isBefore(end))
                            || (apptTblEndTime.isAfter(start) && apptTblEndTime.isBefore(end))) {
                        return true;
                    }
                }
            }
        }
        while(appointmentRS.previous()) {
            //returning appointmentRS to top of list
        }
        return false;
    }
    
    public static LocalDateTime convertToLocalTime(LocalDateTime utcDateTime) {
        //convert timestamp to localTime
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime = utcDateTime.atZone(zoneId.of("UTC"));
        ZonedDateTime localDateTime = zoneDateTime.withZoneSameInstant(zoneId);
        LocalDateTime dateTime = localDateTime.toLocalDateTime();
        return dateTime;
    }
    
    public static void buildreportByAppointmentTypeRows(ResultSet reportRS) throws SQLException{
        TableLists.clearAllReportByAppointmentTypeRows();
        while(reportRS.next()){
            String month = reportRS.getString("MONTHNAME(start)");
            int count = reportRS.getInt("COUNT(DISTINCT type)");
            ReportByAppointmentType row = new ReportByAppointmentType(month, count);
            TableLists.addReportByAppointmentTypeRows(row);
        }
    }
    
    public static void buildConsultantAppointmentRows(ResultSet appointmentRS) throws SQLException {
        TableLists.clearAllAppointmentRows();
        ZoneId zoneId = ZoneId.systemDefault();
        while(appointmentRS.next()){
            int appointmentId = appointmentRS.getInt("appointmentId");
            int customerId = appointmentRS.getInt("customerId");
            int userId = appointmentRS.getInt("userId");
            String customer = appointmentRS.getString("customerName");
            String userName = appointmentRS.getString("userName");
            String type = appointmentRS.getString("type");
            //convert timestamp to localTime
            LocalDateTime dateTimeStart = appointmentRS.getTimestamp("start").toLocalDateTime();
            ZonedDateTime zoneDateTimeStart = dateTimeStart.atZone(zoneId.of("UTC"));
            ZonedDateTime localTimeStart = zoneDateTimeStart.withZoneSameInstant(zoneId);
            LocalDateTime dateTimeEnd = appointmentRS.getTimestamp("end").toLocalDateTime();
            ZonedDateTime zoneDateTimeEnd = dateTimeEnd.atZone(zoneId.of("UTC"));
            ZonedDateTime localTimeEnd = zoneDateTimeEnd.withZoneSameInstant(zoneId);
            LocalDate date = localTimeStart.toLocalDate();
            LocalTime startTime = localTimeStart.toLocalTime();
            LocalTime endTime = localTimeEnd.toLocalTime();
            Appointment row = new Appointment(appointmentId, customerId,
                    userId, date, startTime, endTime, dateTimeStart, dateTimeEnd,
                    type, customer, userName);
                TableLists.addAppointmentRows(row);
        }
    }
    
    public static void buildReportTotalAppointments(ResultSet reportRS) throws SQLException {
        TableLists.clearReportTotalAppointmentRows();
        while(reportRS.next()){
            String consultant = reportRS.getString("userName");
            int count = reportRS.getInt("COUNT(appointment.appointmentId)");
            ReportTotalAppointments row = new ReportTotalAppointments(consultant, count);
            TableLists.addReportTotalAppointmentRows(row);
        }
    }
    
    public static void recordLogin(String userName, String result){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime = LocalDateTime.now().atZone(zoneId).withZoneSameInstant(zoneId.of("UTC"));
        try (PrintWriter loginLogger = new PrintWriter(new FileOutputStream(
                new File("login_log.txt"), true))) {
            loginLogger.append(userName + " " + result + " logged in " + zoneDateTime + "\n");
            loginLogger.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean hasAppointmentScheduled(ResultSet appointmentRS, int customerId) throws SQLException {
        boolean flag = false;
        while(appointmentRS.next()) {
            if(customerId == appointmentRS.getInt("customerId")) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }
}