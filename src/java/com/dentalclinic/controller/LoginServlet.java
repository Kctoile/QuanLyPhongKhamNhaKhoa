package com.dentalclinic.controller;

import com.dentalclinic.dao.NguoiDungDAO;
import com.dentalclinic.model.NguoiDung;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet xử lý đăng nhập - xác thực và redirect theo vai trò
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    /**
     * POST: Nhận email, mật khẩu từ form đăng nhập
     * - Đúng: lưu user + role vào session, redirect theo role (ADMIN/DOCTOR/STAFF)
     * - Sai: hiển thị lỗi và quay lại form login
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");

        NguoiDungDAO dao = new NguoiDungDAO();
        NguoiDung user = dao.login(email, matKhau);

        if (user != null) {

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("maND", user.getMaND());

            String role = user.getVaiTro().getTenVaiTro().toUpperCase().trim();
            session.setAttribute("role", role);

            if ("ADMIN".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else if ("DOCTOR".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/bacsi.jsp");
            } else if ("STAFF".equals(role)) {
                response.sendRedirect(request.getContextPath() + "/staff");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }

        } else {
            request.setAttribute("error", "Sai email hoặc mật khẩu");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
