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
        if (!checkRole(request, response))
            return;

        HttpSession session = request.getSession();
        Integer doctorId = (Integer) session.getAttribute("userId");

        AppointmentDAO apptDAO = new AppointmentDAO();
        AppointmentServiceDAO apptServiceDAO = new AppointmentServiceDAO();
        ServiceDAO serviceDAO = new ServiceDAO();
        MedicineDAO medicineDAO = new MedicineDAO();
        ExaminationResultDAO examDAO = new ExaminationResultDAO(); // Added for view_history

        if ("view_history".equals(request.getParameter("action"))) {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            List<ExaminationResult> history = examDAO.getResultsByPatientId(patientId);
            request.setAttribute("history", history);
            request.getRequestDispatcher("patient_history.jsp").forward(request, response);
            return;
        }

        List<Appointment> list = apptDAO.getAppointmentsByDoctor(doctorId);
        for (Appointment a : list) {
            a.setServices(apptServiceDAO.getServicesByAppointmentId(a.getAppointmentId()));
        }

        request.setAttribute("appointments", list);
        request.setAttribute("services", serviceDAO.getAll());
        request.setAttribute("medicines", medicineDAO.getAll());

        request.getRequestDispatcher("doctor.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkRole(request, response))
            return;
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
                String resultDetails = request.getParameter("resultDetails");
                String instructions = request.getParameter("instructions");
                String[] serviceIds = request.getParameterValues("prescribedServiceIds");
                String[] medicineIds = request.getParameterValues("medicineIds");
                String[] quantities = request.getParameterValues("quantities");

                // Save Examination Result
                ExaminationResult er = new ExaminationResult();
                er.setAppointmentId(appointmentId);
                er.setResultDetails(resultDetails);
                int resultId = examDAO.saveResultReturnId(er);

                if (resultId > 0) {
                    // Cập nhật trạng thái lịch hẹn thành Completed để biến mất khỏi hàng chờ và xuất hiện trong lịch sử
                    new AppointmentDAO().updateStatus(appointmentId, "Completed");

                    // Prescribe Services
                    if (serviceIds != null) {
                        prescribedServiceDAO.addPrescribedServices(resultId, serviceIds);
                    }

                    // Save Prescription
                    Prescription p = new Prescription();
                    p.setResultId(resultId);
                    p.setInstructions(instructions);
                    int prescriptionId = prescriptionDAO.savePrescriptionReturnId(p);

                    // Save Prescription Details
                    if (prescriptionId > 0 && medicineIds != null && quantities != null && medicineIds.length == quantities.length) {
                        for (int i = 0; i < medicineIds.length; i++) {
                            try {
                                if (medicineIds[i] != null && !medicineIds[i].isEmpty() && quantities[i] != null && !quantities[i].isEmpty()) {
                                    int medId = Integer.parseInt(medicineIds[i]);
                                    int qty = Integer.parseInt(quantities[i]);
                                    PrescriptionDetail detail = new PrescriptionDetail();
                                    detail.setPrescriptionId(prescriptionId);
                                    if (medId > 0 && qty > 0) {
                                        if (medicineDAO.exists(medId)) { // Check if medicine exists in the database
                                            detail.setMedicineId(medId);
                                            detail.setQuantity(qty);
                                            detailDAO.addPrescriptionDetail(detail);
                                        } else {
                                            System.err.println("Error: Medicine ID does not exist in the database. Medicine ID: " + medId);
                                        }
                                    } else {
                                        System.err.println("Error: Invalid medicine ID or quantity. Medicine ID: " + medId + ", Quantity: " + qty);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        System.err.println("Error: Mismatched or null medicineIds and quantities arrays.");
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
