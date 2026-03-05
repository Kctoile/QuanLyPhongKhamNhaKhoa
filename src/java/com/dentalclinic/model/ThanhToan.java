package com.dentalclinic.model;

/**
 * Model bảng ThanhToan: MaTT, MaLich, TongTien, PhuongThuc, TrangThai
 */
public class ThanhToan {

    private int maTT;
    private int maLich;
    private double tongTien;
    private String phuongThuc;
    private String trangThai;

    public ThanhToan() {
    }

    public ThanhToan(int maTT, int maLich, double tongTien, String phuongThuc, String trangThai) {
        this.maTT = maTT;
        this.maLich = maLich;
        this.tongTien = tongTien;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
    }

    public int getMaTT() {
        return maTT;
    }

    public void setMaTT(int maTT) {
        this.maTT = maTT;
    }

    public int getMaLich() {
        return maLich;
    }

    public void setMaLich(int maLich) {
        this.maLich = maLich;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
