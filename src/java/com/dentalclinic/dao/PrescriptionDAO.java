package com.dentalclinic.dao;

import com.dentalclinic.model.Prescription;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PrescriptionDAO {

    public Prescription getPrescriptionByResultId(int resultId) {
        String sql = "SELECT * FROM prescriptions WHERE result_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, resultId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Prescription p = new Prescription();
                p.setPrescriptionId(rs.getInt("prescription_id"));
                p.setResultId(rs.getInt("result_id"));
                p.setInstructions(rs.getString("instructions"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int savePrescriptionReturnId(Prescription p) {
        String checkSql = "SELECT prescription_id FROM prescriptions WHERE result_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, p.getResultId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                int pId = rs.getInt("prescription_id");
                String updateSql = "UPDATE prescriptions SET instructions = ? WHERE prescription_id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setString(1, p.getInstructions());
                    updatePs.setInt(2, pId);
                    updatePs.executeUpdate();
                }
                return pId;
            } else {
                String insertSql = "INSERT INTO prescriptions (result_id, instructions) VALUES (?, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertPs.setInt(1, p.getResultId());
                    insertPs.setString(2, p.getInstructions());
                    insertPs.executeUpdate();
                    try (ResultSet keys = insertPs.getGeneratedKeys()) {
                        if (keys.next())
                            return keys.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
