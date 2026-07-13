package com.dentalclinic.dao;

import com.dentalclinic.utils.DBConnection;

import java.sql.*;

public class PrescribedServiceDAO {

    public void addPrescribedServices(int resultId, String[] serviceIds) {
        if (serviceIds == null || serviceIds.length == 0) {
            return;
        }
        String sql = "INSERT INTO prescribed_services (result_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            for (String id : serviceIds) {
                ps.setInt(2, Integer.parseInt(id));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
