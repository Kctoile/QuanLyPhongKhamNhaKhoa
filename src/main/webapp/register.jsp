<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Phòng Khám Nha Khoa</title>
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
        .register-wrapper { width: 100%; max-width: 480px; }
        .register-brand {
            text-align: center;
            margin-bottom: var(--space-5);
            color: #fff;
        }
        .register-brand h1 { font-size: 20px; font-weight: 700; color: #fff; }
        .register-brand p { font-size: 13px; opacity: .8; color: #fff; }

        .register-card {
            background: #fff;
            border-radius: var(--radius-xl);
            padding: var(--space-8) var(--space-10);
            box-shadow: 0 24px 64px rgba(0,0,0,.25);
        }
        .register-card h2 {
            font-size: 20px; font-weight: 700;
            margin-bottom: var(--space-5);
            text-align: center;
            color: var(--color-foreground);
        }
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: var(--space-4); }
        .btn-register {
            width: 100%; padding: 13px; justify-content: center;
            font-size: 15px; margin-top: var(--space-2);
            background: var(--color-primary); color: #fff;
            border-radius: var(--radius-sm);
            box-shadow: 0 4px 14px rgba(15,118,110,.35);
        }
        .btn-register:hover { background: var(--color-primary-dark); transform: translateY(-1px); }
        .register-links { text-align: center; margin-top: var(--space-4); font-size: 13px; }
        .register-links a { color: var(--color-primary); font-weight: 500; }
        .back-home {
            display: block; text-align: center; margin-top: var(--space-5);
            color: rgba(255,255,255,.75); font-size: 13px; text-decoration: none;
        }
        .back-home:hover { color: #fff; }
        @media (max-width: 480px) {
            .form-row { grid-template-columns: 1fr; }
            .register-card { padding: var(--space-6); }
        }
    </style>
</head>
<body>
    <div class="register-wrapper">
        <div class="register-brand">
            <h1>🦷 Dental Clinic</h1>
            <p>Tạo tài khoản để đặt lịch khám nhanh chóng</p>
        </div>

        <div class="register-card">
            <h2>Đăng ký tài khoản</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-error" role="alert">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success" role="alert">${message}</div>
            </c:if>

            <form class="auth-form" action="${pageContext.request.contextPath}/RegisterServlet" method="post" novalidate>
                <div class="form-group">
                    <label class="form-label" for="fullName">Họ và tên</label>
                    <input type="text" id="fullName" name="fullName" class="form-control"
                           placeholder="Nguyễn Văn A" required autocomplete="name">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label" for="email">Email</label>
                        <input type="email" id="email" name="email" class="form-control"
                               placeholder="email@example.com" required autocomplete="email">
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="phone">Số điện thoại</label>
                        <input type="tel" id="phone" name="phone" class="form-control"
                               placeholder="0901 234 567" required autocomplete="tel">
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="password">Mật khẩu</label>
                    <input type="password" id="password" name="password" class="form-control"
                           placeholder="Tối thiểu 8 ký tự" required autocomplete="new-password">
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label" for="gender">Giới tính</label>
                        <select id="gender" name="gender" class="form-control">
                            <option value="M">Nam</option>
                            <option value="F">Nữ</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="dob">Ngày sinh</label>
                        <input type="date" id="dob" name="dob" class="form-control" autocomplete="bday">
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label" for="address">Địa chỉ</label>
                    <input type="text" id="address" name="address" class="form-control"
                           placeholder="Số nhà, đường, quận, thành phố" autocomplete="address-line1">
                </div>
                <button type="submit" class="btn btn-register">Tạo tài khoản</button>
            </form>

            <div class="register-links">
                Đã có tài khoản? <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/" class="back-home">← Trở về trang chủ</a>
    </div>
</body>
</html>