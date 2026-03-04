package com.dentalclinic.controller;

import com.dentalclinic.dao.NguoiDungDAO;
import com.dentalclinic.model.NguoiDung;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý người dùng - Chỉ ADMIN mới được truy cập
 */
@WebServlet("/nguoidung")
public class NguoiDungServlet extends HttpServlet {

    /**
     * Kiểm tra quyền ADMIN - dùng attribute "role" do LoginServlet lưu trong session
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
     * GET: Hiển thị danh sách toàn bộ người dùng
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        NguoiDungDAO dao = new NguoiDungDAO();
        List<NguoiDung> list = dao.getAllUsers();

        request.setAttribute("users", list);
        request.getRequestDispatcher("nguoidung.jsp")
                .forward(request, response);
    }
}
