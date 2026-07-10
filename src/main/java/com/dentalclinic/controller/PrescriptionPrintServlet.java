package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.ExaminationResultDAO;
import com.dentalclinic.dao.PrescriptionDAO;
import com.dentalclinic.dao.PrescriptionDetailDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.ExaminationResult;
import com.dentalclinic.model.Prescription;
import com.dentalclinic.model.PrescriptionDetail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/prescription-print")
public class PrescriptionPrintServlet extends HttpServlet {

    private final transient AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final transient ExaminationResultDAO examinationResultDAO = new ExaminationResultDAO();
    private final transient PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final transient PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Appointment appointment = getValidAppointment(request, response);
        if (appointment == null) {
            return;
        }

        String role = getRole(session);
        Integer userId = (Integer) session.getAttribute("userId");

        if (!isAuthorized(role, userId, appointment, response)) {
            return;
        }

        loadPrescriptionData(request, appointment);
        request.getRequestDispatcher("/prescription_print.jsp").forward(request, response);
    }

    private Appointment getValidAppointment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (Exception e) {
            response.sendRedirect("appointments");
            return null;
        }

        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            response.sendRedirect("appointments");
            return null;
        }
        return appointment;
    }

    private String getRole(HttpSession session) {
        Object role = session.getAttribute("role");
        return role == null ? "" : role.toString().toUpperCase().trim();
    }

    private boolean isAuthorized(String role, Integer userId, Appointment appointment, HttpServletResponse response) throws IOException {
        if ("CUSTOMER".equals(role)) {
            if (appointment.getPatientId() == null || !appointment.getPatientId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem đơn thuốc này.");
                return false;
            }
        } else if ("DOCTOR".equals(role)) {
            if (appointment.getDoctorId() == null || !appointment.getDoctorId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem đơn thuốc này.");
                return false;
            }
        } else if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Quyền truy cập bị từ chối.");
            return false;
        }
        return true;
    }

    private void loadPrescriptionData(HttpServletRequest request, Appointment appointment) {
        int appointmentId = appointment.getAppointmentId();
        ExaminationResult result = examinationResultDAO.getResultByAppointmentId(appointmentId);
        Prescription prescription = null;
        List<PrescriptionDetail> details = null;

        if (result != null) {
            prescription = prescriptionDAO.getPrescriptionByResultId(result.getResultId());
            if (prescription != null) {
                details = prescriptionDetailDAO.getDetailsByPrescriptionId(prescription.getPrescriptionId());
            }
        }

        request.setAttribute("appointment", appointment);
        request.setAttribute("examinationResult", result);
        request.setAttribute("prescription", prescription);
        request.setAttribute("details", details);
    }
}
