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

}
