package com.dentalclinic.model;

import java.sql.Date;
import java.sql.Time;

/**
 * DTO hiển thị lịch hẹn kèm tên khách, bác sĩ, dịch vụ
 */
public class LichHenDisplayDTO {

    private int maLich;
    private String tenKhachHang;
    private String email;
    private String soDienThoai;
    private String tenBacSi;
    private String tenDichVu;
    private Date ngayKham;
    private Time gioKham;
    private String trangThai;
    private String ghiChu;

    public int getMaLich() { return maLich; }
    public void setMaLich(int maLich) { this.maLich = maLich; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getTenBacSi() { return tenBacSi; }
    public void setTenBacSi(String tenBacSi) { this.tenBacSi = tenBacSi; }
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    public Date getNgayKham() { return ngayKham; }
    public void setNgayKham(Date ngayKham) { this.ngayKham = ngayKham; }
    public Time getGioKham() { return gioKham; }
    public void setGioKham(Time gioKham) { this.gioKham = gioKham; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
