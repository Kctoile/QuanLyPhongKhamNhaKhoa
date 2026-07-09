package com.dentalclinic.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payment {
    private int paymentId;
    private int appointmentId;
    private Appointment appointment;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionCode;
    private String gatewayReference;
    private String cardBrand;
    private String cardLast4;
    private String qrContent;
    private String notes;
    private Timestamp paidAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getGatewayReference() {
        return gatewayReference;
    }

    public void setGatewayReference(String gatewayReference) {
        this.gatewayReference = gatewayReference;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardLast4() {
        return cardLast4;
    }

    public void setCardLast4(String cardLast4) {
        this.cardLast4 = cardLast4;
    }

    public String getQrContent() {
        return qrContent;
    }

    public void setQrContent(String qrContent) {
        this.qrContent = qrContent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isPaid() {
        return "PAID".equalsIgnoreCase(status);
    }

    public String getMethodLabel() {
        if ("CASH".equalsIgnoreCase(method)) {
            return "Tiền mặt";
        }
        if ("BANK_TRANSFER".equalsIgnoreCase(method)) {
            return "Chuyển khoản QR";
        }
        if ("CREDIT_CARD".equalsIgnoreCase(method)) {
            return "Thẻ tín dụng";
        }
        return "Chưa chọn";
    }

    public String getStatusLabel() {
        if ("PAID".equalsIgnoreCase(status)) {
            return "Đã thanh toán";
        }
        if ("PENDING".equalsIgnoreCase(status)) {
            return "Đang chờ";
        }
        if ("FAILED".equalsIgnoreCase(status)) {
            return "Thất bại";
        }
        if ("CANCELLED".equalsIgnoreCase(status)) {
            return "Đã hủy";
        }
        return "Chưa thanh toán";
    }
}
