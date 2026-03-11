<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Lịch Khám Của Tôi</title>
            </head>

            <body>

                <h2>Lịch Khám Của Xin chào, ${sessionScope.user.fullName}</h2>
                <a href="${pageContext.request.contextPath}/">Trang chủ</a> | <a href="booking">Đặt Lịch Khám</a> | <a
                    href="logout">Đăng xuất</a>
                <hr>

                <c:choose>
                    <c:when test="${not empty list}">
                        <table border="1" cellpadding="8" style="border-collapse: collapse;">
                            <tr style="background:#f0f0f0;">
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
                                            ${s.serviceName}<br />
                                        </c:forEach>
                                    </td>
                                    <td>${app.room}</td>
                                    <td>
                                        <span
                                            style="font-weight:bold; color: ${app.status == 'Pending' ? 'orange' : (app.status == 'Completed' ? 'green' : 'black')}">
                                            ${app.status}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p>Bạn chưa có lịch hẹn nào.</p>
                    </c:otherwise>
                </c:choose>

            </body>

            </html>