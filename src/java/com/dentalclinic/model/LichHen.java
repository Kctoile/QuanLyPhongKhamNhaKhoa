package com.dentalclinic.model;

import java.sql.Date;
import java.sql.Time;

/**
 * Model bảng LichHen: MaLich, MaND, MaBacSi, MaDV, NgayKham, GioKham, TrangThai, GhiChu
 */
public class LichHen {

    private int maLich;
    private int maND;
    private Integer maBacSi;
    private int maDV;
    private Date ngayKham;
    private Time gioKham;
    private String trangThai;
    private String ghiChu;

    public LichHen() {
    }

    public int getMaLich() {
        return maLich;
    }

    public void setMaLich(int maLich) {
        this.maLich = maLich;
    }

    public int getMaND() {
        return maND;
    }

    public void setMaND(int maND) {
        this.maND = maND;
    }

    public Integer getMaBacSi() {
        return maBacSi;
    }

    public void setMaBacSi(Integer maBacSi) {
        this.maBacSi = maBacSi;
    }

    public int getMaDV() {
        return maDV;
    }

    public void setMaDV(int maDV) {
        this.maDV = maDV;
    }

    public Date getNgayKham() {
        return ngayKham;
    }

    public void setNgayKham(Date ngayKham) {
        this.ngayKham = ngayKham;
    }

    public Time getGioKham() {
        return gioKham;
    }

    public void setGioKham(Time gioKham) {
        this.gioKham = gioKham;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
