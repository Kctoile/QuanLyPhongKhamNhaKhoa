package com.dentalclinic.dao;

import com.dentalclinic.model.Service;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceDAO {

    public boolean addServicesForAppointment(int appointmentId, String[] serviceIds) {
        if (serviceIds == null || serviceIds.length == 0) {
            return true;
        }
        String sql = "INSERT INTO appointment_services (appointment_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, appointmentId);
            for (String strId : serviceIds) {
                Integer serviceId = tryParseInt(strId);
                if (serviceId != null) {
                    ps.setInt(2, serviceId);
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Integer tryParseInt(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public List<Service> getServicesByAppointmentId(int appointmentId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.service_id, s.service_name, s.description, s.price, s.duration_minutes FROM services s "
                + "JOIN appointment_services aserv ON s.service_id = aserv.service_id "
                + "WHERE aserv.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Service s = new Service();
                s.setServiceId(rs.getInt("service_id"));
                s.setServiceName(rs.getString("service_name"));
                s.setDescription(rs.getString("description"));
                s.setPrice(rs.getBigDecimal("price"));
                s.setDurationMinutes(rs.getObject("duration_minutes") != null ? rs.getInt("duration_minutes") : null);
                services.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public boolean deleteServicesForAppointment(int appointmentId) {
        String sql = "DELETE FROM appointment_services WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
