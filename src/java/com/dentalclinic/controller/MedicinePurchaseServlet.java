package com.dentalclinic.controller;

import com.dentalclinic.dao.PrescriptionDetailDAO;
import com.dentalclinic.dao.MedicineDAO;
import com.dentalclinic.model.Medicine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/purchase_medicine")
public class MedicinePurchaseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int prescriptionId = Integer.parseInt(request.getParameter("prescriptionId"));
            int medicineId = Integer.parseInt(request.getParameter("medicineId"));
            int qtyToBuy = Integer.parseInt(request.getParameter("qtyToBuy"));

            MedicineDAO medicineDAO = new MedicineDAO();
            PrescriptionDetailDAO detailDAO = new PrescriptionDetailDAO();

            Medicine m = medicineDAO.getMedicineById(medicineId);
            if (m != null && m.getStockQuantity() >= qtyToBuy) {
                // Deduct stock
                m.setStockQuantity(m.getStockQuantity() - qtyToBuy);
                medicineDAO.updateMedicine(m);

                // Increase purchased quantity
                detailDAO.updatePurchasedQuantity(prescriptionId, medicineId, qtyToBuy);

                session.setAttribute("successMessage", "Mua thuốc thành công!");
            } else {
                session.setAttribute("errorMessage", "Không đủ số lượng thuốc trong kho!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Lỗi xử lý mua thuốc.");
        }

        // Normally you'd redirect back to the prescription details page
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "index.jsp");
    }
}
