package com.dentalclinic.model;

/**
 * DTO hiển thị thanh toán kèm thông tin lịch hẹn - dùng cho Admin và Staff
 */
public class ThanhToanDisplayDTO {

    private int maTT;
    private int maLich;
    private String tenKhachHang;
    private String tenDichVu;
    private double tongTien;
    private String phuongThuc;
    private String trangThai;

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

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
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
