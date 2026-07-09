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

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ExaminationResultDAO examinationResultDAO = new ExaminationResultDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final PrescriptionDetailDAO prescriptionDetailDAO = new PrescriptionDetailDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String role = session.getAttribute("role") != null 
                ? session.getAttribute("role").toString().toUpperCase().trim() 
                : "";
        Integer userId = (Integer) session.getAttribute("userId");

        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (Exception e) {
            response.sendRedirect("appointments");
            return;
        }

        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            response.sendRedirect("appointments");
            return;
        }

        // Authorization check
        if ("CUSTOMER".equals(role)) {
            if (appointment.getPatientId() == null || !appointment.getPatientId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem đơn thuốc này.");
                return;
            }
        } else if ("DOCTOR".equals(role)) {
            if (appointment.getDoctorId() == null || !appointment.getDoctorId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem đơn thuốc này.");
                return;
            }
        } else if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Quyền truy cập bị từ chối.");
            return;
        }

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

        request.getRequestDispatcher("/prescription_print.jsp").forward(request, response);
    }
}
