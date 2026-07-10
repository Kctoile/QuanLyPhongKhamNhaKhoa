package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.PrescriptionDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.ExaminationResult;
import com.dentalclinic.model.Prescription;
import com.dentalclinic.utils.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/ViewResultServlet")
public class ViewResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_DOCTOR = "DOCTOR";
    private static final String LOGIN_JSP = "/login.jsp";
    private static final String DOCTOR_URL = "/doctor";
    private static final String RESULT_JSP = "/customer_result.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect(LOGIN_JSP);
            return;
        }
        String role = (String) session.getAttribute("role");
        Integer userId = (Integer) session.getAttribute("userId");
        String appointmentIdStr = request.getParameter("appointmentId");

        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + DOCTOR_URL);
            return;
        }

        int appointmentId = parseAppointmentId(appointmentIdStr, response, request);
        if (appointmentId <= 0) {
            return;
        }

        AppointmentDAO appointmentDAO = new AppointmentDAO();
        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            response.sendRedirect(LOGIN_JSP);
            return;
        }

        if (!checkAccessPermission(role, userId, appointment)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem kết quả này.");
            return;
        }

        ExaminationResult result = getExaminationResult(appointmentId);
        request.setAttribute("appointment", appointment);
        request.setAttribute("examinationResult", result);
        if (result != null) {
            PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
            Prescription prescription = prescriptionDAO.getPrescriptionByResultId(result.getResultId());
            request.setAttribute("prescription", prescription);
        }
        request.getRequestDispatcher(RESULT_JSP).forward(request, response);
    }

    private int parseAppointmentId(String idStr, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            if (id <= 0) {
                response.sendRedirect(LOGIN_JSP);
                return -1;
            }
            return id;
        } catch (NumberFormatException e) {
            response.sendRedirect(LOGIN_JSP);
            return -1;
        }
    }

    private boolean checkAccessPermission(String role, Integer userId, Appointment appointment) {
        if (ROLE_CUSTOMER.equalsIgnoreCase(role)) {
            return appointment.getPatientId() != null && appointment.getPatientId().equals(userId);
        } else if (ROLE_DOCTOR.equalsIgnoreCase(role)) {
            return appointment.getDoctorId() != null && appointment.getDoctorId().equals(userId);
        }
        return true;
    }

    private ExaminationResult getExaminationResult(int appointmentId) {
        String sql = "SELECT result_id, appointment_id, result_details, examination_date, prescription, doctor_notes FROM examination_results WHERE appointment_id = ?";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
