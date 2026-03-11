package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import com.dentalclinic.model.LichHen;
import com.dentalclinic.model.LichHenDisplayDTO;
import com.dentalclinic.model.NguoiDung;

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
        if ("xong".equals(action)) {
            int maLich = Integer.parseInt(request.getParameter("maLich"));
            lhDAO.updateTrangThai(maLich, "Đã khám xong");
            response.sendRedirect("staff");
            return;
        }

        String ngayParam = request.getParameter("ngay");
        List<LichHenDisplayDTO> list;
        if (ngayParam != null && !ngayParam.isEmpty()) {
            System.out.println("[Staff] received filter date param: " + ngayParam);
            try {
                Date ngay = Date.valueOf(ngayParam);
                list = lhDAO.getByDate(ngay);
                System.out.println("[Staff] filtered list size: " + list.size());
            } catch (IllegalArgumentException e) {
                // bad format, show all
                System.out.println("[Staff] invalid date format, showing all");
                list = lhDAO.getAllDisplay();
            }
            request.setAttribute("filterNgay", ngayParam);
        } else {
            list = lhDAO.getAllDisplay();
        }
        request.setAttribute("listLichHen", list);
        // gửi danh sách dịch vụ và bác sĩ cho phần đặt mới
        request.setAttribute("services", dvDAO.getAll());
        request.setAttribute("doctors", userDAO.getDoctors());
        request.getRequestDispatcher("staff_le_tan.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkStaff(request, response)) return;

        // get customer's name as free text
        String tenKhachHang = request.getParameter("tenKhachHang");
        String email = request.getParameter("email");
        String soDienThoai = request.getParameter("soDienThoai");
        if (tenKhachHang == null || tenKhachHang.trim().isEmpty()) {
            request.getSession().setAttribute("staffError", "Tên khách hàng không được để trống.");
            response.sendRedirect("staff");
            return;
        }
        tenKhachHang = tenKhachHang.trim();
        if (email != null) email = email.trim();
        if (soDienThoai != null) soDienThoai = soDienThoai.trim();

        int maND;
        // try to find existing customer by name
        NguoiDung existing = userDAO.getCustomerByName(tenKhachHang);
        if (existing != null) {
            maND = existing.getMaND();
            // update contact info if provided
            boolean changed = false;
            if (email != null && !email.isEmpty() && !email.equals(existing.getEmail())) {
                existing.setEmail(email);
                changed = true;
            }
            if (soDienThoai != null && !soDienThoai.isEmpty() && !soDienThoai.equals(existing.getSoDienThoai())) {
                existing.setSoDienThoai(soDienThoai);
                changed = true;
            }
            if (changed) {
                userDAO.updateUser(existing, existing.getVaiTro().getMaVaiTro());
            }
        } else {
            // create a new CUSTOMER entry using given info
            NguoiDung nd = new NguoiDung();
            nd.setHoTen(tenKhachHang);
            nd.setEmail(email != null ? email : "");
            nd.setMatKhau("");
            nd.setSoDienThoai(soDienThoai != null ? soDienThoai : "");
            Integer roleId = userDAO.getRoleIdByName("CUSTOMER");
            if (roleId == null) {
                request.getSession().setAttribute("staffError", "Không tìm thấy vai trò CUSTOMER.");
                response.sendRedirect("staff");
                return;
            }
            userDAO.addUser(nd, roleId);
            // fetch again to get generated id
            existing = userDAO.getCustomerByName(tenKhachHang);
            if (existing == null) {
                request.getSession().setAttribute("staffError", "Không thể tạo khách hàng mới.");
                response.sendRedirect("staff");
                return;
            }
            maND = existing.getMaND();
        }

        int maBacSi = Integer.parseInt(request.getParameter("maBacSi"));
        Date ngayKham = Date.valueOf(request.getParameter("ngayKham"));
        String gioStr = request.getParameter("gioKham");
        Time gioKham = Time.valueOf(gioStr + ":00");
        int maDV = Integer.parseInt(request.getParameter("maDV"));
        String ghiChu = request.getParameter("ghiChu");

        // Chặn đặt trùng: kiểm tra slot còn trống trước khi insert
        if (!lhDAO.getAvailableSlots(maBacSi, ngayKham).contains(gioStr)) {
            request.getSession().setAttribute("staffError", "Giờ khám đã được đặt. Vui lòng chọn giờ khác.");
            response.sendRedirect("staff");
            return;
        }

        LichHen lh = new LichHen();
        lh.setMaND(maND);
        lh.setMaBacSi(maBacSi);
        lh.setMaDV(maDV);
        lh.setNgayKham(ngayKham);
        lh.setGioKham(gioKham);
        lh.setTrangThai("Chờ xác nhận");
        lh.setGhiChu(ghiChu);

        lhDAO.insert(lh);
        request.getSession().setAttribute("staffSuccess", "Đặt lịch thành công!");
        response.sendRedirect("staff");
    }
}
