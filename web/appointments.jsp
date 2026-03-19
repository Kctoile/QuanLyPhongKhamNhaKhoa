<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <link rel="stylesheet" href="css/appointments.css" />
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Lịch Khám Của Tôi</title>

                <link rel="stylesheet" href="css/style.css">

            </head>

            <body>

                <div class="page-wrapper">

                    <div class="card">

                        <div class="header">

                            <h2>Lịch Khám Của Bạn</h2>

                            <div class="nav">
                                <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                                <a href="booking">Đặt Lịch Khám</a>
                                <a href="logout">Đăng xuất</a>
                            </div>

                        </div>

                        <hr>

                        <c:choose>

                            <c:when test="${not empty list}">

                                <div class="table-wrapper">

                                    <table class="appointment-table">

                                        <tr>
                                            <th>Mã Lịch</th>
                                            <th>Bác sĩ</th>
                                            <th>Ngày khám</th>
                                            <th>Giờ khám</th>
                                            <th>Dịch vụ</th>
                                            <th>Phòng</th>
                                            <th>Trạng thái</th>
                                        </tr>

                                        <c:forEach var="app" items="${list}">
                                            <tr>

                                                <td>${app.appointmentId}</td>

                                                <td>${app.doctor != null ? app.doctor.fullName : 'Chưa phân công'}</td>

                                                <td>
                                                    <fmt:formatDate value="${app.appointmentDate}" pattern="dd/MM/yyyy" />
                                                </td>

                                                <td>${app.appointmentTime}</td>

                                                <td>
                                                    <c:forEach var="s" items="${app.services}">
                                                        ${s.serviceName}<br/>
                                                    </c:forEach>
                                                </td>

                                                <td>${app.room}</td>

                                                <td style="text-align:center;">
                                                    <span class="status ${app.status}">
                                                        ${app.status}
                                                    </span>
                                                    <c:if test="${app.status == 'Completed'}">
                                                        <br>
                                                        <a href="appointments?action=view_result&appointmentId=${app.appointmentId}" style="display:inline-block; margin-top:8px; font-size:13px; color:#0ea5e9; font-weight:bold; text-decoration:none;">
                                                            📝 Xem Bệnh Án
                                                        </a>
                                                    </c:if>
                                                </td>

                                            </tr>
                                        </c:forEach>

                                    </table>

                                </div>

                            </c:when>

                            <c:otherwise>
                                <p class="no-data">Bạn chưa có lịch hẹn nào.</p>
                            </c:otherwise>

                        </c:choose>

                        <div class="booking-history">
                            <h3>Lịch sử đặt lịch</h3>
                            <table>
                                <tr>
                                    <th>Mã lịch hẹn</th>
                                    <th>Ngày - Giờ</th>
                                    <th>Dịch vụ</th>
                                    <th>Trạng thái</th>
                                </tr>
                                <c:forEach var="app" items="${bookingHistory}">
                                    <tr>
                                        <td>${app.appointmentId}</td>
                                        <td>
                                            <fmt:formatDate value="${app.appointmentDate}" pattern="dd/MM/yyyy" /> ${app.appointmentTime}
                                        </td>
                                        <td>
                                            <c:forEach var="service" items="${app.services}">
                                                ${service.serviceName}<br />
                                            </c:forEach>
                                        </td>
                                        <td style="text-align:center;">
                                            <span class="status ${app.status}">${app.status}</span>
                                            <c:if test="${app.status == 'Completed'}">
                                                <br>
                                                <a href="appointments?action=view_result&appointmentId=${app.appointmentId}" style="display:inline-block; margin-top:8px; font-size:13px; color:#0ea5e9; font-weight:bold; text-decoration:none;">
                                                    📝 Xem Bệnh Án
                                                </a>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                            <c:if test="${empty bookingHistory}">
                                <p>Chưa có lịch sử đặt lịch.</p>
                            </c:if>
                        </div>

                    </div>

                </div>

            </body>

            </html>