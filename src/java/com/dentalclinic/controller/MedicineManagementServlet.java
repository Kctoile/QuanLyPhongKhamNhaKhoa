package com.dentalclinic.controller;

import com.dentalclinic.dao.MedicineDAO;
import com.dentalclinic.model.Medicine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/medicines")
public class MedicineManagementServlet extends HttpServlet {

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response))
            return;

        MedicineDAO dao = new MedicineDAO();
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteMedicine(id);
            } catch (NumberFormatException ignored) {
            }
            response.sendRedirect("medicines");
            return;
        }

        List<Medicine> list = (keyword != null && !keyword.trim().isEmpty())
                ? dao.searchMedicines(keyword.trim())
                : dao.getAll();

        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/medicines.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response))
            return;
        request.setCharacterEncoding("UTF-8");

        MedicineDAO dao = new MedicineDAO();
        String action = request.getParameter("action");

        try {
            String medicineName = request.getParameter("medicineName");
            double price = Double.parseDouble(request.getParameter("price"));
            int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));

            Medicine medicine = new Medicine();
            medicine.setMedicineName(medicineName);
            medicine.setPrice(price);
            medicine.setStockQuantity(stockQuantity);

            if ("add".equals(action)) {
                dao.addMedicine(medicine);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("medicineId"));
                medicine.setMedicineId(id);
                dao.updateMedicine(medicine);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("medicines");
    }
}
