package com.dentalclinic.model;

public class LichHen {
    private int maLH;
    private int maND;
    private int maDV;
    private String ngayHen;
    private String trangThai;
    
    // Thuộc tính bổ trợ để hiển thị (Join Table)
    private String tenKhachHang;
    private String soDienThoai;
    private String tenDichVu;

    public LichHen() {}

    // Getter và Setter
    public int getMaLH() { return maLH; }
    public void setMaLH(int maLH) { this.maLH = maLH; }
    public int getMaND() { return maND; }
    public void setMaND(int maND) { this.maND = maND; }
    public int getMaDV() { return maDV; }
    public void setMaDV(int maDV) { this.maDV = maDV; }
    public String getNgayHen() { return ngayHen; }
    public void setNgayHen(String ngayHen) { this.ngayHen = ngayHen; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
}
