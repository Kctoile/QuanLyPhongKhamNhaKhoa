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

                // 1. Save Examination Result
                ExaminationResult er = new ExaminationResult();
                er.setAppointmentId(appointmentId);
                er.setResultDetails(resultDetails);
                int resultId = examDAO.saveResultReturnId(er);

                if (resultId > 0) {
                    // 2. Prescribe Services
                    if (serviceIds != null) {
                        prescribedServiceDAO.addPrescribedServices(resultId, serviceIds);
                    }

                    // 3. Save Prescription
                    Prescription p = new Prescription();
                    p.setResultId(resultId);
                    p.setInstructions(instructions);
                    int prescriptionId = prescriptionDAO.savePrescriptionReturnId(p);

                    // 4. Save Prescription Details
                    if (prescriptionId > 0 && medicineIds != null && quantities != null) {
                        for (int i = 0; i < medicineIds.length; i++) {
                            try {
                                int medId = Integer.parseInt(medicineIds[i]);
                                int qty = Integer.parseInt(quantities[i]);
                                if (qty > 0) {
                                    Medicine m = medicineDAO.getMedicineById(medId);
                                    if (m != null) {
                                        detailDAO.addOrUpdateDetail(prescriptionId, medId, qty, m.getPrice());
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    // Update appointment status
                    new AppointmentDAO().updateStatus(appointmentId, "Completed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("doctor");
    }
}
