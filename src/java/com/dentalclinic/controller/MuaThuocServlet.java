package com.dentalclinic.controller;

import com.dentalclinic.dao.DonThuocDAO;
import com.dentalclinic.dao.ThuocDAO;
import com.dentalclinic.model.PrescriptionDisplayDTO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet mua thuốc - Khách mua thuốc theo đơn (toàn bộ hoặc 1 phần), thanh
 * toán theo số lượng
 */
@WebServlet("/muathuoc")
public class MuaThuocServlet extends HttpServlet {

    private DonThuocDAO dtDAO = new DonThuocDAO();
    private ThuocDAO thuocDAO = new ThuocDAO();

    private boolean checkLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkLogin(request, response))
            return;

        int maND = (int) request.getSession().getAttribute("maND");
        List<Object[]> rawList = dtDAO.getDonThuocByMaND(maND);
        List<PrescriptionDisplayDTO> donThuocList = new ArrayList<>();

        for (Object[] row : rawList) {
            int maDon = (int) row[0];
            int maLich = (int) row[2];
            PrescriptionDisplayDTO dto = new PrescriptionDisplayDTO();
            dto.setMaDon(maDon);
            dto.setMaLich(maLich);
            dto.setHuongDan((String) row[3]);
            dto.setNgayKham(row[4] != null ? (java.util.Date) row[4] : null);

            for (Object[] ct : dtDAO.getChiTietByMaDon(maDon)) {
                PrescriptionDisplayDTO.ChiTietThuocDTO chiTiet = new PrescriptionDisplayDTO.ChiTietThuocDTO();
                chiTiet.setMaThuoc((int) ct[1]);
                chiTiet.setTenThuoc((String) ct[2]);
                chiTiet.setSoLuong((int) ct[3]);
                chiTiet.setDonGia((double) ct[4]);
                dto.addChiTiet(chiTiet);
            }
            donThuocList.add(dto);
        }

        request.setAttribute("donThuocList", donThuocList);
        request.getRequestDispatcher("muathuoc.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkLogin(request, response))
            return;

        // Chức năng thanh toán đã bị loại bỏ
        response.sendRedirect("muathuoc");
    }
}
