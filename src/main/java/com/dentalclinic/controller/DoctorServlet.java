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
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {

    private static final String ERROR_MSG = "errorMsg";
    private static final String DOCTOR = "doctor";

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

        if (handleViewHistory(request, response)) {
            return;
        }
        if (handleFutureExamBlock(request, response, session)) {
            return;
        }

        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        MedicineDAO medicineDAO = new MedicineDAO();

        List<Appointment> list = apptDAO.getAppointmentsByDoctor(doctorId);
        enrichAppointments(list, apptServiceDAO);

        request.setAttribute("appointments", list);
        request.setAttribute("services", serviceDAO.getAll());
        request.setAttribute("medicines", medicineDAO.getAll());
        request.getRequestDispatcher("doctor.jsp").forward(request, response);
    }

    private boolean handleViewHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"view_history".equals(request.getParameter("action"))) {
            return false;
        }

        int patientId = Integer.parseInt(request.getParameter("patientId"));
        ExaminationResultDAO examDAO = new ExaminationResultDAO();
        List history = examDAO.getResultsByPatientId(patientId);
        request.setAttribute("history", history);
        request.getRequestDispatcher("patient_history.jsp").forward(request, response);
        return true;
    }

    private boolean handleFutureExamBlock(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        if (!"1".equals(request.getParameter("form"))) {
            return false;
        }

        int apptId = Integer.parseInt(request.getParameter("appointmentId"));
        AppointmentDAO apptDAO = new AppointmentDAO();
        Appointment appt = apptDAO.getAppointmentById(apptId);

        if (isFutureAppointment(appt)) {
            session.setAttribute(ERROR_MSG, "Chưa đến thời gian khám. Vui lòng đợi đến giờ hẹn.");
            response.sendRedirect(DOCTOR);
            return true;
        }
        return false;
    }

    private void enrichAppointments(List<Appointment> list, AppointmentServiceDAO apptServiceDAO) {
        LocalDateTime now = LocalDateTime.now();
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
            a.setCanExamine(canExamine(a, now));
        }
    }

    private boolean canExamine(Appointment a, LocalDateTime now) {
        if (!"Checked In".equals(a.getStatus()) || a.getAppointmentDate() == null || a.getAppointmentTime() == null) {
            return false;
        }
        LocalDateTime apptDateTime = LocalDateTime.of(
                a.getAppointmentDate().toLocalDate(),
                a.getAppointmentTime().toLocalTime()
        );
        return !now.isBefore(apptDateTime);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");

        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        if ("save_exam".equals(request.getParameter("action"))) {
            handleSaveExam(request, response, appointmentId);
            return;
        }
        response.sendRedirect(DOCTOR);
    }

    private void handleSaveExam(HttpServletRequest request, HttpServletResponse response, int appointmentId)
            throws IOException {
        HttpSession session = request.getSession();

        if (isFutureAppointment(new AppointmentDAO().getAppointmentById(appointmentId))) {
            session.setAttribute(ERROR_MSG, "Chưa đến thời gian khám. Không thể kê đơn.");
            response.sendRedirect(DOCTOR);
            return;
        }

        ExaminationResultDAO examDAO = new ExaminationResultDAO();
        int resultId = examDAO.saveResultReturnId(buildExaminationResult(request, appointmentId));

        if (resultId > 0) {
            new AppointmentDAO().updateStatus(appointmentId, "Completed");
            saveServices(request, resultId);
            savePrescription(request, resultId);
        }
        response.sendRedirect(DOCTOR);
    }

    private ExaminationResult buildExaminationResult(HttpServletRequest request, int appointmentId) {
        ExaminationResult er = new ExaminationResult();
        er.setAppointmentId(appointmentId);
        er.setResultDetails(request.getParameter("resultDetails"));
        return er;
    }

    private void saveServices(HttpServletRequest request, int resultId) {
        String[] serviceIds = request.getParameterValues("prescribedServiceIds");
        if (serviceIds != null) {
            new PrescribedServiceDAO().addPrescribedServices(resultId, serviceIds);
        }
    }

    private void savePrescription(HttpServletRequest request, int resultId) {
        PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
        Prescription p = new Prescription();
        p.setResultId(resultId);
        p.setInstructions(request.getParameter("instructions"));
        int prescriptionId = prescriptionDAO.savePrescriptionReturnId(p);

        if (prescriptionId > 0) {
            saveMedicineDetails(request, prescriptionId);
        }
    }

    private void saveMedicineDetails(HttpServletRequest request, int prescriptionId) {
        String[] medicineIds = request.getParameterValues("medicineIds");
        String[] quantities = request.getParameterValues("quantities");
        if (medicineIds == null || quantities == null || medicineIds.length != quantities.length) {
            return;
        }

        PrescriptionDetailDAO detailDAO = new PrescriptionDetailDAO();
        MedicineDAO medicineDAO = new MedicineDAO();

        for (int i = 0; i < medicineIds.length; i++) {
            try {
                if (medicineIds[i] == null || medicineIds[i].isEmpty()
                        || quantities[i] == null || quantities[i].isEmpty()) {
                    continue;
                }

                int medId = Integer.parseInt(medicineIds[i]);
                int qty = Integer.parseInt(quantities[i]);
                if (medId > 0 && qty > 0 && medicineDAO.exists(medId)) {
                    PrescriptionDetail detail = new PrescriptionDetail();
                    detail.setPrescriptionId(prescriptionId);
                    detail.setMedicineId(medId);
                    detail.setQuantity(qty);
                    detailDAO.addPrescriptionDetail(detail);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isFutureAppointment(Appointment appt) {
        if (appt == null || appt.getAppointmentDate() == null || appt.getAppointmentTime() == null) {
            return false;
        }
        LocalDateTime apptDateTime = LocalDateTime.of(
                appt.getAppointmentDate().toLocalDate(),
                appt.getAppointmentTime().toLocalTime()
        );
        return LocalDateTime.now().isBefore(apptDateTime);
    }
}
