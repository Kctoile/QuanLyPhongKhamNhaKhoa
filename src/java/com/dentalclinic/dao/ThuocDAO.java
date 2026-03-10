package com.dentalclinic.dao;

import com.dentalclinic.model.Thuoc;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DAO thao tác với bảng Thuoc
 */
public class ThuocDAO {

    /**
     * Đếm tổng số thuốc trong hệ thống
     */
    public int countThuoc() {
        String sql = "SELECT COUNT(*) FROM Thuoc";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Lấy danh sách tất cả thuốc
     */
    public List<Thuoc> getAll() {
        List<Thuoc> list = new ArrayList<>();
        String sql = "SELECT MaThuoc, TenThuoc, DonGia, SoLuongTon FROM Thuoc";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Thuoc t = new Thuoc();
                t.setMaThuoc(rs.getInt("MaThuoc"));
                t.setTenThuoc(rs.getString("TenThuoc"));
                t.setDonGia(rs.getDouble("DonGia"));
                t.setSoLuongTon(rs.getInt("SoLuongTon"));
                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Lấy thuốc theo mã
     */
    public Thuoc getById(int maThuoc) {
        String sql = "SELECT MaThuoc, TenThuoc, DonGia, SoLuongTon FROM Thuoc WHERE MaThuoc = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maThuoc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Thuoc t = new Thuoc();
                    t.setMaThuoc(rs.getInt("MaThuoc"));
                    t.setTenThuoc(rs.getString("TenThuoc"));
                    t.setDonGia(rs.getDouble("DonGia"));
                    t.setSoLuongTon(rs.getInt("SoLuongTon"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy đơn giá theo danh sách mã thuốc (tối ưu: tránh N+1 query)
     */
    public Map<Integer, Double> getDonGiaMapByIds(Set<Integer> maThuocIds) {
        Map<Integer, Double> map = new HashMap<>();
        if (maThuocIds == null || maThuocIds.isEmpty()) {
            return map;
        }

        StringBuilder sb = new StringBuilder("SELECT MaThuoc, DonGia FROM Thuoc WHERE MaThuoc IN (");
        int i = 0;
        for (Integer ignored : maThuocIds) {
            if (i++ > 0) sb.append(",");
            sb.append("?");
        }
        sb.append(")");

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            int idx = 1;
            for (Integer id : maThuocIds) {
                ps.setInt(idx++, id);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getInt("MaThuoc"), rs.getDouble("DonGia"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * Thêm thuốc mới
     */
    public void insert(Thuoc t) {
        String sql = "INSERT INTO Thuoc(TenThuoc, DonGia, SoLuongTon) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTenThuoc());
            ps.setDouble(2, t.getDonGia());
            ps.setInt(3, t.getSoLuongTon());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật thuốc
     */
    public void update(Thuoc t) {
        String sql = "UPDATE Thuoc SET TenThuoc=?, DonGia=?, SoLuongTon=? WHERE MaThuoc=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTenThuoc());
            ps.setDouble(2, t.getDonGia());
            ps.setInt(3, t.getSoLuongTon());
            ps.setInt(4, t.getMaThuoc());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa thuốc theo mã
     */
    public void delete(int maThuoc) {
        String sql = "DELETE FROM Thuoc WHERE MaThuoc=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maThuoc);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
