<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null ||
        (!"DOCTOR".equalsIgnoreCase((String) s.getAttribute("role")) &&
         !"ADMIN".equalsIgnoreCase((String) s.getAttribute("role")))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử khám bệnh</title>
        <link rel="stylesheet" href="css/doctor.css">
    </head>
    <body>
        <div class="header">
            <h2>Cổng Thông Tin Bác Sĩ</h2>
            <span>Xin chào, BS. ${sessionScope.user.fullName}</span>
            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
            <a href="logout">Đăng xuất</a>
        </div>

        <div class="container">
            <h3>Lịch Sử Khám Bệnh - Hồ Sơ Bệnh Án</h3>
            <a href="doctor">← Quay lại lịch khám</a>
            <br/><br/>

            <c:choose>
                <c:when test="${not empty examinationResults}">
                    <table border="1" style="width:100%; border-collapse:collapse; padding: 8px;">
                        <tr style="background:#1a2a4a; color:white;">
                            <th>Ngày khám</th>
                            <th>Chẩn đoán / Kết quả</th>
                            <th>Đơn thuốc</th>
                            <th>Ghi chú Bác sĩ</th>
                        </tr>
                        <c:forEach var="result" items="${examinationResults}">
                            <tr>
                                <td><fmt:formatDate value="${result.examinationDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>${fn:escapeXml(result.resultDetails)}</td>
                                <td>
                                    <c:if test="${not empty result.prescription}">
                                        ${fn:escapeXml(result.prescription)}
                                    </c:if>
                                    <c:if test="${empty result.prescription}">
                                        Không kê toa
                                    </c:if>
                                </td>
                                <td>${fn:escapeXml(result.doctorNotes)}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>
                <c:otherwise>
                    <p>Bệnh nhân này chưa có lịch sử khám bệnh lưu trong hệ thống.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
