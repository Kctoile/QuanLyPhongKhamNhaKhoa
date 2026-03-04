package com.dentalclinic.dao;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class LichHenDAO {

    public List<LichHen> getAll() {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT * FROM LichHen";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichHen l = new LichHen();
                l.setId(rs.getInt("id"));
                l.setNgayHen(rs.getDate("ngayHen"));
                l.setTrangThai(rs.getString("trangThai"));
                // thêm các field khác nếu có

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countToday() {
        String sql = "SELECT COUNT(*) FROM LichHen WHERE ngayHen = CURDATE()";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
