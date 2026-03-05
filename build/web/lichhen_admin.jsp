<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="admin_menu.jsp" %>

<h2>TOÀN BỘ LỊCH HẸN HỆ THỐNG</h2>

<a href="admin">← Quay lại Dashboard</a>
<hr>

<table border="1" cellpadding="10">
    <tr>
        <th>ID</th>
        <th>Khách hàng</th>
        <th>Bác sĩ</th>
        <th>Ngày hẹn</th>
        <th>Trạng thái</th>
    </tr>

    <c:forEach var="l" items="${list}">
        <tr>
            <td>${l.maLich}</td>
            <td>${l.tenKhachHang}</td>
            <td>${l.tenBacSi}</td>
            <td>${l.ngayKham} ${l.gioKham}</td>
            <td>${l.trangThai}</td>
        </tr>
    </c:forEach>
</table>