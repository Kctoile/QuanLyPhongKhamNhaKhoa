package com.dentalclinic.controller;

import com.dentalclinic.dao.ServiceDAO;
import com.dentalclinic.model.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/services")
public class ServiceManagementServlet extends HttpServlet {

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

        ServiceDAO dao = new ServiceDAO();
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                dao.deleteService(id);
            } catch (NumberFormatException ignored) {
            }
            response.sendRedirect("services");
            return;
        }

        List<Service> list = (keyword != null && !keyword.trim().isEmpty())
                ? dao.searchServices(keyword.trim())
                : dao.getAll();

        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/services.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response))
            return;
        request.setCharacterEncoding("UTF-8");

        ServiceDAO dao = new ServiceDAO();
        String action = request.getParameter("action");

        try {
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            String durationStr = request.getParameter("durationMinutes");
            Integer durationMinutes = (durationStr != null && !durationStr.isEmpty()) ? Integer.parseInt(durationStr)
                    : null;

            Service service = new Service();
            service.setServiceName(serviceName);
            service.setDescription(description);
            service.setPrice(price);
            service.setDurationMinutes(durationMinutes);

            if ("add".equals(action)) {
                dao.addService(service);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("serviceId"));
                service.setServiceId(id);
                dao.updateService(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("services");
    }
}
