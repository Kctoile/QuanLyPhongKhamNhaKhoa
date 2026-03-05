<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đặt lịch khám</title>
</head>
<body>

<h2>Đặt lịch khám</h2>
<p>Xin chào: ${sessionScope.user.hoTen}</p>
<a href="dichvu">← Xem dịch vụ</a> | <a href="logout">Đăng xuất</a>
<hr>

<p style="color:red">${error}</p>
<p style="color:green">${sessionScope.successMessage}</p>
<c:if test="${not empty sessionScope.successMessage}">
    <c:remove var="successMessage" scope="session"/>
</c:if>

<form action="datlich" method="post">
    <label>Chọn bác sĩ:</label><br>
    <select name="maBacSi" required>
        <option value="">-- Chọn bác sĩ --</option>
        <c:forEach var="bs" items="${doctors}">
            <option value="${bs.maND}">${bs.hoTen}</option>
        </c:forEach>
    </select><br><br>

    <label>Ngày khám:</label><br>
    <input type="date" name="ngayKham" required><br><br>

    <label>Giờ khám:</label><br>
    <select name="gioKham" required>
        <option value="">-- Chọn giờ --</option>
        <option value="08:00">08:00</option>
        <option value="08:30">08:30</option>
        <option value="09:00">09:00</option>
        <option value="09:30">09:30</option>
        <option value="10:00">10:00</option>
        <option value="10:30">10:30</option>
        <option value="11:00">11:00</option>
        <option value="11:30">11:30</option>
        <option value="13:00">13:00</option>
        <option value="13:30">13:30</option>
        <option value="14:00">14:00</option>
        <option value="14:30">14:30</option>
        <option value="15:00">15:00</option>
        <option value="15:30">15:30</option>
        <option value="16:00">16:00</option>
        <option value="16:30">16:30</option>
        <option value="17:00">17:00</option>
        <option value="17:30">17:30</option>
        <option value="18:00">18:00</option>
        <option value="18:30">18:30</option>
        <option value="19:00">19:00</option>
        <option value="19:30">19:30</option>
        <option value="20:00">20:00</option>
    </select><br><br>

    <label>Dịch vụ:</label><br>
    <select name="maDV" required>
        <option value="">-- Chọn dịch vụ --</option>
        <c:forEach var="dv" items="${services}">
            <option value="${dv.maDV}">${dv.tenDV} - ${dv.gia} VNĐ</option>
        </c:forEach>
    </select><br><br>

    <label>Ghi chú:</label><br>
    <textarea name="ghiChu" rows="3" cols="40"></textarea><br><br>

    <button type="submit">Đặt lịch</button>
</form>

</body>
</html>
