package com.dentalclinic.controller;

import com.dentalclinic.dao.NguoiDungDAO;
import com.dentalclinic.model.NguoiDung;
import com.dentalclinic.model.VaiTro;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet quản lý người dùng - Chỉ ADMIN mới được truy cập
 */
@WebServlet("/nguoidung")
public class NguoiDungServlet extends HttpServlet {

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        String action = request.getParameter("action");
        NguoiDungDAO dao = new NguoiDungDAO();

        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            NguoiDung user = dao.getUserById(id);
            request.setAttribute("user", user);
            request.setAttribute("formAction", "update");
            request.getRequestDispatcher("nguoidung_form.jsp").forward(request, response);
        } else if ("add".equals(action)) {
            // Provide a new empty user object so the form fields don't break/populate with
            // previous data
            request.setAttribute("user", new NguoiDung());
            request.setAttribute("formAction", "add");
            request.getRequestDispatcher("nguoidung_form.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteUser(id);
            response.sendRedirect("nguoidung");
        } else {
            List<NguoiDung> list = dao.getAllUsers();

            // Lọc theo role nếu có tham số role
            String roleFilter = request.getParameter("role");
            if (roleFilter != null && !roleFilter.trim().isEmpty() && !"ALL".equalsIgnoreCase(roleFilter)) {
                if ("UNASSIGNED".equalsIgnoreCase(roleFilter)) {
                    list = list.stream()
                            .filter(u -> u.getVaiTro() == null)
                            .collect(Collectors.toList());
                } else {
                    list = list.stream()
                            .filter(u -> u.getVaiTro() != null && roleFilter.equalsIgnoreCase(u.getVaiTro().getTenVaiTro()))
                            .collect(Collectors.toList());
                }
            }

            request.setAttribute("currentRole", roleFilter != null ? roleFilter.toUpperCase() : "ALL");
            request.setAttribute("users", list);
            request.getRequestDispatcher("nguoidung.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!checkAdmin(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        NguoiDungDAO dao = new NguoiDungDAO();

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String soDienThoai = request.getParameter("soDienThoai");
        int maVaiTro = Integer.parseInt(request.getParameter("maVaiTro"));

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen);
        nd.setEmail(email);
        nd.setMatKhau(matKhau);
        nd.setSoDienThoai(soDienThoai);

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("maND"));
            nd.setMaND(id);
            dao.updateUser(nd, maVaiTro);
        } else if ("add".equals(action)) {
            dao.addUser(nd, maVaiTro);
        }

        response.sendRedirect("nguoidung");
    }
}
