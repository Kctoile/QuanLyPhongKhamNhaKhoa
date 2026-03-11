package com.dentalclinic.dao;

import com.dentalclinic.model.ClinicConfig;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClinicConfigDAO {

    public ClinicConfig getConfig() {
        String sql = "SELECT * FROM clinic_configs";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ClinicConfig config = new ClinicConfig();
                config.setConfigId(rs.getInt("config_id"));
                config.setOpeningTime(rs.getTime("opening_time"));
                config.setClosingTime(rs.getTime("closing_time"));
                config.setClinicInfo(rs.getString("clinic_info"));
                return config;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateConfig(ClinicConfig config) {
        // Assume there is only one row with config_id = 1, create if not exist
        String checkSql = "SELECT COUNT(*) FROM clinic_configs";
        boolean exists = false;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(checkSql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0)
                exists = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql;
        if (exists) {
            sql = "UPDATE clinic_configs SET opening_time = ?, closing_time = ?, clinic_info = ?";
        } else {
            sql = "INSERT INTO clinic_configs (opening_time, closing_time, clinic_info) VALUES (?, ?, ?)";
        }

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, config.getOpeningTime());
            ps.setTime(2, config.getClosingTime());
            ps.setString(3, config.getClinicInfo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
