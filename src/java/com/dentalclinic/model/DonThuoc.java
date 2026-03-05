package com.dentalclinic.model;

/**
 * Model bảng DonThuoc: MaDon, MaKQ, HuongDan
 */
public class DonThuoc {

    private int maDon;
    private int maKQ;
    private String huongDan;

    public int getMaDon() { return maDon; }
    public void setMaDon(int maDon) { this.maDon = maDon; }
    public int getMaKQ() { return maKQ; }
    public void setMaKQ(int maKQ) { this.maKQ = maKQ; }
    public String getHuongDan() { return huongDan; }
    public void setHuongDan(String huongDan) { this.huongDan = huongDan; }
}
