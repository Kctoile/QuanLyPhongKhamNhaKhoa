<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
<link rel="stylesheet" href="css/edit_medicine.css"/>
<div class="admin-layout">
    <%@ include file="admin_menu.jsp" %>
    <div class="admin-content">
        <h2>SỬA DỊCH VỤ</h2>
        <a href="services">← Quay lại danh sách</a>
        <hr>
        <form action="services" method="post" style="max-width: 500px;">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="serviceId" value="${fn:escapeXml(param.id)}">
            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Tên dịch vụ *</label>
                <input type="text" name="serviceName" value="${fn:escapeXml(param.name)}" required
                       style="width: 100%; padding: 8px; box-sizing: border-box;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Mô tả</label>
                <textarea name="description" rows="4"
                          style="width: 100%; padding: 8px; box-sizing: border-box;">${fn:escapeXml(param.desc)}</textarea>
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Giá (VNĐ) *</label>
                <input type="number" name="price" value="${fn:escapeXml(param.price)}" required
                       style="width: 100%; padding: 8px; box-sizing: border-box;" min="0" step="0.01">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display:block; font-weight:bold; margin-bottom:5px;">Thời lượng dự kiến (phút)</label>
                <input type="number" name="durationMinutes" value="${fn:escapeXml(param.duration)}"
                       style="width: 100%; padding: 8px; box-sizing: border-box;" min="1" step="1">
            </div>
            <button type="submit"
                    style="padding: 10px 20px; background-color: #008CBA; color: white; border: none; border-radius: 4px; cursor: pointer;">
                Cập nhật
            </button>
        </form>
    </div>
</div>
