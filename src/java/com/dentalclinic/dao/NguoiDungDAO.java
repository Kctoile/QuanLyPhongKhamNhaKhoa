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

        String sql
                = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, "
                + "vt.maVaiTro, vt.tenVaiTro "
                + "FROM NguoiDung nd "
                + "JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                VaiTro vaiTro = new VaiTro();
                vaiTro.setMaVaiTro(rs.getInt("maVaiTro"));
                vaiTro.setTenVaiTro(rs.getString("tenVaiTro"));

                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getInt("maND"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setMatKhau(rs.getString("matKhau"));
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

        String sql
                = "SELECT nd.maND, nd.hoTen, nd.email, nd.matKhau, "
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

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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

        String sql
                = "SELECT COUNT(*) FROM NguoiDung nd "
                + "JOIN VaiTro vt ON nd.maVaiTro = vt.maVaiTro "
                + "WHERE vt.tenVaiTro = 'DOCTOR'";

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
