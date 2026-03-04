package com.dentalclinic.controller;

import com.dentalclinic.dao.ThuocDAO;
import com.dentalclinic.model.Thuoc;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý thuốc - Chỉ ADMIN được truy cập
 */
@WebServlet("/thuoc")
public class ThuocServlet extends HttpServlet {

    private ThuocDAO dao = new ThuocDAO();

    /**
     * Kiểm tra quyền ADMIN trước khi cho phép truy cập
     */
    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        return "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * GET: Xử lý xem danh sách, form thêm, form sửa, xóa
     * - action=add: hiển thị form thêm mới
     * - action=edit&id=X: hiển thị form sửa
     * - action=delete&id=X: xóa thuốc
     * - không có action: hiển thị danh sách
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!checkAdmin(request, response)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            request.getRequestDispatcher("add_thuoc.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                int maThuoc = Integer.parseInt(idStr);
                Thuoc t = dao.getById(maThuoc);
                if (t != null) {
                    request.setAttribute("thuoc", t);
                    request.getRequestDispatcher("edit_thuoc.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("thuoc");
            return;
        }

        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                dao.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect("thuoc");
            return;
        }

        // Mặc định: hiển thị danh sách thuốc
        List<Thuoc> list = dao.getAll();
        request.setAttribute("listThuoc", list);
        request.getRequestDispatcher("thuoc.jsp").forward(request, response);
    }

    /**
     * POST: Xử lý thêm mới (insert) hoặc cập nhật (update)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!checkAdmin(request, response)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");

        if ("update".equals(action)) {
            int maThuoc = Integer.parseInt(request.getParameter("maThuoc"));
            String tenThuoc = request.getParameter("tenThuoc");
            double donGia = Double.parseDouble(request.getParameter("donGia"));
            int soLuongTon = Integer.parseInt(request.getParameter("soLuongTon"));

            Thuoc t = new Thuoc();
            t.setMaThuoc(maThuoc);
            t.setTenThuoc(tenThuoc);
            t.setDonGia(donGia);
            t.setSoLuongTon(soLuongTon);

            dao.update(t);

        } else {
            String tenThuoc = request.getParameter("tenThuoc");
            double donGia = Double.parseDouble(request.getParameter("donGia"));
            int soLuongTon = Integer.parseInt(request.getParameter("soLuongTon"));

            Thuoc t = new Thuoc();
            t.setTenThuoc(tenThuoc);
            t.setDonGia(donGia);
            t.setSoLuongTon(soLuongTon);

            dao.insert(t);
        }

        response.sendRedirect("thuoc");
    }
}
