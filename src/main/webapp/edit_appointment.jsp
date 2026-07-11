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
<div class="admin-layout">
    <%@ include file="admin_menu.jsp" %>
    <div style="margin-left: 30px; padding: 20px;" class="appointment-container">
        <h2>SỬA LỊCH HẸN</h2>
        <form action="appointment_admin" method="post" style="max-width: 500px; margin-top: 20px;">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">

            <div style="margin-bottom: 12px;">
                <label for="patientId" style="font-weight: bold; display: block; margin-bottom: 5px;">Khách hàng: </label>
                <select id="patientId" name="patientId" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.userId}" ${c.userId == appointment.patientId ? 'selected' : ''}>${c.fullName}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label for="doctorId" style="font-weight: bold; display: block; margin-bottom: 5px;">Bác sĩ: </label>
                <select id="doctorId" name="doctorId" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <c:forEach var="d" items="${doctors}">
                        <option value="${d.userId}" ${d.userId == appointment.doctorId ? 'selected' : ''}>${d.fullName}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label for="appointmentDate" style="font-weight: bold; display: block; margin-bottom: 5px;">Ngày hẹn: </label>
                <input id="appointmentDate" type="date" name="appointmentDate" value="${appointment.appointmentDate}" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 12px;">
                <label for="appointmentTime" style="font-weight: bold; display: block; margin-bottom: 5px;">Giờ hẹn: </label>
                <input id="appointmentTime" type="time" name="appointmentTime" value="${appointment.appointmentTime}" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 12px;">
                <label for="status" style="font-weight: bold; display: block; margin-bottom: 5px;">Trạng thái: </label>
                <select id="status" name="status" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <option value="Pending" ${appointment.status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="CONFIRMED" ${appointment.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                    <option value="Checked In" ${appointment.status == 'Checked In' ? 'selected' : ''}>Checked In</option>
                    <option value="Checked Out" ${appointment.status == 'Checked Out' ? 'selected' : ''}>Checked Out</option>
                    <option value="Completed" ${appointment.status == 'Completed' ? 'selected' : ''}>Completed</option>
                    <option value="Cancelled" ${appointment.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label for="room" style="font-weight: bold; display: block; margin-bottom: 5px;">Phòng khám: </label>
                <input id="room" type="text" name="room" value="${appointment.room}" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 20px;">
                <label for="notes" style="font-weight: bold; display: block; margin-bottom: 5px;">Ghi chú: </label>
                <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                ...
                <textarea id="notes" name="notes" rows="4" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">${fn:escapeXml(appointment.notes)}</textarea>

            </div>

            <button type="submit" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">Lưu Thay Đổi</button>
            <a href="appointment_admin" style="margin-left: 15px; text-decoration: none; color: #555;">Hủy bỏ</a>
        </form>
    </div>
</div>
