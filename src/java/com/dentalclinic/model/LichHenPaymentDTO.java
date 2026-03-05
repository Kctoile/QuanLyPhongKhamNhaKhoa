package com.dentalclinic.model;

import java.sql.Date;
import java.sql.Time;

/**
 * DTO hiển thị lịch hẹn cần thanh toán - dùng cho trang Staff
 */
public class LichHenPaymentDTO {

    private int maLich;
    private String tenKhachHang;
    private String tenBacSi;
    private String tenDV;
    private double gia;
    private Date ngayKham;
    private Time gioKham;
    private String trangThai;

    public int getMaLich() {
        return maLich;
    }

    public void setMaLich(int maLich) {
        this.maLich = maLich;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenBacSi() {
        return tenBacSi;
    }

    public void setTenBacSi(String tenBacSi) {
        this.tenBacSi = tenBacSi;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
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
}
