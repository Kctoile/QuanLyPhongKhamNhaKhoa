package com.dentalclinic.model;

import java.sql.Time;

public class ClinicConfig {
    private int configId;
    private Time openingTime;
    private Time closingTime;
    private String clinicInfo;

    public ClinicConfig() {
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    public String getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(String clinicInfo) {
        this.clinicInfo = clinicInfo;
    }
}
