<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bác sĩ - Khám bệnh</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; margin: 10px 0; }
        th, td { border: 1px solid #333; padding: 8px; }
        th { background: #e0e0e0; }
        .error { color: red; padding: 8px; background: #ffe0e0; margin: 10px 0; }
        .today { background: #e8f5e9; }
        .form-section { margin: 20px 0; padding: 15px; border: 1px solid #ccc; background: #f9f9f9; }
        .thuoc-row { margin: 6px 0; }
    </style>
</head>
<body>

<h2>TRANG BÁC SĨ</h2>
<p>Xin chào: ${sessionScope.user.hoTen}</p>
<a href="${pageContext.request.contextPath}/">Trang chủ</a> | <a href="dichvu">Dịch vụ</a> | <a href="logout">Đăng xuất</a>
<hr>

<c:if test="${param.error == '1'}">
    <div class="error">Lỗi: Vui lòng nhập đầy đủ thông tin kết quả khám.</div>
</c:if>
<c:if test="${param.error == '2'}">
    <div class="error">Lỗi: Lịch hẹn này đã có kết quả khám.</div>
</c:if>

<h3>1. Lịch khám hôm nay</h3>
<c:choose>
    <c:when test="${not empty listLichHenHomNay}">
        <table>
            <tr><th>Mã</th><th>Khách hàng</th><th>Dịch vụ</th><th>Giờ</th><th>Trạng thái</th><th>Hành động</th></tr>
            <c:forEach var="l" items="${listLichHenHomNay}">
                <tr class="today">
                    <td>${l.maLich}</td>
                    <td>${l.tenKhachHang}</td>
                    <td>${l.tenDichVu}</td>
                    <td><fmt:formatDate value="${l.gioKham}" pattern="HH:mm"/></td>
                    <td>${l.trangThai}</td>
                    <td>
                        <c:if test="${l.trangThai == 'Đã vào khám'}">
                            <a href="bacsi?form=1&maLich=${l.maLich}">Ghi kết quả & Kê đơn</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p>Không có lịch khám hôm nay.</p>
    </c:otherwise>
</c:choose>

<h3>2. Tất cả lịch khám của tôi</h3>
<table>
    <tr><th>Mã</th><th>Khách hàng</th><th>Dịch vụ</th><th>Ngày - Giờ</th><th>Trạng thái</th><th>Hành động</th></tr>
    <c:forEach var="l" items="${listLichHen}">
        <tr>
            <td>${l.maLich}</td>
            <td>${l.tenKhachHang}</td>
            <td>${l.tenDichVu}</td>
            <td><fmt:formatDate value="${l.ngayKham}" pattern="dd/MM/yyyy"/> <fmt:formatDate value="${l.gioKham}" pattern="HH:mm"/></td>
            <td>${l.trangThai}</td>
            <td>
                <c:if test="${l.trangThai == 'Đã vào khám'}">
                    <a href="bacsi?form=1&maLich=${l.maLich}">Ghi kết quả & Kê đơn</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
<c:if test="${empty listLichHen}">
    <p>Chưa có lịch hẹn nào.</p>
</c:if>

<c:if test="${param.form == '1' && not empty param.maLich}">
    <hr>
    <div class="form-section">
        <h3>Nhập kết quả khám & Tạo đơn thuốc - Mã lịch: ${param.maLich}</h3>
        <form action="bacsi" method="post">
            <input type="hidden" name="maLich" value="${param.maLich}">
            <p><strong>Kết quả khám:</strong><br>
            <textarea name="ketQua" rows="4" cols="60" required placeholder="Nhập chẩn đoán, kết quả khám..."></textarea></p>
            <p><strong>Hướng dẫn sử dụng thuốc:</strong><br>
            <input type="text" name="huongDan" size="70" placeholder="VD: Uống sau bữa ăn, ngày 3 lần (hoặc để trống nếu không kê thuốc)"></p>
            <p><strong>Kê thuốc:</strong></p>
            <c:forEach var="t" items="${listThuoc}" varStatus="st">
                <div class="thuoc-row">
                    <input type="hidden" name="maThuoc" value="${t.maThuoc}">
                    ${t.tenThuoc} (<fmt:formatNumber value="${t.donGia}" type="number"/> đ/đơn vị):
                    <input type="number" name="soLuong" min="0" value="0" style="width:60px">
                </div>
            </c:forEach>
            <c:if test="${empty listThuoc}">
                <p><em>Chưa có thuốc trong danh mục. Admin cần thêm thuốc trước.</em></p>
            </c:if>
            <p><button type="submit">Hoàn thành khám & Lưu đơn thuốc</button></p>
        </form>
    </div>
</c:if>

</body>
</html>