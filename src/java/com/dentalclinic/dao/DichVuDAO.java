package com.dentalclinic.dao;

import com.dentalclinic.model.DichVu;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO thao tác với bảng DichVu
 */
public class DichVuDAO {

    /**
     * Lấy danh sách tất cả dịch vụ
     */
    public List<DichVu> getAll() {
        List<DichVu> list = new ArrayList<>();

        String sql = "SELECT maDV, tenDV, moTa, gia, thoiGianThucHien FROM dbo.DichVu";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DichVu dv = new DichVu();
                dv.setMaDV(rs.getInt("maDV"));
                dv.setTenDV(rs.getString("tenDV"));
                dv.setMoTa(rs.getString("moTa"));
                dv.setGia(rs.getInt("gia"));
                dv.setThoiGian(rs.getInt("thoiGianThucHien"));
                list.add(dv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Thêm dịch vụ mới - bảng DichVu có cột ThoiGianThucHien
     */
    public void insert(DichVu dv) {
        String sql = "INSERT INTO DichVu(TenDV, MoTa, Gia, ThoiGianThucHien) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dv.getTenDV());
            ps.setString(2, dv.getMoTa());
            ps.setDouble(3, dv.getGia());
            ps.setInt(4, dv.getThoiGian());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy dịch vụ theo mã
     */
    public DichVu getById(int maDV) {
        String sql = "SELECT maDV, tenDV, moTa, gia, thoiGianThucHien FROM dbo.DichVu WHERE maDV = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DichVu dv = new DichVu();
                    dv.setMaDV(rs.getInt("maDV"));
                    dv.setTenDV(rs.getString("tenDV"));
                    dv.setMoTa(rs.getString("moTa"));
                    dv.setGia(rs.getInt("gia"));
                    dv.setThoiGian(rs.getInt("thoiGianThucHien"));
                    return dv;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cập nhật dịch vụ - bảng DichVu có cột ThoiGianThucHien
     */
    public void update(DichVu dv) {
        String sql = "UPDATE DichVu SET TenDV=?, MoTa=?, Gia=?, ThoiGianThucHien=? WHERE MaDV=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dv.getTenDV());
            ps.setString(2, dv.getMoTa());
            ps.setInt(3, dv.getGia());
            ps.setInt(4, dv.getThoiGian());
            ps.setInt(5, dv.getMaDV());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa dịch vụ theo mã
     */
    public void delete(int maDV) {
        String sql = "DELETE FROM dbo.DichVu WHERE maDV=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maDV);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm dịch vụ theo tên hoặc mô tả
     */
    public List<DichVu> searchDichVu(String keyword) {
        List<DichVu> list = new ArrayList<>();
        String sql = "SELECT maDV, tenDV, moTa, gia, thoiGianThucHien FROM dbo.DichVu "
                + "WHERE tenDV LIKE ? OR moTa LIKE ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DichVu dv = new DichVu();
                    dv.setMaDV(rs.getInt("maDV"));
                    dv.setTenDV(rs.getString("tenDV"));
                    dv.setMoTa(rs.getString("moTa"));
                    dv.setGia(rs.getInt("gia"));
                    dv.setThoiGian(rs.getInt("thoiGianThucHien"));
                    list.add(dv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
