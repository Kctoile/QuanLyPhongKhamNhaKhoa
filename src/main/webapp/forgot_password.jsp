<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quên mật khẩu</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #1a2a4a 0%, #2c3e6b 100%);
                min-height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .card {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 8px 32px rgba(0,0,0,0.2);
                padding: 40px;
                width: 420px;
                max-width: 90%;
            }
            .card h2 {
                text-align: center;
                color: #1a2a4a;
                margin-bottom: 8px;
            }
            .card p.subtitle {
                text-align: center;
                color: #666;
                font-size: 14px;
                margin-bottom: 24px;
            }
            .form-group {
                margin-bottom: 16px;
            }
            .form-group label {
                display: block;
                font-weight: 600;
                margin-bottom: 6px;
                font-size: 14px;
                color: #333;
            }
            .form-group input {
                width: 100%;
                padding: 12px 14px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 15px;
                transition: border-color 0.2s;
            }
            .form-group input:focus {
                outline: none;
                border-color: #1a2a4a;
            }
            .btn-submit {
                width: 100%;
                padding: 12px;
                background: #1a2a4a;
                color: #fff;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                cursor: pointer;
                font-weight: 600;
                transition: background 0.2s;
            }
            .btn-submit:hover {
                background: #2c3e6b;
            }
            .message {
                background: #d4edda;
                color: #155724;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
                text-align: center;
            }
            .error {
                background: #f8d7da;
                color: #721c24;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
                text-align: center;
            }
            .reset-link-box {
                background: #fff3cd;
                color: #856404;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
                text-align: center;
                word-break: break-all;
            }
            .reset-link-box a {
                color: #1a2a4a;
                font-weight: 600;
            }
            .back-link {
                display: block;
                text-align: center;
                margin-top: 16px;
                color: #1a2a4a;
                text-decoration: none;
                font-size: 14px;
            }
            .back-link:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <div class="card">
            <h2>Quên mật khẩu</h2>
            <p class="subtitle">Nhập email của bạn để nhận link đặt lại mật khẩu</p>

            <c:if test="${not empty message}">
                <div class="message">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <c:if test="${not empty resetLink}">
                <div class="reset-link-box">
                    <strong>Link đặt lại mật khẩu:</strong><br/>
                    <a href="${resetLink}">${resetLink}</a>
                </div>
            </c:if>

            <form action="forgot-password" method="post">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required
                           placeholder="Nhập email của bạn" />
                </div>
                <button type="submit" class="btn-submit">Gửi yêu cầu</button>
            </form>

            <a href="login.jsp" class="back-link">Quay lại đăng nhập</a>
        </div>
    </body>
</html>
