<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="admin_menu.jsp" %>

<h2>QUẢN LÝ NGƯỜI DÙNG</h2>

<a href="admin">← Quay lại Dashboard</a>
<hr>

<table border="1" cellpadding="10">
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Họ tên</th>
        <th>Email</th>
        <th>Phone</th>
        <th>Role</th>
    </tr>

    <!-- Hiển thị toàn bộ user -->
    <c:forEach var="u" items="${users}">
        <tr>
            <td>${u.maND}</td>
            <td>${u.hoTen}</td>
            <td>${u.email}</td>
            <td>${u.vaiTro.tenVaiTro}</td>
        </tr>
    </c:forEach>
</table>