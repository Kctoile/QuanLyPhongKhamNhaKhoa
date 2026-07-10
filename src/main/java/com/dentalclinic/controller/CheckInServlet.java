package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/CheckInServlet")
public class CheckInServlet extends HttpServlet {

    private static final String STAFF_PAGE = "/staff";

    private final transient AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + STAFF_PAGE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + STAFF_PAGE);
            return;
        }
        int appointmentId = Integer.parseInt(appointmentIdStr);
        boolean success = appointmentDAO.updateStatus(appointmentId, "Checked In");
        if (!success) {
            request.setAttribute("error", "Không thể check-in. Vui lòng kiểm tra trạng thái lịch hẹn.");
        }
        response.sendRedirect(request.getContextPath() + STAFF_PAGE);
    }
}
