package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/CheckInServlet")
public class CheckInServlet extends HttpServlet {

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    private boolean checkRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null
                || (!"STAFF".equalsIgnoreCase((String) session.getAttribute("role"))
                && !"ADMIN".equalsIgnoreCase((String) session.getAttribute("role")))) {
            response.sendRedirect("login.jsp");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }
        response.sendRedirect(request.getContextPath() + "/staff");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff");
            return;
        }
        int appointmentId = Integer.parseInt(appointmentIdStr);
        boolean success = appointmentDAO.updateStatus(appointmentId, "Checked In");
        if (!success) {
            request.setAttribute("error", "Không thể check-in. Vui lòng kiểm tra trạng thái lịch hẹn.");
        }
        response.sendRedirect(request.getContextPath() + "/staff");
    }
}
