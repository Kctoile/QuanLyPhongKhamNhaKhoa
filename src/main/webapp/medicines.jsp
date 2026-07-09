<%@ page contentType="text/html;charset=UTF-8" %>
<%
// === KIỂM TRA QUYỀN ADMIN ===
jakarta.servlet.http.HttpSession s = request.getSession(false);
if (s == null || s.getAttribute("role") == null) {
    response.sendRedirect("login.jsp");
    return;
}
if (!"ADMIN".equalsIgnoreCase((String) s.getAttribute("role"))) {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
    return;
}
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="admin_menu.jsp" %>

<div class="admin-content">
    <h2>QUẢN LÝ THUỐC</h2>
    <a href="add_medicine.jsp" style="display:inline-block; padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:4px; margin-bottom:15px;">+ Thêm Thuốc Mới</a>

    <table border="1" cellpadding="8" cellspacing="0" style="width:100%; border-collapse:collapse;">
        <tr style="background:#1a2a4a; color:white;">
            <th>ID</th>
            <th>Tên thuốc</th>
            <th>Giá (VNĐ)</th>
            <th>Số lượng tồn kho</th>
            <th>Thao tác</th>
        </tr>
        <c:forEach var="m" items="${medicines}">
            <tr>
                <td>${m.medicineId}</td>
                <td>${fn:escapeXml(m.medicineName)}</td>
                <td><fmt:formatNumber value="${m.price}" type="currency" currencySymbol="" groupingUsed="true"/></td>
            <td>${m.stockQuantity}</td>
            <td>
                <a href="edit_medicine.jsp?id=${m.medicineId}&name=${fn:escapeXml(m.medicineName)}&price=${m.price}&stock=${m.stockQuantity}">Sửa</a>
                <a href="medicines?action=delete&id=${m.medicineId}" onclick="return confirm('Xác nhận xóa?')">Xóa</a>
            </td>
            </tr>
        </c:forEach>
    </table>
</div>
