package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

/**
 * Servlet trang dashboard quản trị - hiển thị thống kê tổng quan
 */
@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    /**
     * Kiểm tra session có đăng nhập và role = ADMIN, không thì redirect login hoặc 403
     */
    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }

        String role = session.getAttribute("role").toString();

        if (!"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }

    /**
     * GET: Hiển thị dashboard admin với thống kê (số user, bác sĩ, lịch hẹn hôm nay, doanh thu, số thuốc)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        NguoiDungDAO userDAO = new NguoiDungDAO();
        LichHenDAO lichDAO = new LichHenDAO();
        ThanhToanDAO thanhToanDAO = new ThanhToanDAO();
        ThuocDAO thuocDAO = new ThuocDAO();

        request.setAttribute("totalUsers", userDAO.countUsers());
        request.setAttribute("totalDoctors", userDAO.countDoctors());
        request.setAttribute("totalAppointmentsToday", lichDAO.countToday());
        request.setAttribute("totalRevenueToday", thanhToanDAO.totalRevenueToday());
        request.setAttribute("totalThuoc", thuocDAO.countThuoc());

        request.getRequestDispatcher("admin_dashboard.jsp")
                .forward(request, response);
    }
}
