package com.dentalclinic.controller;

import com.dentalclinic.dao.ThanhToanDAO;
import com.dentalclinic.model.ThanhToan;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.dentalclinic.model.LichHenPaymentDTO;

import java.io.IOException;
import java.util.List;

/**
 * Servlet thanh toán - dành cho STAFF
 * Xem lịch hẹn chưa thanh toán và thực hiện thanh toán
 */
@WebServlet("/thanhtoan")
public class ThanhToanServlet extends HttpServlet {

    private ThanhToanDAO dao = new ThanhToanDAO();

    /**
     * Kiểm tra quyền STAFF hoặc ADMIN
     */
    private boolean checkStaffOrAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    /**
     * GET: Hiển thị danh sách lịch hẹn chưa thanh toán
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!checkStaffOrAdmin(request, response)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        List<LichHenPaymentDTO> list = dao.getLichHenChuaThanhToan();
        request.setAttribute("listLichHen", list);
        request.getRequestDispatcher("thanhtoan.jsp").forward(request, response);
    }

    /**
     * POST: Thực hiện thanh toán cho lịch hẹn
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!checkStaffOrAdmin(request, response)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        int maLich = Integer.parseInt(request.getParameter("maLich"));
        double tongTien = Double.parseDouble(request.getParameter("tongTien"));
        String phuongThuc = request.getParameter("phuongThuc");

        if (phuongThuc == null || phuongThuc.trim().isEmpty()) {
            phuongThuc = "Tiền mặt";
        }

        ThanhToan t = new ThanhToan();
        t.setMaLich(maLich);
        t.setTongTien(tongTien);
        t.setPhuongThuc(phuongThuc);
        t.setTrangThai("Đã thanh toán");

        dao.insert(t);

        response.sendRedirect("thanhtoan");
    }
}
