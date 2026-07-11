package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.AppointmentServiceDAO;
import com.dentalclinic.dao.ExaminationResultDAO;
import com.dentalclinic.dao.PaymentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.ExaminationResult;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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

        if (handleViewResult(request, response, userId)) {
            return;
        }

        AppointmentDAO dao = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        List<Appointment> list = dao.getAppointmentsByPatient(userId);
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
            a.setPayment(paymentDAO.getByAppointmentId(a.getAppointmentId()));
        }

        List<Appointment> upcoming = new ArrayList<>();
        List<Appointment> history = new ArrayList<>();
        splitAppointments(list, upcoming, history);

        request.setAttribute("upcoming", upcoming);
        request.setAttribute("history", history);
        request.getRequestDispatcher("appointments.jsp").forward(request, response);
    }

    private boolean handleViewResult(HttpServletRequest request, HttpServletResponse response, Integer userId)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (!"view_result".equals(action)) {
            return false;
        }

        String apptIdStr = request.getParameter("appointmentId");
        if (apptIdStr == null) {
            response.sendRedirect("appointments");
            return true;
        }

        int appointmentId = Integer.parseInt(apptIdStr);
        AppointmentDAO dao = new AppointmentDAO();
        Appointment appt = dao.getAppointmentById(appointmentId);

        if (appt != null && appt.getPatient() != null && appt.getPatient().getUserId() == userId) {
            ExaminationResultDAO examDAO = new ExaminationResultDAO();
            ExaminationResult result = examDAO.getResultByAppointmentId(appointmentId);
            request.setAttribute("appointment", appt);
            request.setAttribute("examinationResult", result);
            request.getRequestDispatcher("customer_result.jsp").forward(request, response);
        } else {
            response.sendRedirect("appointments");
        }
        return true;
    }

    private void splitAppointments(List<Appointment> list, List<Appointment> upcoming, List<Appointment> history) {
        LocalDate today = LocalDate.now();
        for (Appointment a : list) {
            if (isUpcoming(a, today)) {
                upcoming.add(a);
            } else {
                history.add(a);
            }
        }
        upcoming.sort((a, b) -> {
            int dateCmp = a.getAppointmentDate().compareTo(b.getAppointmentDate());
            if (dateCmp != 0) {
                return dateCmp;
            }
            return a.getAppointmentTime().compareTo(b.getAppointmentTime());
        });
        history.sort((a, b) -> {
            int dateCmp = b.getAppointmentDate().compareTo(a.getAppointmentDate());
            if (dateCmp != 0) {
                return dateCmp;
            }
            return b.getAppointmentTime().compareTo(a.getAppointmentTime());
        });
    }

    private boolean isUpcoming(Appointment a, LocalDate today) {
        if (a.getAppointmentDate() == null) {
            return false;
        }
        LocalDate apptDate = a.getAppointmentDate().toLocalDate();
        return apptDate.isAfter(today) || (apptDate.equals(today)
                && !"Completed".equalsIgnoreCase(a.getStatus())
                && !"Cancelled".equalsIgnoreCase(a.getStatus()));
    }
}
