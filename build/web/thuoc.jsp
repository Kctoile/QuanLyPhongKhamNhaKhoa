<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="admin_menu.jsp" %>

<h2>QUẢN LÝ THUỐC</h2>
<a href="admin">← Quay lại Dashboard</a>
<hr>

<a href="thuoc?action=add">➕ Thêm thuốc</a>

<table border="1" cellpadding="10">
    <tr>
        <th>Mã thuốc</th>
        <th>Tên thuốc</th>
        <th>Đơn giá (VNĐ)</th>
        <th>Số lượng tồn</th>
        <th>Hành động</th>
    </tr>

    <c:forEach var="t" items="${listThuoc}">
        <tr>
            <td>${t.maThuoc}</td>
            <td>${t.tenThuoc}</td>
            <td>${t.donGia}</td>
            <td>${t.soLuongTon}</td>
            <td>
                <a href="thuoc?action=edit&id=${t.maThuoc}">Sửa</a> |
                <a href="thuoc?action=delete&id=${t.maThuoc}"
                   onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>

    <c:if test="${empty listThuoc}">
        <tr>
            <td colspan="5">Không có thuốc nào.</td>
        </tr>
    </c:if>
</table>
