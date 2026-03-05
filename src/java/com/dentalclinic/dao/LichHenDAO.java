package com.dentalclinic.dao;

import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.LichHenDisplayDTO;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO bảng LichHen
 */
public class LichHenDAO {

    /** Các khung giờ 30 phút: 8:00 - 20:00 */
    private static final String[] TIME_SLOTS = {
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
        "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00"
    };

    /**
     * Lấy danh sách giờ đã được đặt của bác sĩ trong ngày
     */
    public List<String> getBookedTimes(int maBacSi, Date ngayKham) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT CONVERT(VARCHAR(5), GioKham, 108) AS gio FROM LichHen "
                + "WHERE MaBacSi = ? AND NgayKham = ? AND TrangThai NOT IN (N'Đã hủy')";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBacSi);
            ps.setDate(2, ngayKham);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("gio").substring(0, 5)); // HH:mm
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách khung giờ còn trống
     */
    public List<String> getAvailableSlots(int maBacSi, Date ngayKham) {
        List<String> booked = getBookedTimes(maBacSi, ngayKham);
        List<String> available = new ArrayList<>();
        for (String slot : TIME_SLOTS) {
            if (!booked.contains(slot)) {
                available.add(slot);
            }
        }
        return available;
    }

    /**
     * Thêm lịch hẹn mới
     */
    public void insert(LichHen lh) {
        String sql = "INSERT INTO LichHen(MaND, MaBacSi, MaDV, NgayKham, GioKham, TrangThai, GhiChu) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lh.getMaND());
            ps.setObject(2, lh.getMaBacSi());
            ps.setInt(3, lh.getMaDV());
            ps.setDate(4, lh.getNgayKham());
            ps.setTime(5, lh.getGioKham());
            ps.setString(6, lh.getTrangThai() != null ? lh.getTrangThai() : "Chờ xác nhận");
            ps.setString(7, lh.getGhiChu());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy tất cả lịch hẹn với tên khách, bác sĩ, dịch vụ (Admin)
     */
    public List<LichHenDisplayDTO> getAllDisplay() {
        List<LichHenDisplayDTO> list = new ArrayList<>();
        String sql = "SELECT lh.MaLich, kh.HoTen AS TenKhachHang, bs.HoTen AS TenBacSi, dv.TenDV AS TenDichVu, "
                + "lh.NgayKham, lh.GioKham, lh.TrangThai, lh.GhiChu "
                + "FROM LichHen lh "
                + "JOIN NguoiDung kh ON lh.MaND = kh.MaND "
                + "LEFT JOIN NguoiDung bs ON lh.MaBacSi = bs.MaND "
                + "JOIN DichVu dv ON lh.MaDV = dv.MaDV "
                + "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichHenDisplayDTO dto = new LichHenDisplayDTO();
                dto.setMaLich(rs.getInt("MaLich"));
                dto.setTenKhachHang(rs.getString("TenKhachHang"));
                dto.setTenBacSi(rs.getString("TenBacSi"));
                dto.setTenDichVu(rs.getString("TenDichVu"));
                dto.setNgayKham(rs.getDate("NgayKham"));
                dto.setGioKham(rs.getTime("GioKham"));
                dto.setTrangThai(rs.getString("TrangThai"));
                dto.setGhiChu(rs.getString("GhiChu"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy tất cả lịch hẹn (raw)
     */
    public List<LichHen> getAll() {
        List<LichHen> list = new ArrayList<>();
        String sql = "SELECT MaLich, MaND, MaBacSi, MaDV, NgayKham, GioKham, TrangThai, GhiChu FROM LichHen ORDER BY NgayKham DESC, GioKham DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LichHen l = mapResultSet(rs);
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy lịch hẹn theo mã
     */
    public LichHen getById(int maLich) {
        String sql = "SELECT MaLich, MaND, MaBacSi, MaDV, NgayKham, GioKham, TrangThai, GhiChu FROM LichHen WHERE MaLich = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLich);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật trạng thái lịch hẹn (ví dụ: Đã vào khám)
     */
    public void updateTrangThai(int maLich, String trangThai) {
        String sql = "UPDATE LichHen SET TrangThai = ? WHERE MaLich = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maLich);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lịch hẹn của bác sĩ - trạng thái Đã vào khám (để khám) hoặc Đã khám xong
     */
    public List<LichHenDisplayDTO> getByMaBacSi(int maBacSi) {
        List<LichHenDisplayDTO> list = new ArrayList<>();
        String sql = "SELECT lh.MaLich, kh.HoTen AS TenKhachHang, bs.HoTen AS TenBacSi, dv.TenDV AS TenDichVu, "
                + "lh.NgayKham, lh.GioKham, lh.TrangThai, lh.GhiChu "
                + "FROM LichHen lh "
                + "JOIN NguoiDung kh ON lh.MaND = kh.MaND "
                + "LEFT JOIN NguoiDung bs ON lh.MaBacSi = bs.MaND "
                + "JOIN DichVu dv ON lh.MaDV = dv.MaDV "
                + "WHERE lh.MaBacSi = ? AND lh.TrangThai IN (N'Đã vào khám', N'Đã khám xong') "
                + "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBacSi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LichHenDisplayDTO dto = new LichHenDisplayDTO();
                    dto.setMaLich(rs.getInt("MaLich"));
                    dto.setTenKhachHang(rs.getString("TenKhachHang"));
                    dto.setTenBacSi(rs.getString("TenBacSi"));
                    dto.setTenDichVu(rs.getString("TenDichVu"));
                    dto.setNgayKham(rs.getDate("NgayKham"));
                    dto.setGioKham(rs.getTime("GioKham"));
                    dto.setTrangThai(rs.getString("TrangThai"));
                    dto.setGhiChu(rs.getString("GhiChu"));
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countToday() {
        String sql = "SELECT COUNT(*) FROM LichHen WHERE CAST(NgayKham AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private LichHen mapResultSet(ResultSet rs) throws SQLException {
        LichHen l = new LichHen();
        l.setMaLich(rs.getInt("MaLich"));
        l.setMaND(rs.getInt("MaND"));
        l.setMaBacSi(rs.getObject("MaBacSi") != null ? rs.getInt("MaBacSi") : null);
        l.setMaDV(rs.getInt("MaDV"));
        l.setNgayKham(rs.getDate("NgayKham"));
        l.setGioKham(rs.getTime("GioKham"));
        l.setTrangThai(rs.getString("TrangThai"));
        l.setGhiChu(rs.getString("GhiChu"));
        return l;
    }
}
