package com.dentalclinic.controller;

import com.dentalclinic.dao.PasswordResetTokenDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.PasswordResetToken;
import com.dentalclinic.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            return;
        }

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.getValidToken(token);

        if (prt == null) {
            request.setAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            return;
        }

        request.setAttribute("token", token);
        request.getRequestDispatcher("reset_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.getValidToken(token);

        if (prt == null) {
            request.setAttribute("error", "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.");
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updatePassword(prt.getUserId(), hashedPassword);

        if (updated) {
            tokenDAO.markTokenUsed(token);
            request.setAttribute("message", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đã xảy ra lỗi. Vui lòng thử lại.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("reset_password.jsp").forward(request, response);
        }
    }
}
