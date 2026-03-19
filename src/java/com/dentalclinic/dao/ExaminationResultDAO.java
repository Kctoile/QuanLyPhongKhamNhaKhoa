package com.dentalclinic.dao;

import com.dentalclinic.model.ExaminationResult;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExaminationResultDAO {

    public ExaminationResult getResultByAppointmentId(int appointmentId) {
        String sql = "SELECT * FROM examination_results WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ExaminationResult er = new ExaminationResult();
                er.setResultId(rs.getInt("result_id"));
                er.setAppointmentId(rs.getInt("appointment_id"));
                er.setResultDetails(rs.getString("result_details"));
                er.setExaminationDate(rs.getTimestamp("examination_date"));
                er.setPrescription(rs.getString("prescription"));
                er.setDoctorNotes(rs.getString("doctor_notes"));
                return er;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int saveResultReturnId(ExaminationResult result) {
        String checkSql = "SELECT result_id FROM examination_results WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, result.getAppointmentId());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                // Update
                int resultId = rs.getInt("result_id");
                String updateSql = "UPDATE examination_results SET result_details = ? WHERE result_id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setString(1, result.getResultDetails());
                    updatePs.setInt(2, resultId);
                    updatePs.executeUpdate();
                }
                return resultId;
            } else {
                // Insert
                String insertSql = "INSERT INTO examination_results (appointment_id, result_details) VALUES (?, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertPs.setInt(1, result.getAppointmentId());
                    insertPs.setString(2, result.getResultDetails());
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

    public List<ExaminationResult> getResultsByPatientId(int patientId) {
        String sql = "SELECT er.result_id, er.appointment_id, er.result_details, er.examination_date, er.prescription, er.doctor_notes " +
                 "FROM examination_results er " +
                 "JOIN appointments a ON er.appointment_id = a.appointment_id " +
                 "WHERE a.patient_id = ?";
        List<ExaminationResult> results = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ExaminationResult er = new ExaminationResult();
                er.setResultId(rs.getInt("result_id"));
                er.setAppointmentId(rs.getInt("appointment_id"));
                er.setResultDetails(rs.getString("result_details"));
                er.setExaminationDate(rs.getTimestamp("examination_date"));
                er.setPrescription(rs.getString("prescription"));
                er.setDoctorNotes(rs.getString("doctor_notes"));
                results.add(er);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
