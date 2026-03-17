package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/appointment_admin")
public class AppointmentAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        AppointmentDAO dao = new AppointmentDAO();
        request.setAttribute("list", dao.getAll());

        request.getRequestDispatcher("appointments_admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        AppointmentDAO dao = new AppointmentDAO();

        if ("checkin".equals(action)) {
            dao.updateStatus(appointmentId, "Checked In");
        } else if ("checkout".equals(action)) {
            dao.updateStatus(appointmentId, "Checked Out");
        } else if ("complete".equals(action)) {
            dao.updateStatus(appointmentId, "Completed");
        }

        response.sendRedirect("appointment_admin");
    }
}
