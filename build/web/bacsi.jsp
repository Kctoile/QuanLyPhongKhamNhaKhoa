<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bác sĩ - Khám bệnh</title>
</head>
<body>

<h2>TRANG BÁC SĨ</h2>
<p>Xin chào: ${sessionScope.user.hoTen}</p>
<a href="dichvu">Dịch vụ</a> | <a href="logout">Đăng xuất</a>
<hr>

<h3>Lịch hẹn cần khám (Đã vào phòng)</h3>
<table border="1" cellpadding="8">
    <tr>
        <th>Mã</th>
        <th>Khách hàng</th>
        <th>Dịch vụ</th>
        <th>Ngày - Giờ</th>
        <th>Trạng thái</th>
        <th>Hành động</th>
    </tr>
    <c:forEach var="l" items="${listLichHen}">
        <tr>
            <td>${l.maLich}</td>
            <td>${l.tenKhachHang}</td>
            <td>${l.tenDichVu}</td>
            <td>${l.ngayKham} ${l.gioKham}</td>
            <td>${l.trangThai}</td>
            <td>
                <c:if test="${l.trangThai == 'Đã vào khám'}">
                    <a href="bacsi?form=1&maLich=${l.maLich}">Ghi kết quả & Kê đơn</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

<c:if test="${param.form == '1' && not empty param.maLich}">
<hr>
<h3>Ghi kết quả khám & Kê đơn thuốc - Mã lịch: ${param.maLich}</h3>
<form action="bacsi" method="post">
    <input type="hidden" name="maLich" value="${param.maLich}">
    <label>Kết quả khám:</label><br>
    <textarea name="ketQua" rows="4" cols="50" required></textarea><br><br>
    <label>Hướng dẫn sử dụng thuốc:</label><br>
    <input type="text" name="huongDan" size="60" placeholder="VD: Uống sau bữa ăn, ngày 3 lần"><br><br>

    <label>Thuốc kê đơn:</label><br>
    <c:forEach var="t" items="${listThuoc}" varStatus="st">
        <input type="hidden" name="maThuoc" value="${t.maThuoc}">
        ${t.tenThuoc} (${t.donGia} đ/đơn vị): <input type="number" name="soLuong" min="0" value="0" style="width:60px"> <br>
    </c:forEach>
    <br>
    <button type="submit">Hoàn thành khám & Lưu đơn thuốc</button>
</form>
</c:if>

</body>
</html>
