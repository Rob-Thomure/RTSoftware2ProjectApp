/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;

/**
 *
 * @author robertthomure
 */
public class QueryDB {
    
    private static PreparedStatement pStatement;
    
    public static void setPStatement(Connection connection, String sqlStatement) {
        try {
            pStatement = connection.prepareStatement(sqlStatement);
        } catch (SQLException ex) {
            Logger.getLogger(QueryDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static PreparedStatement getPStatement(){
        return pStatement;
    }
    
    public static ResultSet selectAllFromTbl(String tableName) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String selectStatment = "SELECT * FROM " + tableName;
        setPStatement(connect, selectStatment);
        PreparedStatement ps = getPStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;
    }
    
    public static int insertCountryTbl(String country) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String insertStatement = "INSERT INTO country(country, createDate, createdBy, "
                + "lastUpdateBy) VALUES(?,now(),?,?)";
        setPStatement(connect, insertStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, country);
        ps.setString(2, "Admin");
        ps.setString(3, "Admin");
        ps.execute();
        // get countryId
        String idStatement = "SELECT LAST_INSERT_ID() FROM country";
        setPStatement(connect, idStatement);
        ps = getPStatement();
        ResultSet rs = ps.executeQuery();
        rs.next();
        int countryId = rs.getInt(1);
        ConnectDB.DBClose();
        return countryId;
    }
        
    public static int insertCityTbl(int countryId, String city) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String insertStatement = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?, now(),?,?)";
        setPStatement(connect, insertStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, city);
        ps.setInt(2, countryId);
        ps.setString(3, "Admin");
        ps.setString(4, "Admin");
        ps.execute();
        String idStatement = "SELECT LAST_INSERT_ID() FROM city";
        setPStatement(connect, idStatement);
        ps = getPStatement();
        ResultSet rs = ps.executeQuery();
        rs.next();
        int cityId = rs.getInt(1);
        ConnectDB.DBClose();
        return cityId;
    }
    
    public static int insertAddressTbl(int cityId, String address, String phone) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String insertStatement = "INSERT INTO address(address, address2, cityId, "
                + "postalCode, phone, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?, now(),?,?)";
        setPStatement(connect, insertStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, address);
        ps.setString(2, "");
        ps.setInt(3, cityId);
        ps.setString(4, "12345");
        ps.setString(5, phone);
        ps.setString(6, "Admin");
        ps.setString(7, "Admin");
        ps.execute();
        String idStatement = "SELECT LAST_INSERT_ID() FROM address";
        setPStatement(connect, idStatement);
        ps = getPStatement();
        ResultSet rs = ps.executeQuery();
        rs.next();
        int addressId = rs.getInt(1);
        ConnectDB.DBClose();
        return addressId;
    }
    
    public static void insertCustomerTbl(int addressId, String customerName) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String insertStatement = "INSERT INTO customer(customerName, addressId, "
                + "active, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?, now(),?,?)";
        setPStatement(connect, insertStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, customerName);
        ps.setInt(2, addressId);
        ps.setInt(3, 1);
        ps.setString(4, "Admin");
        ps.setString(5, "Admin");
        ps.execute();
        ConnectDB.DBClose();
    }
    
    public static void insertAppointmentTbl(int customerId, int userId, String type,
            String startTime, String endTime) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String insertStatement = "INSERT INTO appointment(customerId, userId, title, "
                + "description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?, now(),?,?)";
        setPStatement(connect, insertStatement);
        PreparedStatement ps = getPStatement();
        ps.setInt(1, customerId);
        ps.setInt(2, userId);
        ps.setString(3, "unknown");
        ps.setString(4, "unknown");
        ps.setString(5, "unknown");
        ps.setString(6, "unknown");
        ps.setString(7, type);
        ps.setString(8, "unknown");
        ps.setString(9, startTime);
        ps.setString(10, endTime);
        ps.setString(11, "Admin");
        ps.setString(12, "Admin");
        ps.execute();
        ConnectDB.DBClose();
    }
    
    public static void updateAppointmentTbl(Appointment appointmentRow) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String updateStatement = "UPDATE appointment"
                + " SET customerId = ?, userId = ?, type = ?, start = ?, end = ?, lastUpdateBy = ?"
                + " WHERE appointmentId = ?";
        setPStatement(connect, updateStatement);
        PreparedStatement ps = getPStatement();
        ps.setInt(1, appointmentRow.getCustomerId());
        ps.setInt(2, appointmentRow.getUserId());
        ps.setString(3, appointmentRow.getType());
        LocalDateTime dateTimeStart = appointmentRow.getDateTimeStart();
        LocalDateTime dateTimeEnd = appointmentRow.getDateTimeEnd();
        String startTime = Data.convertToUTC(dateTimeStart);
        String endTime = Data.convertToUTC(dateTimeEnd);
        ps.setString(4, startTime);
        ps.setString(5, endTime);
        ps.setString(6, "Admin");
        ps.setInt(7, appointmentRow.getAppointmentId());
        ps.execute();
        ConnectDB.DBClose();
    }
    
    public static void updateCustomerTbl(int customerId, String customerName) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String updateStatement = "UPDATE customer SET customerName = ? WHERE customerId = ?";
        setPStatement(connect, updateStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, customerName);
        ps.setInt(2, customerId);
        ps.execute();
        ConnectDB.DBClose();
    }
    
    public static void updateAddressTbl(int addressId, String address, String phone) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String updateStatement = "UPDATE address SET address = ?, phone = ? WHERE addressId = ?";
        setPStatement(connect, updateStatement);
        PreparedStatement ps = getPStatement();
        ps.setString(1, address);
        ps.setString(2, phone);
        ps.setInt(3, addressId);
        ps.execute();
    }
    
    public static void deleteFromTbl(String tbl, String tblColumn,int id) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String deleteStatement = "DELETE FROM " +tbl + " WHERE " + tblColumn + " = ?";
        setPStatement(connect, deleteStatement);
        PreparedStatement ps = getPStatement();
        ps.setInt(1, id);
        ps.execute();
        ConnectDB.DBClose();
    }
    
    public static ResultSet NumberAppointmentTypesByMonth() throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String selectStatment = "SELECT MONTHNAME(start), COUNT(DISTINCT type) "
                + "FROM appointment GROUP BY MONTHNAME(start)";
        setPStatement(connect, selectStatment);
        PreparedStatement ps = getPStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;
    }
    
    public static ResultSet scheduleForEachConsultant(String user) throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String selectStatment = "SELECT appointment.*, user.*, customer.* "
                                + "FROM appointment, user, customer "
                                + "WHERE appointment.userId = user.userId "
                                + "AND appointment.customerId = customer.customerId "
                                + "AND user.userName = ?";
        setPStatement(connect, selectStatment);
        PreparedStatement ps = getPStatement();
        ps.setString(1, user);
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;
    }
    
    public static ResultSet allSchedules() throws SQLException {
        Connection connect = ConnectDB.DBConnect();
        String selectStatement = "SELECT appointment.*, user.*, customer.* "
                                + "FROM appointment, user, customer "
                                + "WHERE appointment.userId = user.userId "
                                + "AND appointment.customerId = customer.customerId ";
        setPStatement(connect, selectStatement);
        PreparedStatement ps = getPStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;
    }
    
    public static ResultSet appointmentsForEachConsultant() throws SQLException{
        Connection connect = ConnectDB.DBConnect();
        String selectStatment = "SELECT user.userName, COUNT(appointment.appointmentId) "
                                + "FROM appointment, user "
                                + "WHERE appointment.userId = user.userId "
                                + "GROUP BY user.userName";
        setPStatement(connect, selectStatment);
        PreparedStatement ps = getPStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;
    }
    
    public static ResultSet retrieveCustomerRecords() throws SQLException {
        Connection connect = ConnectDB.DBConnect();
        String selectStatment = "SELECT customer.*, address.* "
                                + "FROM customer, address "
                                + "WHERE customer.addressId = address.addressId ";
        setPStatement(connect, selectStatment);
        PreparedStatement ps = getPStatement();
        ps.execute();
        ResultSet rs = ps.getResultSet();
        return rs;        
    }
}