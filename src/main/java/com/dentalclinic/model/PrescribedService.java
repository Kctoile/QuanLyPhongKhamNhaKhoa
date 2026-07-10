package com.dentalclinic.model;

public class PrescribedService {
    private int prescribedServiceId;
    private int resultId;
    private int serviceId;
    private String serviceName;
    private int quantity;

    public PrescribedService() {
        // Default constructor
    }

    public int getPrescribedServiceId() {
        return prescribedServiceId;
    }

    public void setPrescribedServiceId(int prescribedServiceId) {
        this.prescribedServiceId = prescribedServiceId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
