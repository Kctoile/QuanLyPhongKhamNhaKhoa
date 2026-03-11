package com.dentalclinic.controller;

import com.dentalclinic.dao.ServiceDAO;
import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.AppointmentServiceDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.Service;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();
    private ServiceDAO serviceDAO = new ServiceDAO();
    private AppointmentDAO apptDAO = new AppointmentDAO();
    private AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<User> doctors = userDAO.getDoctors();
        List<Service> services = serviceDAO.getAll();

        request.setAttribute("doctors", doctors);
        request.setAttribute("services", services);
        request.getRequestDispatcher("booking.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));
        Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
        String timeStr = request.getParameter("appointmentTime");
        String notes = request.getParameter("notes");
        String[] serviceIds = request.getParameterValues("serviceIds"); // Allow multiple service selection

        if (serviceIds == null || serviceIds.length == 0) {
            request.setAttribute("error", "Vui lòng chọn ít nhất một dịch vụ.");
            request.setAttribute("doctors", userDAO.getDoctors());
            request.setAttribute("services", serviceDAO.getAll());
            request.getRequestDispatcher("booking.jsp").forward(request, response);
            return;
        }

        Time appointmentTime = Time.valueOf(timeStr + ":00");

        // Kiểm tra lịch trống của bác sĩ
        boolean slotTaken = apptDAO.isDoctorSlotTaken(doctorId, appointmentDate, appointmentTime);
        if (slotTaken) {
            request.setAttribute("error", "Bác sĩ đã có lịch vào thời gian này. Vui lòng chọn thời gian khác.");
            request.setAttribute("doctors", userDAO.getDoctors());
            request.setAttribute("services", serviceDAO.getAll());
            request.getRequestDispatcher("booking.jsp").forward(request, response);
            return;
        }

        Appointment appt = new Appointment();
        appt.setPatientId(user.getUserId());
        appt.setDoctorId(doctorId);
        appt.setAppointmentDate(appointmentDate);
        appt.setAppointmentTime(appointmentTime);
        appt.setStatus("Pending");
        appt.setNotes(notes);

        int newApptId = apptDAO.addAppointmentReturnId(appt);

        if (newApptId > 0) {
            apptServiceDAO.addServicesForAppointment(newApptId, serviceIds);
            session.setAttribute("successMessage", "Đặt lịch thành công! Vui lòng chờ xác nhận.");
        } else {
            session.setAttribute("error", "Đã xảy ra lỗi khi đặt lịch.");
        }

        response.sendRedirect("booking");
    }
}
