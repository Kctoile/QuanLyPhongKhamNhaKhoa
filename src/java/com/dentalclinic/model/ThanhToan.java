package com.dentalclinic.model;

/**
 * Model đại diện bảng ThanhToan
 */
public class ThanhToan {

    private int id;
    private double tongTien;
    private String trangThai;

    public ThanhToan() {
    }

    public ThanhToan(int id, double tongTien, String trangThai) {
        this.id = id;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
