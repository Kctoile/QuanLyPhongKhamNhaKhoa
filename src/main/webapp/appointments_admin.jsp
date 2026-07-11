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

<%@ include file="admin_menu.jsp" %>

<div class="admin-content">
    <h2>QUẢN LÝ LỊCH HẸN (TỔNG HỢP)</h2>

    <table border="1" style="width:100%; border-collapse:collapse; padding: 8px;">
        <tr style="background:#1a2a4a; color:white;">
            <th>STT</th>
            <th>Bệnh nhân</th>
            <th>Bác sĩ</th>
            <th>Ngày hẹn</th>
            <th>Giờ hẹn</th>
            <th>Trạng thái</th>
            <th>Phòng</th>
            <th>Ghi chú</th>
            <th>Hành động</th>
        </tr>
        <c:forEach var="h" items="${appointments}" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>${h.patient != null ? h.patient.fullName : 'Khách vãng lai'}</td>
                <td>${h.doctor != null ? h.doctor.fullName : 'Chưa phân công'}</td>
                <td><fmt:formatDate value="${h.appointmentDate}" pattern="dd/MM/yyyy"/></td>
                <td>${h.appointmentTime}</td>
                <td>${h.status}</td>
                <td>${h.room}</td>
                <td>${h.notes}</td>
                <td>
                    <a href="appointment_admin?action=edit&id=${h.appointmentId}" title="Sửa">Sửa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
