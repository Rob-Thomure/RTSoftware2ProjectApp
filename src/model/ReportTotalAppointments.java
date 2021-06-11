/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author robertthomure
 */
public class ReportTotalAppointments {
    private String consultant;
    private int numberOfAppointments;

    public ReportTotalAppointments(String consultant, int numberOfAppointments) {
        this.consultant = consultant;
        this.numberOfAppointments = numberOfAppointments;
    }

    public String getConsultant() {
        return consultant;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public int getNumberOfAppointments() {
        return numberOfAppointments;
    }

    public void setNumberOfAppointments(int numberOfAppointments) {
        this.numberOfAppointments = numberOfAppointments;
    }
}