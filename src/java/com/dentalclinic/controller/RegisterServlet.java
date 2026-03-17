package com.dentalclinic.controller;

import com.dentalclinic.dao.RoleDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");

        // Validate basic
        if (fullName == null || email == null || password == null || phone == null ||
                fullName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()
                || phone.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setGender(gender);
        user.setAddress(address);

        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                user.setDob(java.sql.Date.valueOf(dobStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid date of birth: " + dobStr);
            }
        }

        // Assign null role directly, admin will assign it later,
        // per user request.
        Integer unassignedRoleId = null;

        boolean isSuccess = userDAO.addUser(user, unassignedRoleId);

        if (isSuccess) {
            request.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại. Email có thể đã tồn tại.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
