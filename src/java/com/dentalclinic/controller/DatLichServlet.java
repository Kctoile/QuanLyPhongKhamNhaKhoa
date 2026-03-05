package com.dentalclinic.controller;

import com.dentalclinic.dao.DichVuDAO;
import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.dao.NguoiDungDAO;
import com.dentalclinic.model.DichVu;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.NguoiDung;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Servlet đặt lịch khám - Customer chọn bác sĩ, thời gian, dịch vụ
 */
@WebServlet("/datlich")
public class DatLichServlet extends HttpServlet {

    private NguoiDungDAO userDAO = new NguoiDungDAO();
    private DichVuDAO dvDAO = new DichVuDAO();
    private LichHenDAO lhDAO = new LichHenDAO();

    /**
     * GET: Hiển thị form đặt lịch
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<NguoiDung> doctors = userDAO.getDoctors();
        List<DichVu> services = dvDAO.getAll();

        request.setAttribute("doctors", doctors);
        request.setAttribute("services", services);
        request.getRequestDispatcher("datlich.jsp").forward(request, response);
    }

    /**
     * POST: Xử lý đặt lịch - kiểm tra slot còn trống rồi lưu
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        NguoiDung user = (NguoiDung) session.getAttribute("user");
        int maBacSi = Integer.parseInt(request.getParameter("maBacSi"));
        Date ngayKham = Date.valueOf(request.getParameter("ngayKham"));
        String gioStr = request.getParameter("gioKham");
        int maDV = Integer.parseInt(request.getParameter("maDV"));
        String ghiChu = request.getParameter("ghiChu");

        Time gioKham = Time.valueOf(gioStr + ":00");

        List<String> available = lhDAO.getAvailableSlots(maBacSi, ngayKham);
        if (!available.contains(gioStr)) {
            request.setAttribute("error", "Khung giờ này đã được đặt. Vui lòng chọn giờ khác.");
            List<NguoiDung> doctors = userDAO.getDoctors();
            List<DichVu> services = dvDAO.getAll();
            request.setAttribute("doctors", doctors);
            request.setAttribute("services", services);
            request.getRequestDispatcher("datlich.jsp").forward(request, response);
            return;
        }

        LichHen lh = new LichHen();
        lh.setMaND(user.getMaND());
        lh.setMaBacSi(maBacSi);
        lh.setMaDV(maDV);
        lh.setNgayKham(ngayKham);
        lh.setGioKham(gioKham);
        lh.setTrangThai("Chờ xác nhận");
        lh.setGhiChu(ghiChu);

        lhDAO.insert(lh);

        session.setAttribute("successMessage", "Đặt lịch thành công! Vui lòng chờ xác nhận.");
        response.sendRedirect("datlich");
    }
}
