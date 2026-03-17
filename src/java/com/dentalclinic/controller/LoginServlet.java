package com.dentalclinic.controller;

import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.User;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(email, password);

        if (user != null) {

            // If user has no role assigned yet, deny login and ask to wait for admin
            if (user.getRole() == null) {
                request.setAttribute("error", "Tài khoản chưa được gán vai trò. Vui lòng chờ quản trị viên xét duyệt.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());

            String role = user.getRole().getRoleName().toUpperCase().trim();
            session.setAttribute("role", role);

            if (role == null || role.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            switch (role) {
                case "ADMIN":
                    response.sendRedirect(request.getContextPath() + "/admin");
                    break;
                case "DOCTOR":
                    response.sendRedirect(request.getContextPath() + "/doctor");
                    break;
                case "STAFF":
                    response.sendRedirect(request.getContextPath() + "/staff");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    break;
            }

        } else {
            request.setAttribute("error", "Sai email hoặc mật khẩu");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
