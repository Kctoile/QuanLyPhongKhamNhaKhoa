package com.dentalclinic.model;

import java.sql.Timestamp;

public class ExaminationResult {

    private int resultId;
    private Integer appointmentId;
    private Appointment appointment; // Navigation property
    private String resultDetails;
    private Timestamp examinationDate;

    public ExaminationResult() {
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public Timestamp getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(Timestamp examinationDate) {
        this.examinationDate = examinationDate;
    }
}
