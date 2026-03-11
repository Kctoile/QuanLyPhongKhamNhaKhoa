package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        if (!"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        UserDAO userDAO = new UserDAO();
        AppointmentDAO apptDAO = new AppointmentDAO();
        MedicineDAO medicineDAO = new MedicineDAO();

        request.setAttribute("totalUsers", userDAO.countUsers());
        request.setAttribute("totalDoctors", userDAO.countDoctors());
        request.setAttribute("totalAppointmentsToday", apptDAO.countAppointmentsToday());
        request.setAttribute("totalThuoc", medicineDAO.countMedicines());

        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }
}
