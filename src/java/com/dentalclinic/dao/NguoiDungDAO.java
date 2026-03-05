package com.dentalclinic.dao;

import com.dentalclinic.model.NguoiDung;
import com.dentalclinic.model.VaiTro;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {

    // Lấy toàn bộ user
    public List<NguoiDung> getAllUsers() {

        List<NguoiDung> list = new ArrayList<>();

        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, "
                + "vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd "
                + "JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "ORDER BY nd.maND ASC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                VaiTro vaiTro = new VaiTro();
                vaiTro.setMaVaiTro(rs.getInt("maVaiTro"));
                vaiTro.setTenVaiTro(rs.getString("tenVaiTro"));

                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setVaiTro(vaiTro);

                list.add(nd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Login
    public NguoiDung login(String email, String matKhau) {

        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, "
                + "vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd "
                + "JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE nd.email = ? AND nd.matKhau = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, matKhau);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                VaiTro vaiTro = new VaiTro();
                vaiTro.setMaVaiTro(rs.getInt("maVaiTro"));
                vaiTro.setTenVaiTro(rs.getString("tenVaiTro"));

                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setVaiTro(vaiTro);

                return nd;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Đếm tổng user
    public int countUsers() {

        String sql = "SELECT COUNT(*) FROM NguoiDung";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Đếm số Doctor
    public int countDoctors() {

        String sql = "SELECT COUNT(*) FROM NguoiDung nd "
                + "JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE vt.tenVaiTro = 'DOCTOR'";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Lấy danh sách bác sĩ (role DOCTOR)
     */
    public List<NguoiDung> getDoctors() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE vt.tenVaiTro = 'DOCTOR' ORDER BY nd.hoTen";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getInt("maVaiTro"));
                vt.setTenVaiTro(rs.getString("tenVaiTro"));
                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setVaiTro(vt);
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách khách hàng (role CUSTOMER) - dùng cho Staff đặt lịch walk-in
     */
    public List<NguoiDung> getCustomers() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE vt.tenVaiTro = 'CUSTOMER' ORDER BY nd.hoTen";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getInt("maVaiTro"));
                vt.setTenVaiTro(rs.getString("tenVaiTro"));
                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setVaiTro(vt);
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // CRUD Methods
    public NguoiDung getUserById(int id) {
        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE nd.maND = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVaiTro(rs.getInt("maVaiTro"));
                vt.setTenVaiTro(rs.getString("tenVaiTro"));

                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setVaiTro(vt);
                return nd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(NguoiDung nd, int maVaiTro) {
        String sql = "INSERT INTO NguoiDung (HoTen, Email, MatKhau, SoDienThoai, MaVaiTro) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getMatKhau());
            ps.setString(4, nd.getSoDienThoai());
            ps.setInt(5, maVaiTro);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(NguoiDung nd, int maVaiTro) {
        String sql = "UPDATE NguoiDung SET HoTen = ?, Email = ?, MatKhau = ?, SoDienThoai = ?, MaVaiTro = ? WHERE MaND = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getMatKhau());
            ps.setString(4, nd.getSoDienThoai());
            ps.setInt(5, maVaiTro);
            ps.setInt(6, nd.getMaND());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int maND) {
        // Must delete related records if they exist (LichHen, etc.) to avoid foreign
        // key constraints
        // For simplicity, we just delete the user here. A robust system would
        // soft-delete or cascade.
        String sql = "DELETE FROM NguoiDung WHERE MaND = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maND);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm kiếm bác sĩ theo họ tên
     */
    public List<NguoiDung> searchDoctors(String keyword) {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, nd.soDienThoai, vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE vt.tenVaiTro = 'DOCTOR' AND nd.hoTen LIKE ? ORDER BY nd.hoTen";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VaiTro vt = new VaiTro();
                    vt.setMaVaiTro(rs.getInt("maVaiTro"));
                    vt.setTenVaiTro(rs.getString("tenVaiTro"));

                    NguoiDung nd = new NguoiDung();
                    nd.setMaND(rs.getInt("maND"));
                    nd.setHoTen(rs.getString("hoTen"));
                    nd.setEmail(rs.getString("email"));
                    nd.setMatKhau(rs.getString("matKhau"));
                    nd.setSoDienThoai(rs.getString("soDienThoai"));
                    nd.setVaiTro(vt);

                    list.add(nd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
