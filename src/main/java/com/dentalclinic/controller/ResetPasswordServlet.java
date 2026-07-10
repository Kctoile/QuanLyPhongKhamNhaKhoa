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

    private static final String TOKEN = "token";
    private static final String ERROR = "error";
    private static final String RESET_PASSWORD_JSP = "reset_password.jsp";
    private static final String INVALID_LINK = "Link đặt lại mật khẩu không hợp lệ hoặc đã hết hạn.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter(TOKEN);

        if (token == null || token.trim().isEmpty()) {
            request.setAttribute(ERROR, INVALID_LINK);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
            return;
        }

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.getValidToken(token);

        if (prt == null) {
            request.setAttribute(ERROR, INVALID_LINK);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
            return;
        }

        request.setAttribute(TOKEN, token);
        request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String token = request.getParameter(TOKEN);
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
        PasswordResetToken prt = tokenDAO.getValidToken(token);

        if (prt == null) {
            request.setAttribute(ERROR, INVALID_LINK);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
            return;
        }

        if (newPassword == null || newPassword.length() < 6) {
            request.setAttribute(ERROR, "Mật khẩu phải có ít nhất 6 ký tự.");
            request.setAttribute(TOKEN, token);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute(ERROR, "Mật khẩu xác nhận không khớp.");
            request.setAttribute(TOKEN, token);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
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
            request.setAttribute(ERROR, "Đã xảy ra lỗi. Vui lòng thử lại.");
            request.setAttribute(TOKEN, token);
            request.getRequestDispatcher(RESET_PASSWORD_JSP).forward(request, response);
        }
    }
}
