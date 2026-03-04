package com.dentalclinic.model;

public class DichVu {

    private int maDV;
    private String tenDV;
    private String moTa;
    private int gia;
    private int thoiGian;

    public DichVu() {
    }

    public DichVu(int maDV, String tenDV, String moTa, int gia, int thoiGian) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.moTa = moTa;
        this.gia = gia;
        this.thoiGian = thoiGian;
    }

    public int getMaDV() {
        return maDV;
    }

    public void setMaDV(int maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(int thoiGian) {
        this.thoiGian = thoiGian;
    }
}
