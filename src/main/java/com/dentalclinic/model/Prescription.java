package com.dentalclinic.model;

public class Prescription {
    private int prescriptionId;
    private Integer resultId;
    private ExaminationResult examinationResult; // Navigation property
    private String instructions;

    public Prescription() {
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getResultId() {
        return resultId;
    }

    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }

    public ExaminationResult getExaminationResult() {
        return examinationResult;
    }

    public void setExaminationResult(ExaminationResult examinationResult) {
        this.examinationResult = examinationResult;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
