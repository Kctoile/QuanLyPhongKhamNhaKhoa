package com.dentalclinic.model;

/**
 * Model ChiTietDonThuoc: MaDon, MaThuoc, SoLuong, DonGia
 */
public class ChiTietDonThuoc {

    private int maDon;
    private int maThuoc;
    private int soLuong;
    private double donGia;

    public int getMaDon() { return maDon; }
    public void setMaDon(int maDon) { this.maDon = maDon; }
    public int getMaThuoc() { return maThuoc; }
    public void setMaThuoc(int maThuoc) { this.maThuoc = maThuoc; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }
}
