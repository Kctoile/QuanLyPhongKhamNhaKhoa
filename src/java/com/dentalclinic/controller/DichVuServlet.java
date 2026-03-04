package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.model.DichVu;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý dịch vụ - Danh sách: tất cả user xem được. Thêm/Sửa/Xóa: chỉ ADMIN
 */
@WebServlet("/dichvu")
public class DichVuServlet extends HttpServlet {

    private DichVuDAO dao = new DichVuDAO();

    /**
     * Kiểm tra quyền ADMIN trước khi cho phép thêm/sửa/xóa
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
     * - action=add: hiển thị form thêm (chỉ ADMIN)
     * - action=edit&id=X: hiển thị form sửa (chỉ ADMIN)
     * - action=delete&id=X: xóa dịch vụ (chỉ ADMIN)
     * - không có action: hiển thị danh sách
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            // Form thêm mới - chỉ ADMIN
            if (!checkAdmin(request, response)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            request.getRequestDispatcher("add_dichvu.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(action)) {
            // Form sửa - chỉ ADMIN
            if (!checkAdmin(request, response)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                int maDV = Integer.parseInt(idStr);
                DichVu dv = dao.getById(maDV);
                if (dv != null) {
                    request.setAttribute("dichvu", dv);
                    request.getRequestDispatcher("edit_dichvu.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("dichvu");
            return;
        }

        if ("delete".equals(action)) {
            // Xóa - chỉ ADMIN
            if (!checkAdmin(request, response)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                dao.delete(Integer.parseInt(idStr));
            }
            response.sendRedirect("dichvu");
            return;
        }

        // Mặc định: hiển thị danh sách dịch vụ (tất cả user xem được)
        List<DichVu> list = dao.getAll();
        request.setAttribute("listDichVu", list);
        request.getRequestDispatcher("dichvu.jsp").forward(request, response);
    }

    /**
     * POST: Xử lý thêm mới (insert) hoặc cập nhật (update)
     * - action=insert: thêm dịch vụ mới
     * - action=update: cập nhật dịch vụ
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
            // Cập nhật dịch vụ
            int maDV = Integer.parseInt(request.getParameter("maDV"));
            String tenDV = request.getParameter("tenDV");
            String moTa = request.getParameter("moTa");
            int gia = Integer.parseInt(request.getParameter("gia"));
            int thoiGian = Integer.parseInt(request.getParameter("thoiGian"));

            DichVu dv = new DichVu();
            dv.setMaDV(maDV);
            dv.setTenDV(tenDV);
            dv.setMoTa(moTa);
            dv.setGia(gia);
            dv.setThoiGian(thoiGian);

            dao.update(dv);

        } else {
            // Thêm mới - nhận cả ten và tenDV (add_dichvu dùng ten, form khác có thể dùng tenDV)
            String tenDV = request.getParameter("tenDV");
            if (tenDV == null || tenDV.isEmpty()) {
                tenDV = request.getParameter("ten");
            }
            String moTa = request.getParameter("moTa");
            double gia = Double.parseDouble(request.getParameter("gia"));
            int thoiGian = Integer.parseInt(request.getParameter("thoiGian"));

            DichVu dv = new DichVu();
            dv.setTenDV(tenDV);
            dv.setMoTa(moTa);
            dv.setGia((int) gia);
            dv.setThoiGian(thoiGian);

            dao.insert(dv);
        }

        response.sendRedirect("dichvu");
    }
}
