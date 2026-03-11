package com.dentalclinic.dao;

import com.dentalclinic.model.Service;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    private Service mapResultSetToService(ResultSet rs) throws Exception {
        Service service = new Service();
        service.setServiceId(rs.getInt("service_id"));
        service.setServiceName(rs.getString("service_name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getDouble("price"));
        service.setDurationMinutes(rs.getObject("duration_minutes") != null ? rs.getInt("duration_minutes") : null);
        return service;
    }

    public List<Service> getAll() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Service getServiceById(int id) {
        String sql = "SELECT * FROM services WHERE service_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToService(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addService(Service service) {
        String sql = "INSERT INTO services (service_name, description, price, duration_minutes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setDouble(3, service.getPrice());
            if (service.getDurationMinutes() == null)
                ps.setNull(4, java.sql.Types.INTEGER);
            else
                ps.setInt(4, service.getDurationMinutes());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateService(Service service) {
        String sql = "UPDATE services SET service_name = ?, description = ?, price = ?, duration_minutes = ? WHERE service_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setDouble(3, service.getPrice());
            if (service.getDurationMinutes() == null)
                ps.setNull(4, java.sql.Types.INTEGER);
            else
                ps.setInt(4, service.getDurationMinutes());
            ps.setInt(5, service.getServiceId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteService(int id) {
        String sql = "DELETE FROM services WHERE service_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Service> searchServices(String keyword) {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE service_name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToService(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
