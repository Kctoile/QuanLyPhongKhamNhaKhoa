package com.dentalclinic.controller;

import com.dentalclinic.dao.NguoiDungDAO;
import com.dentalclinic.model.NguoiDung;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet xử lý đăng ký người dùng mới (Customer)
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private final NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String soDienThoai = request.getParameter("soDienThoai");

        // Validate basic
        if (hoTen == null || email == null || matKhau == null || soDienThoai == null ||
                hoTen.trim().isEmpty() || email.trim().isEmpty() || matKhau.trim().isEmpty()
                || soDienThoai.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        NguoiDung nd = new NguoiDung();
        nd.setHoTen(hoTen);
        nd.setEmail(email);
        nd.setMatKhau(matKhau);
        nd.setSoDienThoai(soDienThoai);

        // 3 = CUSTOMER role id
        boolean isSuccess = nguoiDungDAO.addUser(nd, 3);

        if (isSuccess) {
            request.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại. Email có thể đã tồn tại.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
