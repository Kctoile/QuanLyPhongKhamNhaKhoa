package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.AppointmentServiceDAO;
import com.dentalclinic.model.Appointment;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/appointments")
public class AppointmentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        AppointmentDAO dao = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();

        List<Appointment> list = dao.getAppointmentsByPatient(userId);

        // Fetch services for each appointment
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
        }

        request.setAttribute("list", list);
        request.getRequestDispatcher("appointments.jsp").forward(request, response);
    }
}
