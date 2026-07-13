<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
jakarta.servlet.http.HttpSession s = request.getSession(false);
if (s == null || s.getAttribute("role") == null) {
    response.sendRedirect("login.jsp");
    return;
}
if (request.getAttribute("appointment") == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ và Toa thuốc</title>
    </head>
    <body>
        <h2>CHI TIẾT HỒ SƠ BỆNH ÁN</h2>
        <p><strong>Mã lịch hẹn:</strong> #${appointment.appointmentId}</p>
        <p><strong>Ngày khám:</strong> ${appointment.appointmentTime}</p>
        <p><strong>Khách hàng:</strong> ${fn:escapeXml(appointment.patient.fullName)}</p>
        <p><strong>Bác sĩ khám:</strong> BS. ${fn:escapeXml(appointment.doctor.fullName)}</p>

        <h3>1. Kết quả chẩn đoán</h3>
        <p>${fn:escapeXml(examinationResult.resultDetails)}</p>

        <h3>2. Đơn thuốc & Lời dặn</h3>
        <p><a href="${pageContext.request.contextPath}/prescription-print?appointmentId=${appointment.appointmentId}">In đơn thuốc</a></p>
        <p>${fn:escapeXml(examinationResult.prescription)}</p>
        <c:if test="${empty examinationResult.prescription}">
            <p>Không có kê đơn thuốc trong lần khám này.</p>
        </c:if>

        <h3>Ghi chú của bác sĩ</h3>
        <p>${fn:escapeXml(examinationResult.doctorNotes)}</p>
        <c:if test="${empty examinationResult.doctorNotes}">
            <p>Hồ sơ bệnh án chưa được cập nhật chi tiết cho lịch hẹn này.</p>
        </c:if>
    </body>
</html>
