package com.dentalclinic.model;

import java.util.Date;

/**
 * Model đại diện bảng LichHen
 */
public class LichHen {

    private int id;
    private Date ngayHen;
    private String trangThai;

    public LichHen() {
    }

    public LichHen(int id, Date ngayHen, String trangThai) {
        this.id = id;
        this.ngayHen = ngayHen;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getNgayHen() {
        return ngayHen;
    }

    public void setNgayHen(Date ngayHen) {
        this.ngayHen = ngayHen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
