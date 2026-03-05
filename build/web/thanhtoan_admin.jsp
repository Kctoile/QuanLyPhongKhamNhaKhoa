<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="admin_menu.jsp" %>

<h2>QUẢN LÝ THANH TOÁN</h2>
<a href="admin">← Quay lại Dashboard</a>
<hr>

<table border="1" cellpadding="10">
    <tr>
        <th>Mã TT</th>
        <th>Mã lịch</th>
        <th>Khách hàng</th>
        <th>Dịch vụ</th>
        <th>Tổng tiền (VNĐ)</th>
        <th>Phương thức</th>
        <th>Trạng thái</th>
    </tr>

    <c:forEach var="tt" items="${listThanhToan}">
        <tr>
            <td>${tt.maTT}</td>
            <td>${tt.maLich}</td>
            <td>${tt.tenKhachHang}</td>
            <td>${tt.tenDichVu}</td>
            <td>${tt.tongTien}</td>
            <td>${tt.phuongThuc}</td>
            <td>${tt.trangThai}</td>
        </tr>
    </c:forEach>

    <c:if test="${empty listThanhToan}">
        <tr>
            <td colspan="7">Chưa có giao dịch thanh toán nào.</td>
        </tr>
    </c:if>
</table>
