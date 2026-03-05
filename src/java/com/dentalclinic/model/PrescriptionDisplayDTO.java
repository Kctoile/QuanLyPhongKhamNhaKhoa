package com.dentalclinic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO hiển thị đơn thuốc kèm chi tiết - dùng cho trang mua thuốc
 */
public class PrescriptionDisplayDTO {

    private int maDon;
    private int maLich;
    private String huongDan;
    private java.util.Date ngayKham;
    private List<ChiTietThuocDTO> chiTiet = new ArrayList<>();

    public int getMaDon() { return maDon; }
    public void setMaDon(int maDon) { this.maDon = maDon; }
    public int getMaLich() { return maLich; }
    public void setMaLich(int maLich) { this.maLich = maLich; }
    public String getHuongDan() { return huongDan; }
    public void setHuongDan(String huongDan) { this.huongDan = huongDan; }
    public java.util.Date getNgayKham() { return ngayKham; }
    public void setNgayKham(java.util.Date ngayKham) { this.ngayKham = ngayKham; }
    public List<ChiTietThuocDTO> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietThuocDTO> chiTiet) { this.chiTiet = chiTiet; }
    public void addChiTiet(ChiTietThuocDTO ct) { chiTiet.add(ct); }

    public static class ChiTietThuocDTO {
        private int maThuoc;
        private String tenThuoc;
        private int soLuong;
        private double donGia;
        public int getMaThuoc() { return maThuoc; }
        public void setMaThuoc(int maThuoc) { this.maThuoc = maThuoc; }
        public String getTenThuoc() { return tenThuoc; }
        public void setTenThuoc(String tenThuoc) { this.tenThuoc = tenThuoc; }
        public int getSoLuong() { return soLuong; }
        public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
        public double getDonGia() { return donGia; }
        public void setDonGia(double donGia) { this.donGia = donGia; }
    }
}
