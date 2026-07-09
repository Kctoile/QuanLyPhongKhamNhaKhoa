<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null ||
        (!"DOCTOR".equalsIgnoreCase((String) s.getAttribute("role")) &&
         !"ADMIN".equalsIgnoreCase((String) s.getAttribute("role")) &&
         !"STAFF".equalsIgnoreCase((String) s.getAttribute("role")) &&
         !"CUSTOMER".equalsIgnoreCase((String) s.getAttribute("role")))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>In Đơn Thuốc - ${appointment.appointmentId}</title>
        <style>
            body {
                font-family: 'Times New Roman', serif;
                margin: 40px;
            }
            h2 {
                text-align: center;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #000;
                padding: 8px;
                text-align: left;
            }
            th {
                background: #f0f0f0;
            }
            .signature {
                margin-top: 50px;
                text-align: right;
            }
        </style>
    </head>
    <body>
        <h2>ĐƠN THUỐC</h2>
        <p><strong>Mã đơn thuốc:</strong> DT-${appointment.appointmentId}</p>
        <p><strong>Bệnh nhân:</strong> ${fn:escapeXml(appointment.patient.fullName)}</p>
        <p><strong>Giới tính:</strong> ${fn:escapeXml(appointment.patient.gender)}</p>
        <p><strong>Bác sĩ kê đơn:</strong> BS. ${fn:escapeXml(appointment.doctor.fullName)}</p>
        <p><strong>Ngày kê đơn:</strong> <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></p>

        <h3>Chẩn đoán</h3>
        <p>${fn:escapeXml(examinationResult.resultDetails)}</p>

        <h3>Đơn thuốc</h3>
        <table>
            <tr>
                <th>STT</th>
                <th>Tên thuốc</th>
                <th>Số lượng</th>
                <th>Hướng dẫn sử dụng</th>
            </tr>
            <c:forEach var="item" items="${prescriptionDetails}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${fn:escapeXml(item.medicine.medicineName)}</td>
                    <td>${item.prescribedQuantity}</td>
                    <td>${fn:escapeXml(prescription.instructions)}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty prescriptionDetails}">
                <tr>
                    <td colspan="4" style="text-align:center;">Chưa có thông tin kê đơn thuốc chi tiết.</td>
                </tr>
            </c:if>
        </table>

        <h3>Lời dặn của bác sĩ</h3>
        <p>${fn:escapeXml(examinationResult.doctorNotes)}</p>

        <div class="signature">
            <p>Ngày ... tháng ... năm ...</p>
            <p><strong>Bác sĩ điều trị</strong></p>
            <p>(Ký, ghi rõ họ tên)</p>
            <p><strong>BS. ${fn:escapeXml(appointment.doctor.fullName)}</strong></p>
        </div>
    </body>
</html>
