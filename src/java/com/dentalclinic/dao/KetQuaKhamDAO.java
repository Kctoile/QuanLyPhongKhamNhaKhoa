package com.dentalclinic.dao;

import com.dentalclinic.model.KetQuaKham;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;

/**
 * DAO bảng KetQuaKham
 */
public class KetQuaKhamDAO {

    /**
     * Thêm kết quả khám, trả về MaKQ vừa tạo
     */
    public int insert(KetQuaKham kq) {
        String sql = "INSERT INTO KetQuaKham(MaLich, KetQua) OUTPUT INSERTED.MaKQ VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kq.getMaLich());
            ps.setString(2, kq.getKetQua());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public KetQuaKham getByMaLich(int maLich) {
        String sql = "SELECT MaKQ, MaLich, KetQua, NgayKham FROM KetQuaKham WHERE MaLich = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLich);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KetQuaKham kq = new KetQuaKham();
                    kq.setMaKQ(rs.getInt("MaKQ"));
                    kq.setMaLich(rs.getInt("MaLich"));
                    kq.setKetQua(rs.getString("KetQua"));
                    kq.setNgayKham(rs.getTimestamp("NgayKham"));
                    return kq;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
