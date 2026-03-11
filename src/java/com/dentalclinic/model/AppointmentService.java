package com.dentalclinic.model;

public class AppointmentService {
    private int appointmentId;
    private int serviceId;

    public AppointmentService() {
    }

    public AppointmentService(int appointmentId, int serviceId) {
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
