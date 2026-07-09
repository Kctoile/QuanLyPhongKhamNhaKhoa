package com.dentalclinic.controller;

import com.dentalclinic.dao.PasswordResetTokenDAO;
import com.dentalclinic.dao.UserDAO;
import com.dentalclinic.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;
import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    // Chỉ hiển thị link trực tiếp khi là môi trường development
    private static final boolean DEV_MODE = false;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("forgot_password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String email = request.getParameter("email");

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            Timestamp expiry = Timestamp.valueOf(LocalDateTime.now().plusHours(1));

            PasswordResetTokenDAO tokenDAO = new PasswordResetTokenDAO();
            tokenDAO.createToken(user.getUserId(), token, expiry);

            String resetLink = request.getRequestURL().toString()
                    .replace("/forgot-password", "/reset-password")
                    + "?token=" + token;

            // Thử gửi email
            boolean emailSent = false;
            try {
                sendResetEmail(user.getEmail(), resetLink);
                emailSent = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (emailSent) {
                request.setAttribute("message", "Vui lòng kiểm tra email để nhận hướng dẫn đặt lại mật khẩu.");
            } else {
                // Chỉ hiển thị link khi là môi trường development
                if (DEV_MODE) {
                    request.setAttribute("resetLink", resetLink);
                    request.setAttribute("message", "Không thể gửi email. Bạn có thể dùng link bên dưới để đặt lại mật khẩu:");
                } else {
                    request.setAttribute("message", "Vui lòng kiểm tra email để nhận hướng dẫn đặt lại mật khẩu.");
                }
            }
        } else {
            request.setAttribute("message", "Vui lòng kiểm tra email để nhận hướng dẫn đặt lại mật khẩu.");
        }

        request.getRequestDispatcher("forgot_password.jsp").forward(request, response);
    }

    private void sendResetEmail(String recipientEmail, String resetLink) throws Exception {
        String smtpHost = System.getenv("SMTP_HOST");
        String smtpPort = System.getenv("SMTP_PORT");
        String smtpUser = System.getenv("SMTP_USER");
        String smtpPass = System.getenv("SMTP_PASS");

        if (smtpHost == null || smtpHost.isEmpty()) {
            throw new Exception("SMTP chưa được cấu hình");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort != null ? smtpPort : "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPass);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUser));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Đặt lại mật khẩu - Phòng khám nha khoa");

        String htmlContent = "<html><body>"
                + "<h2>Yêu cầu đặt lại mật khẩu</h2>"
                + "<p>Bạn nhận được email này vì đã yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                + "<p>Nhấp vào link bên dưới để đặt lại mật khẩu (link có hiệu lực trong 1 giờ):</p>"
                + "<p><a href='" + resetLink + "'>" + resetLink + "</a></p>"
                + "<p>Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.</p>"
                + "</body></html>";

        message.setContent(htmlContent, "text/html; charset=UTF-8");
        Transport.send(message);
    }
}
