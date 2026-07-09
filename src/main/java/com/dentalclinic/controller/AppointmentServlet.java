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

        AppointmentDAO dao = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        ExaminationResultDAO examDAO = new ExaminationResultDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        String action = request.getParameter("action");
        if ("view_result".equals(action)) {
            String apptIdStr = request.getParameter("appointmentId");
            if (apptIdStr != null) {
                int appointmentId = Integer.parseInt(apptIdStr);
                Appointment appt = dao.getAppointmentById(appointmentId);
                if (appt != null && appt.getPatient() != null && appt.getPatient().getUserId() == userId) {
                    ExaminationResult result = examDAO.getResultByAppointmentId(appointmentId);
                    // === SỬA: đổi tên attribute "appt" thành "appointment" ===
                    request.setAttribute("appointment", appt);
                    request.setAttribute("examinationResult", result);
                    request.getRequestDispatcher("customer_result.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("appointments");
            return;
        }

        List<Appointment> list = dao.getAppointmentsByPatient(userId);
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
            a.setPayment(paymentDAO.getByAppointmentId(a.getAppointmentId()));
        }

        // === THÊM: tách lịch sắp tới và lịch sử ===
        List<Appointment> upcoming = new ArrayList<>();
        List<Appointment> history = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Appointment a : list) {
            if (a.getAppointmentDate() != null) {
                LocalDate apptDate = a.getAppointmentDate().toLocalDate();
                if (apptDate.isAfter(today)
                        || (apptDate.equals(today) && !"Completed".equalsIgnoreCase(a.getStatus())
                        && !"Cancelled".equalsIgnoreCase(a.getStatus()))) {
                    upcoming.add(a);
                } else {
                    history.add(a);
                }
            } else {
                history.add(a);
            }
        }

        // Sắp xếp upcoming: thời gian tăng dần (gần nhất lên đầu)
        upcoming.sort((a, b) -> {
            int dateCmp = a.getAppointmentDate().compareTo(b.getAppointmentDate());
            if (dateCmp != 0) {
                return dateCmp;
            }
            return a.getAppointmentTime().compareTo(b.getAppointmentTime());
        });

        // Sắp xếp history: thời gian giảm dần (mới nhất lên đầu)
        history.sort((a, b) -> {
            int dateCmp = b.getAppointmentDate().compareTo(a.getAppointmentDate());
            if (dateCmp != 0) {
                return dateCmp;
            }
            return b.getAppointmentTime().compareTo(a.getAppointmentTime());
        });

        request.setAttribute("upcoming", upcoming);
        request.setAttribute("history", history);
        request.getRequestDispatcher("appointments.jsp").forward(request, response);
    }
}
