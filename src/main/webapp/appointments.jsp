<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lịch Khám Của Tôi - Dental Clinic</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', sans-serif;
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
            .header a {
                color: #4fc3f7;
                text-decoration: none;
                margin-left: 10px;
            }
            .header nav a:hover {
                text-decoration: underline;
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
            }
            tbody td {
                padding: 10px 12px;
                border-bottom: 1px solid #eee;
                font-size: 14px;
            }
            tbody tr:hover {
                background: #f8faff;
            }
            .badge {
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 12px;
                font-weight: 500;
                display: inline-block;
            }
            .badge-pending {
                background: #fff3cd;
                color: #856404;
            }
            .badge-confirmed {
                background: #cce5ff;
                color: #004085;
            }
            .badge-checkedin {
                background: #d4edda;
                color: #155724;
            }
            .badge-completed {
                background: #e8ecf1;
                color: #6c757d;
            }
            .badge-cancelled {
                background: #f8d7da;
                color: #721c24;
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
            .no-data {
                text-align: center;
                padding: 30px;
                color: #999;
            }
            .no-data a {
                color: #007bff;
                text-decoration: none;
            }
            .error {
                background: #f8d7da;
                color: #721c24;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
            }
        </style>
    </head>
    <body>

        <div class="header">
            <h1>Lịch Khám Của Bạn</h1>
            <nav>
                <a href="index.jsp">Trang chủ</a>
                <a href="booking">Đặt Lịch Khám</a>
                <a href="logout">Đăng xuất</a>
            </nav>
        </div>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <div class="card">
            <h3>Lịch hẹn sắp tới</h3>

            <c:choose>
                <c:when test="${empty upcoming}">
                    <div class="no-data">
                        <p>Bạn chưa có lịch hẹn nào sắp tới.</p>
                        <a href="booking">Đặt lịch ngay</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã lịch</th>
                                <th>Bác sĩ</th>
                                <th>Ngày khám</th>
                                <th>Giờ</th>
                                <th>Dịch vụ</th>
                                <th>Phòng</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="app" items="${upcoming}">
                                <tr>
                                    <td>#${app.appointmentId}</td>
                                    <td>${app.doctor != null ? app.doctor.fullName : 'Chưa phân công'}</td>
                                    <td><fmt:formatDate value="${app.appointmentDate}" pattern="dd/MM/yyyy" /></td>
                                    <td><fmt:formatDate value="${app.appointmentTime}" pattern="HH:mm" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty app.services}">
                                                <c:forEach var="sv" items="${app.services}" varStatus="loop">
                                                    ${sv.serviceName}<c:if test="${!loop.last}">, </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>—</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${app.room != null ? app.room : '—'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${app.status == 'Pending'}"><span class="badge badge-pending">Chờ xác nhận</span></c:when>
                                            <c:when test="${app.status == 'CONFIRMED'}"><span class="badge badge-confirmed">Đã xác nhận</span></c:when>
                                            <c:when test="${app.status == 'Checked In'}"><span class="badge badge-checkedin">Checked In</span></c:when>
                                            <c:when test="${app.status == 'Completed'}"><span class="badge badge-completed">Hoàn thành</span></c:when>
                                            <c:when test="${app.status == 'Cancelled'}"><span class="badge badge-cancelled">Đã hủy</span></c:when>
                                            <c:otherwise>${app.status}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${app.status == 'Completed'}">
                                            <a href="appointments?action=view_result&appointmentId=${app.appointmentId}" class="btn-sm btn-primary">Xem bệnh án</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="card">
            <h3>Lịch sử đặt lịch</h3>

            <c:choose>
                <c:when test="${empty history}">
                    <div class="no-data">Chưa có lịch sử đặt lịch.</div>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th>Mã lịch</th>
                                <th>Bác sĩ</th>
                                <th>Ngày - Giờ</th>
                                <th>Dịch vụ</th>
                                <th>Trạng thái</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="app" items="${history}">
                                <tr>
                                    <td>#${app.appointmentId}</td>
                                    <td>${app.doctor != null ? app.doctor.fullName : 'Chưa phân công'}</td>
                                    <td><fmt:formatDate value="${app.appointmentDate}" pattern="dd/MM/yyyy" /> <fmt:formatDate value="${app.appointmentTime}" pattern="HH:mm" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty app.services}">
                                                <c:forEach var="sv" items="${app.services}" varStatus="loop">
                                                    ${sv.serviceName}<c:if test="${!loop.last}">, </c:if>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>—</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${app.status == 'Pending'}"><span class="badge badge-pending">Chờ xác nhận</span></c:when>
                                            <c:when test="${app.status == 'CONFIRMED'}"><span class="badge badge-confirmed">Đã xác nhận</span></c:when>
                                            <c:when test="${app.status == 'Checked In'}"><span class="badge badge-checkedin">Checked In</span></c:when>
                                            <c:when test="${app.status == 'Completed'}"><span class="badge badge-completed">Hoàn thành</span></c:when>
                                            <c:when test="${app.status == 'Cancelled'}"><span class="badge badge-cancelled">Đã hủy</span></c:when>
                                            <c:otherwise>${app.status}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:if test="${app.status == 'Completed'}">
                                            <a href="appointments?action=view_result&appointmentId=${app.appointmentId}" class="btn-sm btn-primary">Xem bệnh án</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

    </body>
</html>
