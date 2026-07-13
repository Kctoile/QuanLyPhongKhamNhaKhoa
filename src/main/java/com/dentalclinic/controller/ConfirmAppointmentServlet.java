package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/ConfirmAppointmentServlet")
public class ConfirmAppointmentServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // === THÊM KIỂM TRA QUYỀN ===
        HttpSession session = request.getSession(false);
        String role = (session != null) ? (String) session.getAttribute("role") : null;
        if (role == null || (!"STAFF".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
            return;
        }
        // === KẾT THÚC KIỂM TRA ===

        request.setCharacterEncoding("UTF-8");
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff");  // SỬA: /staff thay vì /staff.jsp
            return;
        }
        int appointmentId = Integer.parseInt(appointmentIdStr);
        appointmentDAO.updateStatus(appointmentId, "CONFIRMED");
        response.sendRedirect(request.getContextPath() + "/staff");      // SỬA: /staff thay vì /staff.jsp
    }
}
