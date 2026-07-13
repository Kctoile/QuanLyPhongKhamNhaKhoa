package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ConfirmAppointmentServlet")
public class ConfirmAppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final transient AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // === ROLE CHECK ===
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        if (role == null || (!"STAFF".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff");
            return;
        }
        int appointmentId = Integer.parseInt(appointmentIdStr);
        appointmentDAO.updateStatus(appointmentId, "CONFIRMED");
        response.sendRedirect(request.getContextPath() + "/staff");
    }
}
