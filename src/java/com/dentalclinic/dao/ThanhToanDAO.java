package com.dentalclinic.dao;
import com.dentalclinic.model.ThanhToan;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ThanhToanDAO {

    public List<ThanhToan> getAll() {
        List<ThanhToan> list = new ArrayList<>();
        String sql = "SELECT * FROM ThanhToan";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThanhToan t = new ThanhToan();
                t.setId(rs.getInt("id"));
                t.setTongTien(rs.getDouble("tongTien"));
                t.setTrangThai(rs.getString("trangThai"));
                // thêm field nếu có

                list.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public double totalRevenueToday() {
        String sql = "SELECT SUM(tongTien) FROM ThanhToan WHERE ngayThanhToan = CURDATE() AND trangThai='PAID'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
