package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.ExaminationResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/ViewResultServlet")
public class ViewResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // === KIỂM TRA ĐĂNG NHẬP ===
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");

        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/doctor");
            return;
        }

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(appointmentIdStr);
            if (appointmentId <= 0) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Lấy thông tin appointment
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // === KIỂM TRA QUYỀN XEM ===
        if ("CUSTOMER".equalsIgnoreCase(role)) {
            if (appointment.getPatientId() == null || !Integer.valueOf(appointment.getPatientId()).equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem kết quả này.");
                return;
            }
        } else if ("DOCTOR".equalsIgnoreCase(role)) {
            if (appointment.getDoctorId() == null || !Integer.valueOf(appointment.getDoctorId()).equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem kết quả này.");
                return;
            }
        }
        // ADMIN và STAFF được xem tất cả

        // Lấy kết quả khám
        ExaminationResult result = getExaminationResult(appointmentId);
        request.setAttribute("appointment", appointment);
        request.setAttribute("examinationResult", result);

        if (result != null) {
            com.dentalclinic.dao.PrescriptionDAO prescriptionDAO = new com.dentalclinic.dao.PrescriptionDAO();
            com.dentalclinic.model.Prescription prescription = prescriptionDAO.getPrescriptionByResultId(result.getResultId());
            request.setAttribute("prescription", prescription);
        }

        request.getRequestDispatcher("/customer_result.jsp").forward(request, response);
    }

    private ExaminationResult getExaminationResult(int appointmentId) {
        String sql = "SELECT * FROM examination_results WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
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
}
