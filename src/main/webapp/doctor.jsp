<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    // Kiểm tra quyền DOCTOR
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || !"DOCTOR".equalsIgnoreCase((String) s.getAttribute("role"))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bác sĩ - Khám bệnh</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f0f4f8;
                color: #333;
            }
            .header {
                background: #1a2a4a;
                color: #fff;
                padding: 15px 30px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .header h1 {
                font-size: 22px;
            }
            .header .subtitle {
                font-size: 13px;
                opacity: 0.8;
            }
            .header .user-info {
                display: flex;
                align-items: center;
                gap: 15px;
            }
            .header .user-info a {
                color: #4fc3f7;
                text-decoration: none;
                font-size: 14px;
            }
            .header .user-info a:hover {
                text-decoration: underline;
            }
            .container {
                max-width: 1200px;
                margin: 20px auto;
                padding: 0 20px;
            }
            .card {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                padding: 20px;
                margin-bottom: 20px;
            }
            .card h3 {
                font-size: 18px;
                color: #1a2a4a;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 2px solid #e8ecf1;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            thead th {
                background: #e8ecf1;
                padding: 10px 12px;
                text-align: left;
                font-size: 13px;
                text-transform: uppercase;
                color: #555;
                position: sticky;
                top: 0;
            }
            tbody td {
                padding: 10px 12px;
                border-bottom: 1px solid #eee;
                font-size: 14px;
            }
            tbody tr:hover {
                background: #f8faff;
            }
            .action-cell {
                display: flex;
                gap: 6px;
                flex-wrap: wrap;
                align-items: center;
            }
            .btn-sm {
                padding: 5px 10px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 12px;
                font-weight: 500;
                display: inline-block;
            }
            .btn-primary {
                background: #007bff;
                color: #fff;
            }
            .btn-primary:hover {
                background: #0056b3;
            }
            .btn-info {
                background: #17a2b8;
                color: #fff;
            }
            .btn-info:hover {
                background: #117a8b;
            }
            .btn-secondary {
                background: #6c757d;
                color: #fff;
            }
            .btn-secondary:hover {
                background: #545b62;
            }
            .badge-warning {
                background: #fff3cd;
                color: #856404;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 12px;
                font-weight: 500;
                display: inline-block;
                border: 1px solid #ffc107;
            }
            .status-label {
                font-weight: 600;
            }
            .status-checkedin {
                color: #28a745;
            }
            .status-completed {
                color: #6c757d;
            }
            .status-pending {
                color: #ffc107;
            }
            .status-cancelled {
                color: #dc3545;
            }
            .error-msg {
                background: #f8d7da;
                color: #721c24;
                padding: 12px 18px;
                border-radius: 4px;
                margin-bottom: 15px;
                border: 1px solid #f5c6cb;
            }
            .no-data {
                text-align: center;
                padding: 40px;
                color: #999;
                font-style: italic;
            }
        </style>
    </head>
    <body>

        <div class="header">
            <div>
                <h1>Cổng Thông Tin Bác Sĩ</h1>
                <div class="subtitle">Quản lý lịch khám & Kê đơn thuốc</div>
            </div>
            <div class="user-info">
                <span>Xin chào, BS. ${sessionScope.fullName}</span>
                <a href="doctor">Trang chủ</a>
                <a href="logout">Đăng xuất</a>
            </div>
        </div>

        <div class="container">

            <c:if test="${not empty errorMsg}">
                <div class="error-msg">${errorMsg}</div>
            </c:if>

            <div class="card">
                <h3>Danh sách lịch hẹn khám của tôi</h3>

                <c:choose>
                    <c:when test="${empty appointments}">
                        <div class="no-data">Chưa có lịch khám nào được phân công cho bạn.</div>
                    </c:when>
                    <c:otherwise>
                        <table>
                            <thead>
                                <tr>
                                    <th>Mã</th>
                                    <th>Bệnh nhân</th>
                                    <th>Dịch vụ</th>
                                    <th>Phòng</th>
                                    <th>Thời gian</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="l" items="${appointments}">
                                    <tr>
                                        <td>#${l.appointmentId}</td>
                                        <td>${l.patient != null ? l.patient.fullName : 'Khách vãng lai'}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty l.services}">
                                                    <c:forEach var="s" items="${l.services}" varStatus="loop">
                                                        ${s.serviceName}<c:if test="${!loop.last}">, </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>—</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${fn:replace(l.room, 'Phòng ', '')}</td>
                                        <td>
                                            <fmt:formatDate value="${l.appointmentDate}" pattern="dd/MM/yyyy" /><br/>
                                            <fmt:formatDate value="${l.appointmentTime}" pattern="HH:mm:ss" />
                                        </td>
                                        <td><span class="status-label status-${fn:toLowerCase(fn:replace(l.status, ' ', ''))}">${l.status}</span></td>
                                        <td>
                                            <div class="action-cell">
                                                <c:choose>
                                                    <c:when test="${l.status == 'Checked In' && !l.canExamine}">
                                                        <span class="badge-warning">Chờ đến giờ</span>
                                                    </c:when>
                                                    <c:when test="${l.canExamine}">
                                                        <a href="doctor?form=1&appointmentId=${l.appointmentId}" class="btn-sm btn-primary">Khám Bệnh</a>
                                                    </c:when>
                                                    <c:when test="${l.status == 'Completed'}">
                                                        <!-- === SỬA: Link Xem KQ qua ViewResultServlet === -->
                                                        <a href="ViewResultServlet?appointmentId=${l.appointmentId}" class="btn-sm btn-secondary">Xem KQ</a>
                                                    </c:when>
                                                </c:choose>
                                                <c:if test="${l.patient != null}">
                                                    <a href="doctor?action=view_history&patientId=${l.patient.userId}" class="btn-sm btn-info">HSBA</a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

        </div>

    </body>
</html>
