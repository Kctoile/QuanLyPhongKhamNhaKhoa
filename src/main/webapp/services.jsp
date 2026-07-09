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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ include file="admin_menu.jsp" %>

<div class="admin-content">
    <h2>QUẢN LÝ DỊCH VỤ</h2>
    <a href="add_service.jsp" style="display:inline-block; padding:8px 16px; background:#007bff; color:white; text-decoration:none; border-radius:4px; margin-bottom:15px;">+ Thêm Dịch Vụ Mới</a>

    <table border="1" cellpadding="8" cellspacing="0" style="width:100%; border-collapse:collapse;">
        <tr style="background:#1a2a4a; color:white;">
            <th>ID</th>
            <th>Tên dịch vụ</th>
            <th>Mô tả</th>
            <th>Giá (VNĐ)</th>
            <th>Thời lượng (phút)</th>
            <th>Thao tác</th>
        </tr>
        <c:forEach var="s" items="${services}">
            <tr>
                <td>${s.serviceId}</td>
                <td>${fn:escapeXml(s.serviceName)}</td>
                <td>${fn:escapeXml(s.description)}</td>
                <td><fmt:formatNumber value="${s.price}" type="currency" currencySymbol="" groupingUsed="true"/></td>
                <td>${s.durationMinutes != null ? s.durationMinutes : 'N/A'}</td>
                <td>
                    <a href="edit_service.jsp?id=${s.serviceId}&name=${fn:escapeXml(s.serviceName)}&desc=${fn:escapeXml(s.description)}&price=${s.price}&duration=${s.durationMinutes}">Sửa</a>
                    <a href="services?action=delete&id=${s.serviceId}" onclick="return confirm('Xác nhận xóa?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
