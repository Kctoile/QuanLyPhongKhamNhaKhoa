package com.dentalclinic.dao;

import com.dentalclinic.model.LichHenPaymentDTO;
import com.dentalclinic.model.ThanhToan;
import com.dentalclinic.model.ThanhToanDisplayDTO;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thao tác bảng ThanhToan và các query liên quan
 */
public class ThanhToanDAO {

    /**
     * Lấy danh sách lịch hẹn chưa có thanh toán hoặc chưa thanh toán (để Staff thực hiện thanh toán)
     */
    public List<LichHenPaymentDTO> getLichHenChuaThanhToan() {
        List<LichHenPaymentDTO> list = new ArrayList<>();
        String sql = "SELECT lh.MaLich, kh.HoTen AS TenKhachHang, bs.HoTen AS TenBacSi, dv.TenDV, dv.Gia, "
                + "lh.NgayKham, lh.GioKham, lh.TrangThai "
                + "FROM LichHen lh "
                + "JOIN NguoiDung kh ON lh.MaND = kh.MaND "
                + "LEFT JOIN NguoiDung bs ON lh.MaBacSi = bs.MaND "
                + "JOIN DichVu dv ON lh.MaDV = dv.MaDV "
                + "WHERE lh.MaLich NOT IN (SELECT MaLich FROM ThanhToan WHERE TrangThai = N'Đã thanh toán') "
                + "ORDER BY lh.NgayKham DESC, lh.GioKham DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LichHenPaymentDTO dto = new LichHenPaymentDTO();
                dto.setMaLich(rs.getInt("MaLich"));
                dto.setTenKhachHang(rs.getString("TenKhachHang"));
                dto.setTenBacSi(rs.getString("TenBacSi"));
                dto.setTenDV(rs.getString("TenDV"));
                dto.setGia(rs.getDouble("Gia"));
                dto.setNgayKham(rs.getDate("NgayKham"));
                dto.setGioKham(rs.getTime("GioKham"));
                dto.setTrangThai(rs.getString("TrangThai"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Kiểm tra lịch hẹn đã có bản ghi ThanhToan chưa (dù trạng thái gì)
     */
    public boolean daCoThanhToan(int maLich) {
        String sql = "SELECT 1 FROM ThanhToan WHERE MaLich = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maLich);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thêm thanh toán mới
     */
    public void insert(ThanhToan t) {
        String sql = "INSERT INTO ThanhToan(MaLich, TongTien, PhuongThuc, TrangThai) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getMaLich());
            ps.setDouble(2, t.getTongTien());
            ps.setString(3, t.getPhuongThuc());
            ps.setString(4, t.getTrangThai());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật trạng thái thanh toán (từ Chưa thanh toán -> Đã thanh toán)
     */
    public void updateTrangThai(int maTT, String trangThai) {
        String sql = "UPDATE ThanhToan SET TrangThai = ? WHERE MaTT = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maTT);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy thanh toán theo mã
     */
    public ThanhToan getById(int maTT) {
        String sql = "SELECT MaTT, MaLich, TongTien, PhuongThuc, TrangThai FROM ThanhToan WHERE MaTT = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maTT);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ThanhToan t = new ThanhToan();
                    t.setMaTT(rs.getInt("MaTT"));
                    t.setMaLich(rs.getInt("MaLich"));
                    t.setTongTien(rs.getDouble("TongTien"));
                    t.setPhuongThuc(rs.getString("PhuongThuc"));
                    t.setTrangThai(rs.getString("TrangThai"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách toàn bộ thanh toán kèm thông tin (Admin)
     */
    public List<ThanhToanDisplayDTO> getAllDisplay() {
        List<ThanhToanDisplayDTO> list = new ArrayList<>();
        String sql = "SELECT tt.MaTT, tt.MaLich, kh.HoTen AS TenKhachHang, dv.TenDV AS TenDichVu, "
                + "tt.TongTien, tt.PhuongThuc, tt.TrangThai "
                + "FROM ThanhToan tt "
                + "JOIN LichHen lh ON tt.MaLich = lh.MaLich "
                + "JOIN NguoiDung kh ON lh.MaND = kh.MaND "
                + "JOIN DichVu dv ON lh.MaDV = dv.MaDV "
                + "ORDER BY tt.MaTT DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThanhToanDisplayDTO dto = new ThanhToanDisplayDTO();
                dto.setMaTT(rs.getInt("MaTT"));
                dto.setMaLich(rs.getInt("MaLich"));
                dto.setTenKhachHang(rs.getString("TenKhachHang"));
                dto.setTenDichVu(rs.getString("TenDichVu"));
                dto.setTongTien(rs.getDouble("TongTien"));
                dto.setPhuongThuc(rs.getString("PhuongThuc"));
                dto.setTrangThai(rs.getString("TrangThai"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tổng doanh thu hôm nay - dựa vào NgayKham của LichHen (SQL Server)
     */
    public double totalRevenueToday() {
        String sql = "SELECT ISNULL(SUM(tt.TongTien),0) FROM ThanhToan tt "
                + "JOIN LichHen lh ON tt.MaLich = lh.MaLich "
                + "WHERE CAST(lh.NgayKham AS DATE) = CAST(GETDATE() AS DATE) "
                + "AND tt.TrangThai = N'Đã thanh toán'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
