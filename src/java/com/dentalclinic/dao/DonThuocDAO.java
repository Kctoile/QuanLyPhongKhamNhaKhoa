package com.dentalclinic.dao;

import com.dentalclinic.model.ChiTietDonThuoc;
import com.dentalclinic.model.DonThuoc;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO DonThuoc và ChiTietDonThuoc
 */
public class DonThuocDAO {

    /**
     * Thêm đơn thuốc, trả về MaDon
     */
    public int insertDonThuoc(DonThuoc dt) {
        String sql = "INSERT INTO DonThuoc(MaKQ, HuongDan) OUTPUT INSERTED.MaDon VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dt.getMaKQ());
            ps.setString(2, dt.getHuongDan());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Thêm chi tiết đơn thuốc (1 loại thuốc)
     */
    public void insertChiTiet(ChiTietDonThuoc ct) {
        String sql = "INSERT INTO ChiTietDonThuoc(MaDon, MaThuoc, SoLuong, DonGia) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ct.getMaDon());
            ps.setInt(2, ct.getMaThuoc());
            ps.setInt(3, ct.getSoLuong());
            ps.setDouble(4, ct.getDonGia());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách chi tiết đơn thuốc theo MaDon (kèm tên thuốc)
     */
    public List<Object[]> getChiTietByMaDon(int maDon) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT ct.MaDon, ct.MaThuoc, t.TenThuoc, ct.SoLuong, ct.DonGia "
                + "FROM ChiTietDonThuoc ct JOIN Thuoc t ON ct.MaThuoc = t.MaThuoc WHERE ct.MaDon = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDon);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("MaDon"), rs.getInt("MaThuoc"), rs.getString("TenThuoc"),
                        rs.getInt("SoLuong"), rs.getDouble("DonGia")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy đơn thuốc của khách hàng (qua LichHen -> KetQuaKham -> DonThuoc)
     * Trả về List[MaDon, MaKQ, MaLich, HuongDan, NgayKham]
     */
    public List<Object[]> getDonThuocByMaND(int maND) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT dt.MaDon, dt.MaKQ, kq.MaLich, dt.HuongDan, kq.NgayKham "
                + "FROM DonThuoc dt JOIN KetQuaKham kq ON dt.MaKQ = kq.MaKQ "
                + "JOIN LichHen lh ON kq.MaLich = lh.MaLich WHERE lh.MaND = ? ORDER BY kq.NgayKham DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maND);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{rs.getInt("MaDon"), rs.getInt("MaKQ"), rs.getInt("MaLich"), rs.getString("HuongDan"), rs.getTimestamp("NgayKham")});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public DonThuoc getByMaKQ(int maKQ) {
        String sql = "SELECT MaDon, MaKQ, HuongDan FROM DonThuoc WHERE MaKQ = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKQ);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DonThuoc dt = new DonThuoc();
                    dt.setMaDon(rs.getInt("MaDon"));
                    dt.setMaKQ(rs.getInt("MaKQ"));
                    dt.setHuongDan(rs.getString("HuongDan"));
                    return dt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
