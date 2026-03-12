package com.dentalclinic.dao;

import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.User;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    // Kiểm tra bác sĩ đã có lịch vào thời gian này chưa
    public boolean isDoctorSlotTaken(int doctorId, Date appointmentDate, java.sql.Time appointmentTime) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, appointmentDate);
            ps.setTime(3, appointmentTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws Exception {
        Appointment appt = new Appointment();
        appt.setAppointmentId(rs.getInt("appointment_id"));
        appt.setPatientId(rs.getObject("patient_id") != null ? rs.getInt("patient_id") : null);
        appt.setDoctorId(rs.getObject("doctor_id") != null ? rs.getInt("doctor_id") : null);
        appt.setAppointmentDate(rs.getDate("appointment_date"));
        appt.setAppointmentTime(rs.getTime("appointment_time"));
        appt.setStatus(rs.getString("status"));
        appt.setNotes(rs.getString("notes"));
        appt.setRoom(rs.getString("room"));

        // Map Patient object if joined
        if (hasColumn(rs, "patient_name")) {
            User patient = new User();
            patient.setUserId(appt.getPatientId());
            patient.setFullName(rs.getString("patient_name"));
            appt.setPatient(patient);
        }

        // Map Doctor object if joined
        if (hasColumn(rs, "doctor_name")) {
            User doctor = new User();
            doctor.setUserId(appt.getDoctorId());
            doctor.setFullName(rs.getString("doctor_name"));
            appt.setDoctor(doctor);
        }

        return appt;
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Appointment getAppointmentById(int id) {
        String sql = "SELECT a.*, p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE a.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addAppointmentReturnId(Appointment appt) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes, room) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            if (appt.getPatientId() == null)
                ps.setNull(1, java.sql.Types.INTEGER);
            else
                ps.setInt(1, appt.getPatientId());
            if (appt.getDoctorId() == null)
                ps.setNull(2, java.sql.Types.INTEGER);
            else
                ps.setInt(2, appt.getDoctorId());
            ps.setDate(3, appt.getAppointmentDate());
            ps.setTime(4, appt.getAppointmentTime());
            ps.setString(5, appt.getStatus());
            ps.setString(6, appt.getNotes());
            ps.setString(7, appt.getRoom());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next())
                        return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ?, status = ?, notes = ?, room = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            if (appt.getPatientId() == null)
                ps.setNull(1, java.sql.Types.INTEGER);
            else
                ps.setInt(1, appt.getPatientId());
            if (appt.getDoctorId() == null)
                ps.setNull(2, java.sql.Types.INTEGER);
            else
                ps.setInt(2, appt.getDoctorId());
            ps.setDate(3, appt.getAppointmentDate());
            ps.setTime(4, appt.getAppointmentTime());
            ps.setString(5, appt.getStatus());
            ps.setString(6, appt.getNotes());
            ps.setString(7, appt.getRoom());
            ps.setInt(8, appt.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRoom(int appointmentId, String room) {
        String sql = "UPDATE appointments SET room = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, d.full_name as doctor_name FROM appointments a "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name as patient_name FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "WHERE a.doctor_id = ? ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countAppointmentsToday() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE CAST(appointment_date AS DATE) = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Appointment> searchAppointments(String query) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE p.full_name LIKE ? OR CAST(a.appointment_id AS VARCHAR) LIKE ? "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
