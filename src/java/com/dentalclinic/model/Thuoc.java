package com.dentalclinic.model;

/**
 * Model thuốc trong danh mục - khớp bảng Thuoc (MaThuoc, TenThuoc, DonGia, SoLuongTon)
 */
public class Thuoc {

    private int maThuoc;
    private String tenThuoc;
    private double donGia;
    private int soLuongTon;

    public Thuoc() {
    }

    public Thuoc(int maThuoc, String tenThuoc, double donGia, int soLuongTon) {
        this.maThuoc = maThuoc;
        this.tenThuoc = tenThuoc;
        this.donGia = donGia;
        this.soLuongTon = soLuongTon;
    }

    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getTenThuoc() {
        return tenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        this.tenThuoc = tenThuoc;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }
}
