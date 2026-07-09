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
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Kết quả khám bệnh</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f0f4f8;
                padding: 20px;
            }
            .header {
                background: #1a2a4a;
                color: #fff;
                padding: 15px 30px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .header h1 {
                font-size: 20px;
            }
            .header a {
                color: #4fc3f7;
                text-decoration: none;
                margin-left: 15px;
            }
            .card {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                padding: 20px;
                margin-bottom: 20px;
            }
            .card h3 {
                color: #1a2a4a;
                border-bottom: 2px solid #e8ecf1;
                padding-bottom: 10px;
                margin-bottom: 15px;
            }
            .info-row {
                display: flex;
                margin-bottom: 8px;
            }
            .info-label {
                font-weight: 600;
                min-width: 140px;
                color: #555;
            }
            .info-value {
                color: #333;
            }
            .content-box {
                background: #f9fafc;
                border: 1px solid #e8ecf1;
                border-radius: 6px;
                padding: 15px;
                margin-top: 10px;
                white-space: pre-wrap;
            }
            .btn-print {
                display: inline-block;
                padding: 10px 20px;
                background: #1a2a4a;
                color: #fff;
                border-radius: 6px;
                text-decoration: none;
                margin-top: 10px;
            }
            .btn-print:hover {
                background: #2c3e6b;
            }
            .no-data {
                color: #888;
                font-style: italic;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>CHI TIẾT HỒ SƠ BỆNH ÁN</h1>
            <div>
                <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                <a href="appointments">Quản lý Lịch hẹn</a>
                <a href="doctor">Trang bác sĩ</a>
                <a href="logout">Đăng xuất</a>
            </div>
        </div>

        <div class="card">
            <h3>Thông tin lịch hẹn</h3>
            <div class="info-row"><span class="info-label">Mã lịch hẹn:</span><span class="info-value">#${appointment.appointmentId}</span></div>
            <div class="info-row"><span class="info-label">Ngày khám:</span><span class="info-value"><fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/> - ${appointment.appointmentTime}</span></div>
            <div class="info-row"><span class="info-label">Khách hàng:</span><span class="info-value">${fn:escapeXml(appointment.patient.fullName)}</span></div>
            <div class="info-row"><span class="info-label">Bác sĩ khám:</span><span class="info-value">BS. ${fn:escapeXml(appointment.doctor.fullName)}</span></div>
        </div>

        <div class="card">
            <h3>1. Kết quả chẩn đoán</h3>
            <c:if test="${not empty examinationResult.resultDetails}">
                <div class="content-box">${fn:escapeXml(examinationResult.resultDetails)}</div>
            </c:if>
            <c:if test="${empty examinationResult.resultDetails}">
                <p class="no-data">Chưa có kết quả chẩn đoán.</p>
            </c:if>
        </div>

        <div class="card">
            <h3>2. Đơn thuốc & Lời dặn</h3>
            <a class="btn-print" href="${pageContext.request.contextPath}/prescription-print?appointmentId=${appointment.appointmentId}">In đơn thuốc</a>
            <br/><br/>
            <c:if test="${not empty examinationResult.prescription}">
                <div class="content-box">${fn:escapeXml(examinationResult.prescription)}</div>
            </c:if>
            <c:if test="${empty examinationResult.prescription}">
                <p class="no-data">Không có kê đơn thuốc trong lần khám này.</p>
            </c:if>
        </div>

        <div class="card">
            <h3>Ghi chú của bác sĩ</h3>
            <c:if test="${not empty examinationResult.doctorNotes}">
                <div class="content-box">${fn:escapeXml(examinationResult.doctorNotes)}</div>
            </c:if>
            <c:if test="${empty examinationResult.doctorNotes}">
                <p class="no-data">Hồ sơ bệnh án chưa được cập nhật chi tiết cho lịch hẹn này.</p>
            </c:if>
        </div>
    </body>
</html>
