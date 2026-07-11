package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import com.dentalclinic.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Comparator;

@WebServlet("/staff")
public class StaffServlet extends HttpServlet {

    private static final String CUSTOMERS = "customers";
    private static final String DOCTORS = "doctors";
    private static final String SERVICES = "services";
    private static final String STAFF_JSP = "staff.jsp";
    private static final String APPOINTMENT_ID = "appointmentId";

    private boolean checkRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"STAFF".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }

        String action = request.getParameter("action");

        if ("view_walkin".equals(action)) {
            handleViewWalkin(request, response);
            return;
        }
        if ("edit".equals(action)) {
            handleEdit(request, response);
            return;
        }
        if ("search_appointments".equals(action)) {
            handleSearch(request, response);
            return;
        }
        handleDefaultView(request, response);
    }

    private void handleViewWalkin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        request.setAttribute(CUSTOMERS, userDAO.getCustomers());
        request.setAttribute(DOCTORS, userDAO.getDoctors());
        request.setAttribute(SERVICES, serviceDAO.getAll());
        request.getRequestDispatcher("walkin_booking.jsp").forward(request, response);
    }

    private void handleEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int apptId = Integer.parseInt(request.getParameter("id"));
        AppointmentDAO apptDAO = new AppointmentDAO();
        UserDAO userDAO = new UserDAO();
        request.setAttribute("appointment", apptDAO.getAppointmentById(apptId));
        request.setAttribute(CUSTOMERS, userDAO.getCustomers());
        request.setAttribute(DOCTORS, userDAO.getDoctors());
        request.getRequestDispatcher("edit_appointment_staff.jsp").forward(request, response);
    }

    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("query");
        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        List<Appointment> appointments;
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            appointments = apptDAO.getAll();
        } else {
            appointments = apptDAO.searchAppointments(searchQuery);
        }
        enrichAppointments(appointments, apptServiceDAO, paymentDAO);
        request.setAttribute("appointments", appointments);
        request.getRequestDispatcher(STAFF_JSP).forward(request, response);
    }

    private void handleDefaultView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        List<Appointment> list = apptDAO.getAll();
        enrichAppointments(list, apptServiceDAO, paymentDAO);
        list.sort(Comparator.comparingInt(Appointment::getAppointmentId));

        request.setAttribute("appointments", list);
        request.setAttribute(CUSTOMERS, userDAO.getCustomers());
        request.setAttribute(DOCTORS, userDAO.getDoctors());
        request.setAttribute(SERVICES, serviceDAO.getAll());
        request.getRequestDispatcher(STAFF_JSP).forward(request, response);
    }

    private void enrichAppointments(List<Appointment> list, AppointmentServiceDAO apptServiceDAO, PaymentDAO paymentDAO) {
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
            a.setPayment(paymentDAO.getByAppointmentId(a.getAppointmentId()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");

        AppointmentDAO apptDAO = new AppointmentDAO();
        String action = request.getParameter("action");

        try {
            if ("update_status".equals(action)) {
                handleUpdateStatus(request, apptDAO);
            } else if ("checkin".equals(action)) {
                apptDAO.updateStatus(Integer.parseInt(request.getParameter(APPOINTMENT_ID)), "Checked In");
            } else if ("checkout".equals(action)) {
                apptDAO.updateStatus(Integer.parseInt(request.getParameter(APPOINTMENT_ID)), "Checked Out");
            } else if ("complete".equals(action)) {
                apptDAO.updateStatus(Integer.parseInt(request.getParameter(APPOINTMENT_ID)), "Completed");
            } else if ("delete".equals(action)) {
                apptDAO.deleteAppointment(Integer.parseInt(request.getParameter(APPOINTMENT_ID)));
            } else if ("update".equals(action)) {
                handleUpdateAppointment(request, apptDAO);
            } else if ("book".equals(action)) {
                handleWalkinBooking(request, response);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("staff");
    }

    private void handleUpdateStatus(HttpServletRequest request, AppointmentDAO apptDAO) {
        int id = Integer.parseInt(request.getParameter(APPOINTMENT_ID));
        String status = request.getParameter("status");
        String room = request.getParameter("room");
        Appointment appt = apptDAO.getAppointmentById(id);
        if (appt != null && "Pending".equalsIgnoreCase(appt.getStatus())) {
            apptDAO.updateStatus(id, "CONFIRMED");
        } else {
            apptDAO.updateStatus(id, status);
        }
        if (room != null && !room.isEmpty()) {
            apptDAO.updateRoom(id, room);
        }
    }

    private void handleUpdateAppointment(HttpServletRequest request, AppointmentDAO apptDAO) {
        int id = Integer.parseInt(request.getParameter(APPOINTMENT_ID));
        Appointment appt = apptDAO.getAppointmentById(id);
        if (appt == null) {
            return;
        }

        appt.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
        appt.setAppointmentDate(Date.valueOf(request.getParameter("appointmentDate")));
        String timeStr = request.getParameter("appointmentTime");
        if (timeStr.length() == 5) {
            timeStr += ":00";
        }
        appt.setAppointmentTime(Time.valueOf(timeStr));
        appt.setStatus(request.getParameter("status"));
        appt.setRoom(request.getParameter("room"));
        appt.setNotes(request.getParameter("notes"));
        apptDAO.updateAppointment(appt);
    }

    private void handleWalkinBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();

        String patientName = request.getParameter("patientName");
        int doctorId = Integer.parseInt(request.getParameter("doctorId"));
        Date apptDate = Date.valueOf(request.getParameter("appointmentDate"));
        Time apptTime = Time.valueOf(request.getParameter("appointmentTime") + ":00");
        String room = request.getParameter("room");
        String[] serviceIds = request.getParameterValues("serviceIds");

        int patientId = findOrCreatePatient(userDAO, patientName);
        if (patientId == -1) {
            forwardWithError(request, response, "Lỗi tạo thông tin khách hàng mới. Vui lòng thử lại.", userDAO, serviceDAO);
            return;
        }

        if (apptDAO.isDoctorSlotTaken(doctorId, apptDate, apptTime)) {
            forwardWithError(request, response, "Bác sĩ đã có lịch vào thời gian này. Vui lòng chọn thời gian khác.", userDAO, serviceDAO);
            request.getRequestDispatcher("walkin_booking.jsp").forward(request, response);
            return;
        }

        Appointment appt = new Appointment();
        appt.setPatientId(patientId);
        appt.setDoctorId(doctorId);
        appt.setAppointmentDate(apptDate);
        appt.setAppointmentTime(apptTime);
        appt.setStatus("Checked In");
        appt.setRoom(room);

        int newId = apptDAO.addAppointmentReturnId(appt);
        if (newId > 0) {
            if (serviceIds != null && serviceIds.length > 0) {
                apptServiceDAO.addServicesForAppointment(newId, serviceIds);
            }
            request.getSession().setAttribute("success", "Đã đặt lịch khám (ID: " + newId + ") thành công cho khách hàng " + patientName);
        }
        response.sendRedirect("staff");
    }

    private int findOrCreatePatient(UserDAO userDAO, String patientName) {
        User existingCustomer = userDAO.getCustomerByName(patientName);
        if (existingCustomer != null) {
            return existingCustomer.getUserId();
        }
        User newCustomer = new User();
        newCustomer.setFullName(patientName);
        newCustomer.setEmail("walkin_" + System.currentTimeMillis() + "@clinic.local");
        newCustomer.setPassword("123456");
        newCustomer.setPhone("");
        return userDAO.addUserReturnId(newCustomer, 4);
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String error,
            UserDAO userDAO, ServiceDAO serviceDAO) throws ServletException, IOException {
        request.setAttribute("error", error);
        request.setAttribute(CUSTOMERS, userDAO.getCustomers());
        request.setAttribute(DOCTORS, userDAO.getDoctors());
        request.setAttribute(SERVICES, serviceDAO.getAll());
        request.getRequestDispatcher(STAFF_JSP).forward(request, response);
    }
}
