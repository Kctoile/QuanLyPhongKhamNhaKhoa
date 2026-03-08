package com.dentalclinic.controller;

import com.dentalclinic.dao.*;
import com.dentalclinic.model.*;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

/**
 * Servlet bác sĩ - Ghi kết quả khám, kê đơn thuốc
 */
@WebServlet("/bacsi")
public class BacSiServlet extends HttpServlet {

    private LichHenDAO lhDAO = new LichHenDAO();
    private KetQuaKhamDAO kqDAO = new KetQuaKhamDAO();
    private DonThuocDAO dtDAO = new DonThuocDAO();
    private ThuocDAO thuocDAO = new ThuocDAO();

    private boolean checkDoctor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        String role = session.getAttribute("role").toString();
        if (!"DOCTOR".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkDoctor(request, response)) return;

        NguoiDung user = (NguoiDung) request.getSession().getAttribute("user");
        int maBacSi = user.getMaND();

        List<LichHenDisplayDTO> list = lhDAO.getByMaBacSi(maBacSi);
        List<LichHenDisplayDTO> listHomNay = lhDAO.getByMaBacSiHomNay(maBacSi);
        request.setAttribute("listLichHen", list);
        request.setAttribute("listLichHenHomNay", listHomNay);
        request.setAttribute("listThuoc", thuocDAO.getAll());
        request.getRequestDispatcher("bacsi.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkDoctor(request, response)) return;

        String maLichStr = request.getParameter("maLich");
        String ketQua = request.getParameter("ketQua");
        String huongDan = request.getParameter("huongDan");
        if (huongDan == null) huongDan = "";

        if (maLichStr == null || maLichStr.isEmpty() || ketQua == null || ketQua.trim().isEmpty()) {
            response.sendRedirect("bacsi?error=1");
            return;
        }

        int maLich;
        try {
            maLich = Integer.parseInt(maLichStr);
        } catch (NumberFormatException e) {
            response.sendRedirect("bacsi?error=1");
            return;
        }

        // Kiểm tra đã có kết quả khám chưa (tránh trùng)
        if (kqDAO.getByMaLich(maLich) != null) {
            response.sendRedirect("bacsi?error=2");
            return;
        }

        KetQuaKham kq = new KetQuaKham();
        kq.setMaLich(maLich);
        kq.setKetQua(ketQua.trim());
        int maKQ = kqDAO.insert(kq);

        if (maKQ > 0) {
            // Tạo đơn thuốc (có thể không có thuốc - huongDan ghi "Không cần uống thuốc")
            DonThuoc dt = new DonThuoc();
            dt.setMaKQ(maKQ);
            dt.setHuongDan(huongDan.trim().isEmpty() ? "Không kê thuốc" : huongDan.trim());
            int maDon = dtDAO.insertDonThuoc(dt);

            if (maDon > 0) {
                String[] maThuocArr = request.getParameterValues("maThuoc");
                String[] soLuongArr = request.getParameterValues("soLuong");
                if (maThuocArr != null && soLuongArr != null) {
                    int len = Math.min(maThuocArr.length, soLuongArr.length);
                    for (int i = 0; i < len; i++) {
                        int sl;
                        try {
                            sl = Integer.parseInt(soLuongArr[i]);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        if (sl <= 0) continue;
                        int maThuoc;
                        try {
                            maThuoc = Integer.parseInt(maThuocArr[i]);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        var thuoc = thuocDAO.getById(maThuoc);
                        if (thuoc == null) continue;
                        ChiTietDonThuoc ct = new ChiTietDonThuoc();
                        ct.setMaDon(maDon);
                        ct.setMaThuoc(maThuoc);
                        ct.setSoLuong(sl);
                        ct.setDonGia(thuoc.getDonGia());
                        dtDAO.insertChiTiet(ct);
                    }
                }
            }
        }

        lhDAO.updateTrangThai(maLich, "Đã khám xong");
        response.sendRedirect("bacsi");
    }
}
