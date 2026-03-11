package com.dentalclinic.dao;

import com.dentalclinic.model.PrescribedService;
import com.dentalclinic.model.Service;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrescribedServiceDAO {

    public boolean addPrescribedServices(int resultId, String[] serviceIds) {
        if (serviceIds == null || serviceIds.length == 0)
            return true;

        String sql = "INSERT INTO prescribed_services (result_id, service_id, status, notes) VALUES (?, ?, 'Pending', '')";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (String strId : serviceIds) {
                try {
                    int serviceId = Integer.parseInt(strId.trim());
                    // Check if exists first to avoid PK violation
                    if (!isServicePrescribed(conn, resultId, serviceId)) {
                        ps.setInt(1, resultId);
                        ps.setInt(2, serviceId);
                        ps.addBatch();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isServicePrescribed(Connection conn, int resultId, int serviceId) throws Exception {
        String checkSql = "SELECT 1 FROM prescribed_services WHERE result_id = ? AND service_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, resultId);
            ps.setInt(2, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Service> getPrescribedServicesByResultId(int resultId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.* FROM services s "
                + "JOIN prescribed_services ps ON s.service_id = ps.service_id "
                + "WHERE ps.result_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getInt("service_id"));
                s.setServiceName(rs.getString("service_name"));
                s.setDescription(rs.getString("description"));
                s.setPrice(rs.getDouble("price"));
                s.setDurationMinutes(rs.getObject("duration_minutes") != null ? rs.getInt("duration_minutes") : null);
                services.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }
}
