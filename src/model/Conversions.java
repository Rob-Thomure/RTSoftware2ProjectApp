/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author robertthomure
 */
public class Conversions {
    
    public LocalDateTime convertToLocalTime(LocalDateTime utcDateTime) {
        //convert timestamp to localTime
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localDateTime = zoneDateTime.withZoneSameInstant(zoneId);
        LocalDateTime dateTime = localDateTime.toLocalDateTime();
        return dateTime;
    }
    
    public String convertToUTC(LocalDateTime dateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zoneDateTime = dateTime.atZone(zoneId).withZoneSameInstant(zoneId.of("UTC"));
        //convert LocalDateTime format into mySQL dateTime format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String utcTime = zoneDateTime.format(formatter);
        return utcTime; // convert to string remove T
    }
    
}
