package com.dentalclinic.controller;

import com.dentalclinic.dao.LichHenDAO;
import com.dentalclinic.model.LichHenDisplayDTO;
import com.dentalclinic.model.NguoiDung;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Servlet hiển thị lịch hẹn của người dùng (CUSTOMER).
 * Cho phép lọc theo ngày bằng tham số `ngay` (dạng yyyy-MM-dd).
 */
@WebServlet("/lichhen")
public class LichHenServlet extends HttpServlet {

    private LichHenDAO lhDAO = new LichHenDAO();

    private boolean checkCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        if (!"CUSTOMER".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            // admin vẫn có thể xem nếu cần
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkCustomer(request, response)) {
            return;
        }

        NguoiDung user = (NguoiDung) request.getSession().getAttribute("user");
        int maND = user.getMaND();

        String ngayParam = request.getParameter("ngay");
        List<LichHenDisplayDTO> list;
        if (ngayParam != null && !ngayParam.isEmpty()) {
            try {
                Date date = Date.valueOf(ngayParam);
                list = lhDAO.getByMaNDAndDate(maND, date);
            } catch (IllegalArgumentException e) {
                // giá trị không phải yyyy-MM-dd, lấy toàn bộ
                list = lhDAO.getByMaND(maND);
            }
        } else {
            list = lhDAO.getByMaND(maND);
        }

        request.setAttribute("listLichHen", list);
        request.getRequestDispatcher("lichhen.jsp").forward(request, response);
    }
}
