<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    // THÊM: kiểm tra đăng nhập
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>In Đơn Thuốc - ${appointment.appointmentId}</title>
    </head>
    <body>
        <h1>Dental Clinic Center</h1>
        <h2>ĐƠN THUỐC</h2>
        <p><strong>Bệnh nhân:</strong> ${fn:escapeXml(appointment.patient.fullName)}</p>
        <p><strong>Giới tính:</strong> ${fn:escapeXml(appointment.patient.gender)}</p>
        <p><strong>Mã đơn thuốc:</strong> DT-${appointment.appointmentId}</p>
        <p><strong>Ngày kê đơn:</strong></p>
        <p><strong>Bác sĩ kê đơn:</strong> BS. ${fn:escapeXml(appointment.doctor.fullName)}</p>
        <p><strong>Chẩn đoán bệnh:</strong></p>
        <p>${fn:escapeXml(examinationResult.resultDetails)}</p>

        <h3>Chỉ định dùng thuốc</h3>
        <table border="1">
            <tr>
                <th>STT</th>
                <th>Tên thuốc</th>
                <th>Số lượng</th>
                <th>Hướng dẫn sử dụng</th>
            </tr>
            <c:forEach var="item" items="${details}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${fn:escapeXml(item.medicine.medicineName)}</td>
                    <td>${item.prescribedQuantity}</td>
                    <td>${fn:escapeXml(prescription.instructions)}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty details}">
                <tr>
                    <td colspan="4">Chưa có thông tin kê đơn thuốc chi tiết.</td>
                </tr>
            </c:if>
        </table>

        <p><strong>Lời dặn bác sĩ:</strong></p>
        <p>${fn:escapeXml(examinationResult.doctorNotes)}</p>
        <p>Ngày ___ tháng ___ năm ___</p>
        <p><strong>Bác sĩ điều trị</strong></p>
        <p>(Ký, ghi rõ họ tên)</p>
        <p><strong>BS. ${fn:escapeXml(appointment.doctor.fullName)}</strong></p>
    </body>
</html>
