package com.dentalclinic.dao;

import com.dentalclinic.model.AppointmentService;
import com.dentalclinic.model.Service;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceDAO {

    public boolean addServicesForAppointment(int appointmentId, String[] serviceIds) {
        if (serviceIds == null || serviceIds.length == 0)
            return true;

        String sql = "INSERT INTO appointment_services (appointment_id, service_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Auto commit false for batch insert
            conn.setAutoCommit(false);
            for (String strId : serviceIds) {
                try {
                    int serviceId = Integer.parseInt(strId.trim());
                    ps.setInt(1, appointmentId);
                    ps.setInt(2, serviceId);
                    ps.addBatch();
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

    public List<Service> getServicesByAppointmentId(int appointmentId) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT s.* FROM services s "
                + "JOIN appointment_services aserv ON s.service_id = aserv.service_id "
                + "WHERE aserv.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
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

    public boolean deleteServicesForAppointment(int appointmentId) {
        String sql = "DELETE FROM appointment_services WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
