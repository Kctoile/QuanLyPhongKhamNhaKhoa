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

    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String appointmentIdStr = request.getParameter("appointmentId");
        if (appointmentIdStr == null || appointmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/staff.jsp");
            return;
        }
        int appointmentId = Integer.parseInt(appointmentIdStr);
        appointmentDAO.updateStatus(appointmentId, "CONFIRMED");
        response.sendRedirect(request.getContextPath() + "/staff.jsp");
    }
}
