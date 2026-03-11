package com.dentalclinic.dao;

import com.dentalclinic.model.Role;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {
    public List<Role> getAll() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT role_id, role_name FROM roles";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Role(rs.getInt("role_id"), rs.getString("role_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer getRoleIdByName(String roleName) {
        String sql = "SELECT role_id FROM roles WHERE role_name = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
