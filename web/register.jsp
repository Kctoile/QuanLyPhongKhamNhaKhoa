<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <title>Đăng ký thành viên</title>
    </head>

    <body>
        <h2>Đăng ký tài khoản</h2>
        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
            Họ tên: <input type="text" name="hoTen" required /><br />
            Email: <input type="email" name="email" required /><br />
            Mật khẩu: <input type="password" name="matKhau" required /><br />
            Số điện thoại: <input type="text" name="soDienThoai" required /><br />
            <button type="submit">Đăng ký</button>
        </form>

        <p style="color:red">${error}</p>
        <p style="color:green">${message}</p>
        <p>Đã có tài khoản? <a href="${pageContext.request.contextPath}/login.jsp">Đăng nhập</a></p>
        <p><a href="${pageContext.request.contextPath}/">Về trang chủ</a></p>
    </body>

    </html>