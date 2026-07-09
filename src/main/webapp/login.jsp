<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đăng nhập vào hệ thống quản lý phòng khám nha khoa">
    <title>Đăng nhập - Phòng Khám Nha Khoa</title>
    <link rel="stylesheet" href="css/global.css">
    <style>
        body {
            background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 60%, #0F4C81 100%);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: var(--space-6);
        }
        .login-wrapper {
            width: 100%;
            max-width: 420px;
        }
        .login-brand {
            text-align: center;
            margin-bottom: var(--space-6);
            color: #fff;
        }
        .login-brand svg {
            width: 48px; height: 48px;
            margin: 0 auto var(--space-3);
            display: block;
        }
        .login-brand h1 {
            font-size: 22px;
            font-weight: 700;
            color: #fff;
            margin-bottom: 4px;
        }
        .login-brand p { font-size: 14px; opacity: .8; color: #fff; }

        .login-card {
            background: #fff;
            border-radius: var(--radius-xl);
            padding: var(--space-10);
            box-shadow: 0 24px 64px rgba(0,0,0,.25);
        }
        .login-card h2 {
            font-size: 20px;
            font-weight: 700;
            margin-bottom: var(--space-6);
            text-align: center;
            color: var(--color-foreground);
        }
        .btn-login {
            width: 100%;
            padding: 13px;
            justify-content: center;
            font-size: 15px;
            margin-top: var(--space-2);
            background: var(--color-primary);
            color: #fff;
            border-radius: var(--radius-sm);
            box-shadow: 0 4px 14px rgba(15,118,110,.35);
        }
        .btn-login:hover { background: var(--color-primary-dark); transform: translateY(-1px); }

        .login-links {
            text-align: center;
            margin-top: var(--space-5);
            display: flex;
            flex-direction: column;
            gap: var(--space-2);
        }
        .login-links a {
            color: var(--color-primary);
            font-size: 13px;
            font-weight: 500;
        }
        .login-links a:hover { text-decoration: underline; }
        .link-sep { color: var(--color-muted-text); font-size: 13px; }

        .back-home {
            display: block;
            text-align: center;
            margin-top: var(--space-5);
            color: rgba(255,255,255,.75);
            font-size: 13px;
            text-decoration: none;
            transition: color var(--transition);
        }
        .back-home:hover { color: #fff; }
    </style>
</head>
<body>
    <div class="login-wrapper">
        <div class="login-brand">
            <svg fill="none" viewBox="0 0 24 24" stroke="white" stroke-width="1.5" aria-hidden="true">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 2C8.5 2 6 4.5 6 7c0 2 1 3.5 1.5 5C8 13.5 8 15 7 17c-1 2-.5 3 .5 4s2.5.5 3.5-1c.5-1 1-1 2-1s1.5 0 2 1c1 1.5 2.5 2 3.5 1s1.5-2 .5-4c-1-2-1-3.5-.5-5C19 10.5 20 9 20 7c0-2.5-2.5-5-6-5z"/>
            </svg>
            <h1>Dental Clinic</h1>
            <p>Phòng khám nha khoa chuyên nghiệp</p>
        </div>

        <div class="login-card">
            <h2>Đăng nhập</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-error" role="alert">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success" role="alert">${message}</div>
            </c:if>

            <form action="LoginServlet" method="post" novalidate>
                <div class="form-group">
                    <label class="form-label" for="email">Email</label>
                    <input type="email" id="email" name="email" class="form-control"
                           placeholder="name@example.com" value="${param.email}" required autocomplete="email">
                </div>
                <div class="form-group">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="••••••••" required autocomplete="current-password">
                </div>
                <button type="submit" class="btn btn-login">Đăng nhập</button>
            </form>

            <div class="login-links">
                <a href="forgot-password">Quên mật khẩu?</a>
                <span class="link-sep">Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a></span>
            </div>
        </div>

        <a href="index.jsp" class="back-home">← Trở về trang chủ</a>
    </div>
</body>
</html>
