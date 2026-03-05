<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán - Nhân viên</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
        th { background: #f4f4f4; }
        .btn { padding: 6px 12px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; border: none; cursor: pointer; }
        .btn:hover { background: #0056b3; }
        .form-inline { display: inline; }
        h2 { color: #333; }
        .back { margin-bottom: 15px; }
    </style>
</head>
<body>

<h2>THANH TOÁN - TRANG NHÂN VIÊN</h2>
<div class="back">
    <a href="dichvu">← Xem dịch vụ</a> | 
    <c:if test="${sessionScope.role == 'ADMIN'}">
        <a href="admin">← Dashboard Admin</a> |
    </c:if>
    <a href="logout">Đăng xuất</a>
</div>
<hr>

<p>Xin chào: <b>${sessionScope.user.hoTen}</b> (${sessionScope.role})</p>

<h3>Lịch hẹn chưa thanh toán</h3>

<table>
    <tr>
        <th>Mã lịch</th>
        <th>Khách hàng</th>
        <th>Bác sĩ</th>
        <th>Dịch vụ</th>
        <th>Ngày</th>
        <th>Giờ</th>
        <th>Giá (VNĐ)</th>
        <th>Hành động</th>
    </tr>

    <c:forEach var="lh" items="${listLichHen}">
        <tr>
            <td>${lh.maLich}</td>
            <td>${lh.tenKhachHang}</td>
            <td>${lh.tenBacSi}</td>
            <td>${lh.tenDV}</td>
            <td>${lh.ngayKham}</td>
            <td>${lh.gioKham}</td>
            <td>${lh.gia}</td>
            <td>
                <form action="thanhtoan" method="post" class="form-inline">
                    <input type="hidden" name="maLich" value="${lh.maLich}">
                    <input type="number" name="tongTien" value="${lh.gia}" min="0" step="1000" style="width:100px" required>
                    <select name="phuongThuc" required>
                        <option value="Tiền mặt">Tiền mặt</option>
                        <option value="Chuyển khoản">Chuyển khoản</option>
                        <option value="Thẻ">Thẻ</option>
                        <option value="VNPay">VNPay</option>
                    </select>
                    <button type="submit" class="btn">Thanh toán</button>
                </form>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty listLichHen}">
        <tr>
            <td colspan="8">Không có lịch hẹn nào cần thanh toán.</td>
        </tr>
    </c:if>
</table>

</body>
</html>
