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
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>In Hóa Đơn - ${appointment.appointmentId}</title>
    </head>
    <body>
        <h1>Dental Clinic Center</h1>
        <h2>HÓA ĐƠN THANH TOÁN</h2>
        <p><strong>Mã hóa đơn:</strong> HD-${appointment.appointmentId}</p>
        <p><strong>Khách hàng:</strong> ${fn:escapeXml(appointment.patient.fullName)}</p>
        <p><strong>Bác sĩ phụ trách:</strong> BS. ${fn:escapeXml(appointment.doctor.fullName)}</p>
        <p><strong>Phòng khám:</strong> ${fn:escapeXml(appointment.room)}</p>

        <table border="1">
            <tr>
                <th>STT</th>
                <th>Loại</th>
                <th>Nội dung chi tiết</th>
                <th>SL</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
            </tr>
            <c:forEach var="item" items="${items}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${fn:escapeXml(item.itemType)}</td>
                    <td>${fn:escapeXml(item.itemName)}</td>
                    <td>${item.quantity}</td>
                    <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="₫"/></td>
                    <td><fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency" currencySymbol="₫"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty items}">
                <tr>
                    <td colspan="6">Không có khoản phí thanh toán nào.</td>
                </tr>
            </c:if>
        </table>

        <p><strong>Phương thức:</strong> ${payment != null ? payment.methodLabel : 'Chưa chọn'}</p>
        <p><strong>Trạng thái:</strong> ${payment != null ? payment.statusLabel : 'Chưa thanh toán'}</p>
        <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫"/></p>
        <hr>
        <p><strong>Khách hàng</strong> (Ký và ghi rõ họ tên)</p>
        <p><strong>Người lập phiếu</strong> (Ký và ghi rõ họ tên)</p>
    </body>
</html>
