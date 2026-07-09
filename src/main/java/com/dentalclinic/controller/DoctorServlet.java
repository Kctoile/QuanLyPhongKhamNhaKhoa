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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {

    private boolean checkRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"DOCTOR".equalsIgnoreCase((String) session.getAttribute("role"))) {
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

        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("userId");

        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        MedicineDAO medicineDAO = new MedicineDAO();
        ExaminationResultDAO examDAO = new ExaminationResultDAO();

        if ("view_history".equals(request.getParameter("action"))) {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            // === IDOR CHECK: Kiểm tra bệnh nhân có thuộc bác sĩ này không ===
            boolean hasAppointment = apptDAO.hasAppointmentWithDoctor(patientId, doctorId);
            if (!hasAppointment) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem hồ sơ này.");
                return;
            }
            List history = examDAO.getResultsByPatientId(patientId);
            request.setAttribute("history", history);
            request.getRequestDispatcher("patient_history.jsp").forward(request, response);
            return;
        }

        // === CHẶN BÁC SĨ VÀO FORM KHÁM CỦA LỊCH TƯƠNG LAI ===
        String form = request.getParameter("form");
        if ("1".equals(form)) {
            int apptId = Integer.parseInt(request.getParameter("appointmentId"));
            Appointment appt = apptDAO.getAppointmentById(apptId);
            if (appt != null && appt.getAppointmentDate() != null && appt.getAppointmentTime() != null) {
                LocalDateTime apptDateTime = LocalDateTime.of(
                        appt.getAppointmentDate().toLocalDate(),
                        appt.getAppointmentTime().toLocalTime()
                );
                if (LocalDateTime.now().isBefore(apptDateTime)) {
                    session.setAttribute("errorMsg", "Chưa đến thời gian khám. Vui lòng đợi đến giờ hẹn.");
                    response.sendRedirect("doctor");
                    return;
                }
            }
        }

        List list = apptDAO.getAppointmentsByDoctor(doctorId);

        // === GẮN canExamine CHO TỪNG APPOINTMENT ===
        LocalDateTime now = LocalDateTime.now();
        for (Object obj : list) {
            Appointment a = (Appointment) obj;
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));

            boolean canExam = false;
            if ("Checked In".equals(a.getStatus()) && a.getAppointmentDate() != null && a.getAppointmentTime() != null) {
                LocalDateTime apptDateTime = LocalDateTime.of(
                        a.getAppointmentDate().toLocalDate(),
                        a.getAppointmentTime().toLocalTime()
                );
                canExam = !now.isBefore(apptDateTime);
            }
            a.setCanExamine(canExam);
        }

        request.setAttribute("appointments", list);
        request.setAttribute("services", serviceDAO.getAll());
        request.setAttribute("medicines", medicineDAO.getAll());
        request.getRequestDispatcher("doctor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        ExaminationResultDAO examDAO = new ExaminationResultDAO();
        PrescribedServiceDAO prescribedServiceDAO = new PrescribedServiceDAO();
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
        PrescriptionDetailDAO detailDAO = new PrescriptionDetailDAO();
        MedicineDAO medicineDAO = new MedicineDAO();

        try {
            if ("save_exam".equals(action)) {
                // === VALIDATION: CHẶN KÊ ĐƠN CHO LỊCH TƯƠNG LAI ===
                HttpSession session = request.getSession();
                Appointment appt = new AppointmentDAO().getAppointmentById(appointmentId);
                if (appt != null && appt.getAppointmentDate() != null && appt.getAppointmentTime() != null) {
                    LocalDateTime apptDateTime = LocalDateTime.of(
                            appt.getAppointmentDate().toLocalDate(),
                            appt.getAppointmentTime().toLocalTime()
                    );
                    if (LocalDateTime.now().isBefore(apptDateTime)) {
                        session.setAttribute("errorMsg", "Chưa đến thời gian khám. Không thể kê đơn.");
                        response.sendRedirect("doctor");
                        return;
                    }
                }

                String resultDetails = request.getParameter("resultDetails");
                String instructions = request.getParameter("instructions");
                String[] serviceIds = request.getParameterValues("prescribedServiceIds");
                String[] medicineIds = request.getParameterValues("medicineIds");
                String[] quantities = request.getParameterValues("quantities");

                ExaminationResult er = new ExaminationResult();
                er.setAppointmentId(appointmentId);
                er.setResultDetails(resultDetails);
                int resultId = examDAO.saveResultReturnId(er);

                if (resultId > 0) {
                    new AppointmentDAO().updateStatus(appointmentId, "Completed");
                    if (serviceIds != null) {
                        prescribedServiceDAO.addPrescribedServices(resultId, serviceIds);
                    }

                    Prescription p = new Prescription();
                    p.setResultId(resultId);
                    p.setInstructions(instructions);
                    int prescriptionId = prescriptionDAO.savePrescriptionReturnId(p);

                    if (prescriptionId > 0 && medicineIds != null && quantities != null
                            && medicineIds.length == quantities.length) {
                        for (int i = 0; i < medicineIds.length; i++) {
                            try {
                                if (medicineIds[i] != null && !medicineIds[i].isEmpty()
                                        && quantities[i] != null && !quantities[i].isEmpty()) {
                                    int medId = Integer.parseInt(medicineIds[i]);
                                    int qty = Integer.parseInt(quantities[i]);
                                    PrescriptionDetail detail = new PrescriptionDetail();
                                    detail.setPrescriptionId(prescriptionId);
                                    if (medId > 0 && qty > 0 && medicineDAO.exists(medId)) {
                                        detail.setMedicineId(medId);
                                        detail.setQuantity(qty);
                                        detailDAO.addPrescriptionDetail(detail);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    response.sendRedirect("doctor");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("doctor");
    }
}
