package com.dentalclinic.model;

import java.util.Date;

/**
 * Model bảng KetQuaKham: MaKQ, MaLich, KetQua, NgayKham
 */
public class KetQuaKham {

    private int maKQ;
    private int maLich;
    private String ketQua;
    private java.util.Date ngayKham;

    public int getMaKQ() { return maKQ; }
    public void setMaKQ(int maKQ) { this.maKQ = maKQ; }
    public int getMaLich() { return maLich; }
    public void setMaLich(int maLich) { this.maLich = maLich; }
    public String getKetQua() { return ketQua; }
    public void setKetQua(String ketQua) { this.ketQua = ketQua; }
    public java.util.Date getNgayKham() { return ngayKham; }
    public void setNgayKham(java.util.Date ngayKham) { this.ngayKham = ngayKham; }
}
