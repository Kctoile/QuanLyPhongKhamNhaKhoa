package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.ServiceDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.Service;
import com.dentalclinic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {

    private static final String ERROR_PARAM = "error";

    private final transient UserDAO userDAO = new UserDAO();
    private final transient ServiceDAO serviceDAO = new ServiceDAO();
    private final transient AppointmentDAO apptDAO = new AppointmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        loadBookingOptions(request);
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
        int doctorId;
        Date appointmentDate;
        Time appointmentTime;
        String notes = request.getParameter("notes");
        String[] serviceIds = request.getParameterValues("serviceIds");
        try {
            doctorId = Integer.parseInt(request.getParameter("doctorId"));
            LocalDate selectedDate = LocalDate.parse(request.getParameter("appointmentDate"));
            if (selectedDate.isBefore(LocalDate.now())) {
                forwardWithError(request, response, "Vui lòng chọn ngày khám từ hôm nay trở đi.");
                return;
            }
            appointmentDate = Date.valueOf(selectedDate);
            appointmentTime = Time.valueOf(request.getParameter("appointmentTime") + ":00");
        } catch (Exception e) {
            forwardWithError(request, response, "Thông tin đặt lịch không hợp lệ. Vui lòng kiểm tra ngày, giờ và bác sĩ.");
            return;
        }
        if (serviceIds == null || serviceIds.length == 0) {
            forwardWithError(request, response, "Vui lòng chọn ít nhất một dịch vụ.");
            return;
        }
        User doctor = userDAO.getUserById(doctorId);
        if (doctor == null || doctor.getRole() == null || !"DOCTOR".equals(doctor.getRole().getRoleName())) {
            forwardWithError(request, response, "Bác sĩ được chọn không hợp lệ.");
            return;
        }
        Appointment appt = new Appointment();
        appt.setPatientId(user.getUserId());
        appt.setDoctorId(doctorId);
        appt.setAppointmentDate(appointmentDate);
        appt.setAppointmentTime(appointmentTime);
        appt.setStatus("Pending");
        appt.setNotes(notes);
        int newApptId = apptDAO.bookAppointmentWithServices(appt, serviceIds);
        if (newApptId > 0) {
            session.setAttribute("successMessage", "Đặt lịch thành công! Vui lòng chờ xác nhận.");
        } else if (newApptId == AppointmentDAO.BOOKING_SLOT_TAKEN) {
            session.setAttribute(ERROR_PARAM, "Bác sĩ đã có lịch vào thời gian này. Vui lòng chọn thời gian khác.");
        } else {
            session.setAttribute(ERROR_PARAM, "Đã xảy ra lỗi khi đặt lịch. Vui lòng thử lại.");
        }
        response.sendRedirect("booking");
    }

    private void loadBookingOptions(HttpServletRequest request) {
        List doctors = userDAO.getDoctors();
        List services = serviceDAO.getAll();
        request.setAttribute("doctors", doctors);
        request.setAttribute("services", services);
        request.setAttribute("minAppointmentDate", LocalDate.now().toString());
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute(ERROR_PARAM, message);
        loadBookingOptions(request);
        request.getRequestDispatcher("booking.jsp").forward(request, response);
    }
}
