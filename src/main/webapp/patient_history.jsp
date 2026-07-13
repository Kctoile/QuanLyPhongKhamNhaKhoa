<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    // THÊM: kiểm tra quyền DOCTOR (vì trang này chỉ dành cho bác sĩ xem HSBA)
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null
            || !"DOCTOR".equalsIgnoreCase((String) s.getAttribute("role"))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử khám bệnh</title>
    </head>
    <body>
        <h2>Cổng Thông Tin Bác Sĩ</h2>
        <p>Chi tiết hồ sơ bệnh án (HSBA) cũ của Bệnh nhân</p>
        <p>Xin chào, BS. ${sessionScope.user.fullName}</p>
        <p><a href="${pageContext.request.contextPath}/">Trang chủ</a> | <a href="logout">Đăng xuất</a></p>

        <h3>Lịch Sử Khám Bệnh - Hồ Sơ Bệnh Án</h3>
        <p><a href="doctor">← Quay lại lịch khám</a></p>

        <table border="1">
            <tr>
                <th>Ngày khám</th>
                <th>Chẩn đoán / Kết quả</th>
                <th>Đơn thuốc</th>
                <th>Ghi chú Bác sĩ</th>
            </tr>
            <c:forEach var="result" items="${history}">
                <tr>
                    <td>${result.examinationDate}</td>
                    <td>${fn:escapeXml(result.resultDetails)}</td>
                    <td>${fn:escapeXml(result.prescription)}</td>
                    <td>${fn:escapeXml(result.doctorNotes)}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty history}">
                <tr>
                    <td colspan="4">Bệnh nhân này chưa có lịch sử khám bệnh lưu trong hệ thống.</td>
                </tr>
            </c:if>
        </table>
    </body>
</html>
