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
        request.setAttribute("listLichHen", list);
        request.setAttribute("listThuoc", thuocDAO.getAll());
        request.getRequestDispatcher("bacsi.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        if (!checkDoctor(request, response)) return;

        int maLich = Integer.parseInt(request.getParameter("maLich"));
        String ketQua = request.getParameter("ketQua");
        String huongDan = request.getParameter("huongDan");

        String[] maThuocArr = request.getParameterValues("maThuoc");
        String[] soLuongArr = request.getParameterValues("soLuong");

        KetQuaKham kq = new KetQuaKham();
        kq.setMaLich(maLich);
        kq.setKetQua(ketQua);
        int maKQ = kqDAO.insert(kq);

        if (maKQ > 0 && huongDan != null) {
            DonThuoc dt = new DonThuoc();
            dt.setMaKQ(maKQ);
            dt.setHuongDan(huongDan);
            int maDon = dtDAO.insertDonThuoc(dt);

            if (maDon > 0 && maThuocArr != null && soLuongArr != null) {
                for (int i = 0; i < maThuocArr.length; i++) {
                    int sl = Integer.parseInt(soLuongArr[i]);
                    if (sl <= 0) continue;
                    int maThuoc = Integer.parseInt(maThuocArr[i]);
                    double donGia = thuocDAO.getById(maThuoc).getDonGia();
                    ChiTietDonThuoc ct = new ChiTietDonThuoc();
                    ct.setMaDon(maDon);
                    ct.setMaThuoc(maThuoc);
                    ct.setSoLuong(sl);
                    ct.setDonGia(donGia);
                    dtDAO.insertChiTiet(ct);
                }
            }
        }

        lhDAO.updateTrangThai(maLich, "Đã khám xong");
        response.sendRedirect("bacsi");
    }
}
