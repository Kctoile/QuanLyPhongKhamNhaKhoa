<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
        Email: <input type="text" name="email" /><br />
        Password: <input type="password" name="matKhau" /><br />
        <button type="submit">Login</button>
    </form>

    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register.jsp">Đăng ký ngay</a></p>
    <p><a href="${pageContext.request.contextPath}/">Về trang chủ</a></p>

    <p style="color:red">${error}</p>