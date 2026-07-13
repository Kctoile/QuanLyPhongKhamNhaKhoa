package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.PaymentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.Payment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/invoice-print")
public class InvoicePrintServlet extends HttpServlet {

    private final transient AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final transient PaymentDAO paymentDAO = new PaymentDAO();

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
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem hóa đơn này.");
                return;
            }
        } else if ("DOCTOR".equals(role)) {
            if (appointment.getDoctorId() == null || !appointment.getDoctorId().equals(userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem hóa đơn này.");
                return;
            }
        } else if (!"ADMIN".equals(role) && !"STAFF".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Quyền truy cập bị từ chối.");
            return;
        }
        Payment payment = paymentDAO.getByAppointmentId(appointmentId);
        List items = paymentDAO.getPaymentItems(appointmentId);
        BigDecimal amount = paymentDAO.calculateAppointmentAmount(appointmentId);
        request.setAttribute("appointment", appointment);
        request.setAttribute("payment", payment);
        request.setAttribute("items", items);
        request.setAttribute("amount", amount);
        request.getRequestDispatcher("/invoice_print.jsp").forward(request, response);
    }
}
