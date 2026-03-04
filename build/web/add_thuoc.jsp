<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="admin_menu.jsp" %>

<h2>Thêm thuốc mới</h2>
<a href="admin">← Quay lại Dashboard</a> | <a href="thuoc">← Danh sách thuốc</a>
<hr>

<form action="thuoc" method="post">
    <input type="hidden" name="action" value="insert">

    <label>Tên thuốc:</label><br>
    <input type="text" name="tenThuoc" required/><br><br>

    <label>Đơn giá (VNĐ):</label><br>
    <input type="number" name="donGia" step="0.01" min="0" required/><br><br>

    <label>Số lượng tồn:</label><br>
    <input type="number" name="soLuongTon" value="0" min="0" required/><br><br>

    <button type="submit">Lưu</button>
</form>
