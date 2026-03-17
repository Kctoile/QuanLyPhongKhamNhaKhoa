package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.AppointmentServiceDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.dao.ServiceDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.User;
import com.dentalclinic.model.Service;
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
        if (!checkRole(request, response))
            return;

        UserDAO userDAO = new UserDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();

        String action = request.getParameter("action");

        if ("view_walkin".equals(action)) {
            request.setAttribute("customers", userDAO.getCustomers());
            request.setAttribute("doctors", userDAO.getDoctors());
            request.setAttribute("services", serviceDAO.getAll());
            request.getRequestDispatcher("walkin_booking.jsp").forward(request, response);
            return;
        }

        if ("search_appointments".equals(action)) {
            String searchQuery = request.getParameter("query");
            List<Appointment> appointments;
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                appointments = apptDAO.getAll();
            } else {
                appointments = apptDAO.searchAppointments(searchQuery);
            }
            for (Appointment a : appointments) {
                a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
            }
            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("staff.jsp").forward(request, response);
            return;
        }

        List<Appointment> list = apptDAO.getAll();
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
        }

        // Sắp xếp danh sách lịch hẹn theo ID tăng dần
        list.sort(Comparator.comparingInt(Appointment::getAppointmentId));

        // Truyền dữ liệu cho form đặt lịch tại chỗ
        request.setAttribute("appointments", list);
        request.setAttribute("customers", userDAO.getCustomers());
        request.setAttribute("doctors", userDAO.getDoctors());
        request.setAttribute("services", serviceDAO.getAll());
        request.getRequestDispatcher("staff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response))
            return;
        request.setCharacterEncoding("UTF-8");

        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        String action = request.getParameter("action");

        try {
                if ("update_status".equals(action)) {
                    int id = Integer.parseInt(request.getParameter("appointmentId"));
                    String status = request.getParameter("status");
                    String room = request.getParameter("room");

                    // Nếu lịch đang ở trạng thái PENDING, chuyển sang CONFIRMED
                    Appointment appt = apptDAO.getAppointmentById(id);
                    if (appt != null && "Pending".equalsIgnoreCase(appt.getStatus())) {
                        apptDAO.updateStatus(id, "CONFIRMED");
                    } else {
                        apptDAO.updateStatus(id, status);
                    }
                    if (room != null && !room.isEmpty()) {
                        apptDAO.updateRoom(id, room);
                    }
            } else if ("book".equals(action)) {
                UserDAO userDAO = new UserDAO();
                ServiceDAO serviceDAO = new ServiceDAO();
                int patientId = Integer.parseInt(request.getParameter("patientId"));
                int doctorId = Integer.parseInt(request.getParameter("doctorId"));
                Date apptDate = Date.valueOf(request.getParameter("appointmentDate"));
                Time apptTime = Time.valueOf(request.getParameter("appointmentTime") + ":00");
                String room = request.getParameter("room");
                String[] serviceIds = request.getParameterValues("serviceIds");

                // Kiểm tra lịch trống của bác sĩ
                boolean slotTaken = apptDAO.isDoctorSlotTaken(doctorId, apptDate, apptTime);
                if (slotTaken) {
                    request.setAttribute("error", "Bác sĩ đã có lịch vào thời gian này. Vui lòng chọn thời gian khác.");
                    request.setAttribute("customers", userDAO.getCustomers());
                    request.setAttribute("doctors", userDAO.getDoctors());
                    request.setAttribute("services", serviceDAO.getAll());
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
                    } else {
                        System.err.println("No services selected for appointment ID: " + newId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("staff");
    }
}
