package com.dentalclinic.dao;

import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.User;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppointmentDAO {

    private static final String COLUMN_PATIENT_ID = "patient_id";
    private static final String COLUMN_DOCTOR_ID = "doctor_id";

    public static final int BOOKING_SLOT_TAKEN = -2;
    public static final int BOOKING_FAILED = -1;

    public boolean isDoctorSlotTaken(int doctorId, Date appointmentDate, java.sql.Time appointmentTime) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status <> 'Cancelled'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, appointmentDate);
            ps.setTime(3, appointmentTime);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int bookAppointmentWithServices(Appointment appt, String[] serviceIds) {
        Set<Integer> normalizedServiceIds = normalizeServiceIds(serviceIds);
        if (normalizedServiceIds.isEmpty()) {
            return BOOKING_FAILED;
        }

        String checkSql = "SELECT 1 FROM appointments "
                + "WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ? AND status <> 'Cancelled'";
        String insertAppointmentSql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes, room) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertServiceSql = "INSERT INTO appointment_services (appointment_id, service_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, appt.getDoctorId());
                checkPs.setDate(2, appt.getAppointmentDate());
                checkPs.setTime(3, appt.getAppointmentTime());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        conn.rollback();
                        return BOOKING_SLOT_TAKEN;
                    }
                }
            }

            int appointmentId;
            try (PreparedStatement ps = conn.prepareStatement(insertAppointmentSql, Statement.RETURN_GENERATED_KEYS)) {
                if (appt.getPatientId() == null) {
                    ps.setNull(1, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(1, appt.getPatientId());
                }
                ps.setInt(2, appt.getDoctorId());
                ps.setDate(3, appt.getAppointmentDate());
                ps.setTime(4, appt.getAppointmentTime());
                ps.setString(5, appt.getStatus());
                ps.setString(6, appt.getNotes());
                ps.setString(7, appt.getRoom());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return BOOKING_FAILED;
                    }
                    appointmentId = rs.getInt(1);
                }
            }

            // Set appointmentId once before the loop (loop-invariant)
            try (PreparedStatement ps = conn.prepareStatement(insertServiceSql)) {
                for (Integer serviceId : normalizedServiceIds) {
                    ps.setInt(2, serviceId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
            return appointmentId;

        } catch (SQLException e) {
            rollbackQuietly(conn);
            if (isDuplicateSlotError(e)) {
                return BOOKING_SLOT_TAKEN;
            }
            e.printStackTrace();
        } catch (Exception e) {
            rollbackQuietly(conn);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return BOOKING_FAILED;
    }

    private Set<Integer> normalizeServiceIds(String[] serviceIds) {
        Set<Integer> ids = new LinkedHashSet<>();
        if (serviceIds == null) {
            return ids;
        }
        for (String rawId : serviceIds) {
            try {
                ids.add(Integer.parseInt(rawId.trim()));
            } catch (NumberFormatException ignored) {
                // Ignored: skip invalid service IDs
            }
        }
        return ids;
    }

    private void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
                // Ignored: rollback failure is non-critical
            }
        }
    }

    private boolean isDuplicateSlotError(SQLException e) {
        return "23505".equals(e.getSQLState())
                || (e.getMessage() != null && e.getMessage().contains("UX_appointments_doctor_slot_active"));
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        if (hasColumn(rs, "appointment_id")) {
            appt.setAppointmentId(rs.getInt("appointment_id"));
        }
        if (hasColumn(rs, COLUMN_PATIENT_ID)) {
            appt.setPatientId(rs.getObject(COLUMN_PATIENT_ID) != null ? rs.getInt(COLUMN_PATIENT_ID) : null);
        }
        if (hasColumn(rs, COLUMN_DOCTOR_ID)) {
            appt.setDoctorId(rs.getObject(COLUMN_DOCTOR_ID) != null ? rs.getInt(COLUMN_DOCTOR_ID) : null);
        }
        if (hasColumn(rs, "appointment_date")) {
            appt.setAppointmentDate(rs.getDate("appointment_date"));
        }
        if (hasColumn(rs, "appointment_time")) {
            appt.setAppointmentTime(rs.getTime("appointment_time"));
        }
        if (hasColumn(rs, "status")) {
            appt.setStatus(rs.getString("status"));
        }
        if (hasColumn(rs, "notes")) {
            appt.setNotes(rs.getString("notes"));
        }
        if (hasColumn(rs, "room")) {
            appt.setRoom(rs.getString("room"));
        }
        if (hasColumn(rs, "patient_name")) {
            User patient = new User();
            patient.setUserId(appt.getPatientId());
            patient.setFullName(rs.getString("patient_name"));
            appt.setPatient(patient);
        }
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
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Appointment> getAll() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC, a.appointment_id ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Appointment getAppointmentById(int id) {
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE a.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addAppointmentReturnId(Appointment appt) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status, notes, room) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (appt.getPatientId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, appt.getPatientId());
            }
            if (appt.getDoctorId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, appt.getDoctorId());
            }
            ps.setDate(3, appt.getAppointmentDate());
            ps.setTime(4, appt.getAppointmentTime());
            ps.setString(5, appt.getStatus());
            ps.setString(6, appt.getNotes());
            ps.setString(7, appt.getRoom());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateAppointment(Appointment appt) {
        String sql = "UPDATE appointments SET doctor_id = ?, appointment_date = ?, appointment_time = ?, status = ?, notes = ?, room = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (appt.getDoctorId() == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, appt.getDoctorId());
            }
            ps.setDate(2, appt.getAppointmentDate());
            ps.setTime(3, appt.getAppointmentTime());
            ps.setString(4, appt.getStatus());
            ps.setString(5, appt.getNotes());
            ps.setString(6, appt.getRoom());
            ps.setInt(7, appt.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(int appointmentId, String status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRoom(int appointmentId, String room) {
        String sql = "UPDATE appointments SET room = ? WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room);
            ps.setInt(2, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Appointment> getAppointmentsByPatient(int patientId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "d.full_name as doctor_name FROM appointments a "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC, a.appointment_time DESC, a.appointment_id ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getAppointmentsByDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "p.full_name as patient_name FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "WHERE a.doctor_id = ? "
                + "ORDER BY CASE WHEN a.status = 'Checked In' THEN 0 "
                + " WHEN a.status = 'CONFIRMED' THEN 1 "
                + " WHEN a.status = 'Completed' THEN 2 "
                + " ELSE 3 END ASC, "
                + "a.appointment_date ASC, a.appointment_time ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countAppointmentsToday() {
        String sql = "SELECT COUNT(*) FROM appointments WHERE CAST(appointment_date AS DATE) = CAST(CURRENT_DATE AS DATE)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean executeDeleteAppointment(Connection conn, int appointmentId) throws SQLException {
        String sql1 = "DELETE FROM prescription_details WHERE prescription_id IN "
                + "(SELECT prescription_id FROM prescriptions WHERE result_id IN "
                + "(SELECT result_id FROM examination_results WHERE appointment_id = ?))";
        String sql2 = "DELETE FROM prescriptions WHERE result_id IN "
                + "(SELECT result_id FROM examination_results WHERE appointment_id = ?)";
        String sql3 = "DELETE FROM prescribed_services WHERE result_id IN "
                + "(SELECT result_id FROM examination_results WHERE appointment_id = ?)";
        String sql4 = "DELETE FROM examination_results WHERE appointment_id = ?";
        String sql5 = "DELETE FROM appointment_services WHERE appointment_id = ?";
        String sql6 = "DELETE FROM appointments WHERE appointment_id = ?";

        try (PreparedStatement ps1 = conn.prepareStatement(sql1); PreparedStatement ps2 = conn.prepareStatement(sql2); PreparedStatement ps3 = conn.prepareStatement(sql3); PreparedStatement ps4 = conn.prepareStatement(sql4); PreparedStatement ps5 = conn.prepareStatement(sql5); PreparedStatement ps6 = conn.prepareStatement(sql6)) {
            ps1.setInt(1, appointmentId);
            ps1.executeUpdate();
            ps2.setInt(1, appointmentId);
            ps2.executeUpdate();
            ps3.setInt(1, appointmentId);
            ps3.executeUpdate();
            ps4.setInt(1, appointmentId);
            ps4.executeUpdate();
            ps5.setInt(1, appointmentId);
            ps5.executeUpdate();
            ps6.setInt(1, appointmentId);
            boolean result = ps6.executeUpdate() > 0;
            return result;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            boolean result = executeDeleteAppointment(conn, appointmentId);
            conn.commit();
            return result;
        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
        } finally {
            closeQuietly(conn);
        }
        return false;
    }

    private void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Appointment> searchAppointments(String query) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE p.full_name LIKE ? OR CAST(a.appointment_id AS TEXT) LIKE ? "
                + "ORDER BY a.appointment_date DESC, a.appointment_time DESC, a.appointment_id ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + query + "%");
            ps.setString(2, "%" + query + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAppointment(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointment> getAppointmentsByDate(java.sql.Date date) {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.patient_id, a.doctor_id, a.appointment_date, a.appointment_time, a.status, a.notes, a.room, "
                + "p.full_name as patient_name, d.full_name as doctor_name "
                + "FROM appointments a "
                + "LEFT JOIN users p ON a.patient_id = p.user_id "
                + "LEFT JOIN users d ON a.doctor_id = d.user_id "
                + "WHERE a.appointment_date = ? "
                + "ORDER BY a.appointment_time ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
