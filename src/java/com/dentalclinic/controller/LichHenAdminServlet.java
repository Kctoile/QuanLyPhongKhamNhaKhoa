package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * Admin xem toàn bộ lịch hẹn trong hệ thống Chỉ READ-ONLY (không xử lý trạng
 * thái)
 */
@WebServlet("/lichhen_admin")
public class LichHenAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        LichHenDAO dao = new LichHenDAO();
        request.setAttribute("list", dao.getAllDisplay());

        request.getRequestDispatcher("lichhen_admin.jsp")
                .forward(request, response);
    }
}
