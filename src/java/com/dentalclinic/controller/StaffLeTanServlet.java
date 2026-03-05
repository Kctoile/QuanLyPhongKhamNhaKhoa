package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.LichHenDisplayDTO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Servlet lễ tân - Staff: Kiểm tra lịch hẹn, đặt lịch walk-in, hướng dẫn vào phòng
 */
@WebServlet("/staff")
public class StaffLeTanServlet extends HttpServlet {

    private LichHenDAO lhDAO = new LichHenDAO();
    private NguoiDungDAO userDAO = new NguoiDungDAO();
    private DichVuDAO dvDAO = new DichVuDAO();

    private boolean checkStaff(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        if (!"STAFF".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkStaff(request, response)) return;

        String action = request.getParameter("action");

        if ("vaophong".equals(action)) {
            int maLich = Integer.parseInt(request.getParameter("maLich"));
            lhDAO.updateTrangThai(maLich, "Đã vào khám");
            response.sendRedirect("staff");
            return;
        }

        List<LichHenDisplayDTO> list = lhDAO.getAllDisplay();
        request.setAttribute("listLichHen", list);
        request.setAttribute("customers", userDAO.getCustomers());
        request.setAttribute("services", dvDAO.getAll());
        request.setAttribute("doctors", userDAO.getDoctors());
        request.getRequestDispatcher("staff_le_tan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkStaff(request, response)) return;

        int maND = Integer.parseInt(request.getParameter("maND"));
        int maBacSi = Integer.parseInt(request.getParameter("maBacSi"));
        Date ngayKham = Date.valueOf(request.getParameter("ngayKham"));
        Time gioKham = Time.valueOf(request.getParameter("gioKham") + ":00");
        int maDV = Integer.parseInt(request.getParameter("maDV"));
        String ghiChu = request.getParameter("ghiChu");

        LichHen lh = new LichHen();
        lh.setMaND(maND);
        lh.setMaBacSi(maBacSi);
        lh.setMaDV(maDV);
        lh.setNgayKham(ngayKham);
        lh.setGioKham(gioKham);
        lh.setTrangThai("Chờ xác nhận");
        lh.setGhiChu(ghiChu);

        lhDAO.insert(lh);

        response.sendRedirect("staff");
    }
}
