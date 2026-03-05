package com.dentalclinic.controller;

import com.dentalclinic.dao.ThanhToanDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * Admin xem toàn bộ thanh toán Không xử lý thanh toán (Staff làm)
 */
@WebServlet("/thanhtoan_admin")
public class ThanhToanAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        ThanhToanDAO dao = new ThanhToanDAO();
        request.setAttribute("listThanhToan", dao.getAllDisplay());

        request.getRequestDispatcher("thanhtoan_admin.jsp")
                .forward(request, response);
    }
}
