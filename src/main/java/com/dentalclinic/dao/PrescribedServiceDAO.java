package com.dentalclinic.dao;

import com.dentalclinic.model.PrescribedService;
import com.dentalclinic.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescribedServiceDAO {

    // Tách nested try block thành method riêng — fix SonarCloud java:S2093 + nested try
    private PreparedStatement prepareStatement(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps;
    }



    public void addPrescribedServices(int resultId, String[] serviceIds) {
        if (serviceIds == null || serviceIds.length == 0) return;
        String sql = "INSERT INTO prescribed_services (result_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String id : serviceIds) {
                ps.setInt(1, resultId);
                ps.setInt(2, Integer.parseInt(id));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
