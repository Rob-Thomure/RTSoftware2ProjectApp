/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author robertthomure
 */
public class ConnectDB {    
    private static final String JDBCURL = "jdbc:mysql://####";    
    private static final String DRIVERINTERFACE = "com.mysql.jdbc.Driver";
    private static Connection connection = null;    
    private static final String USERNAME = "####";
    private static final String PASSWORD = "####";    
    
    public static Connection DBConnect() {
        try{
        Class.forName(DRIVERINTERFACE);
        connection = (Connection)DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
        }
        catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());            
        }
        return connection;
    }
    
    public static void DBClose() {        
        try{
        connection.close();
        }
        catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    } 
}