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

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            int apptId = Integer.parseInt(request.getParameter("id"));
            AppointmentDAO dao = new AppointmentDAO();
            com.dentalclinic.model.Appointment appt = dao.getAppointmentById(apptId);
            request.setAttribute("appointment", appt);
            
            com.dentalclinic.dao.UserDAO userDAO = new com.dentalclinic.dao.UserDAO();
            request.setAttribute("customers", userDAO.getCustomers());
            request.setAttribute("doctors", userDAO.getDoctors());
            
            request.getRequestDispatcher("edit_appointment.jsp").forward(request, response);
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
        } else if ("delete".equals(action)) {
            dao.deleteAppointment(appointmentId);
        } else if ("update".equals(action)) {
            com.dentalclinic.model.Appointment appt = dao.getAppointmentById(appointmentId);
            if(appt != null) {
                appt.setPatientId(Integer.parseInt(request.getParameter("patientId")));
                appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
                appt.setAppointmentDate(java.sql.Date.valueOf(request.getParameter("appointmentDate")));
                
                String timeStr = request.getParameter("appointmentTime");
                if (timeStr.length() == 5) timeStr += ":00"; // hh:mm -> hh:mm:ss
                appt.setAppointmentTime(java.sql.Time.valueOf(timeStr));
                
                appt.setStatus(request.getParameter("status"));
                appt.setRoom(request.getParameter("room"));
                appt.setNotes(request.getParameter("notes"));
                dao.updateAppointment(appt);
            }
        }

        response.sendRedirect("appointment_admin");
    }
}
