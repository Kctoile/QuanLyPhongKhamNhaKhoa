<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null ||
        (!"ADMIN".equalsIgnoreCase((String) s.getAttribute("role")) &&
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
        <title>In Hóa Đơn - ${appointment.appointmentId}</title>
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
            .total {
                text-align: right;
                font-weight: bold;
                margin-top: 20px;
                font-size: 18px;
            }
            .signature {
                margin-top: 50px;
                display: flex;
                justify-content: space-between;
            }
        </style>
    </head>
    <body>
        <h2>HÓA ĐƠN THANH TOÁN</h2>
        <p><strong>Mã hóa đơn:</strong> HD-${appointment.appointmentId}</p>
        <p><strong>Ngày in:</strong> <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></p>
        <p><strong>Khách hàng:</strong> ${fn:escapeXml(appointment.patient.fullName)}</p>
        <p><strong>Bác sĩ phụ trách:</strong> BS. ${fn:escapeXml(appointment.doctor.fullName)}</p>
        <p><strong>Phòng khám:</strong> ${fn:escapeXml(appointment.room)}</p>

        <table>
            <tr>
                <th>STT</th>
                <th>Loại</th>
                <th>Nội dung</th>
                <th>Số lượng</th>
                <th>Đơn giá</th>
                <th>Thành tiền</th>
            </tr>
            <c:forEach var="item" items="${paymentItems}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${fn:escapeXml(item.itemType)}</td>
                    <td>${fn:escapeXml(item.itemName)}</td>
                    <td>${item.quantity}</td>
                    <td><fmt:formatNumber value="${item.unitPrice}" groupingUsed="true"/> ₫</td>
                    <td><fmt:formatNumber value="${item.unitPrice * item.quantity}" groupingUsed="true"/> ₫</td>
                </tr>
            </c:forEach>
            <c:if test="${empty paymentItems}">
                <tr>
                    <td colspan="6" style="text-align:center;">Không có khoản phí thanh toán nào.</td>
                </tr>
            </c:if>
        </table>

        <p><strong>Phương thức:</strong> ${payment != null ? payment.methodLabel : 'Chưa chọn'}</p>
        <p><strong>Trạng thái:</strong> ${payment != null ? payment.statusLabel : 'Chưa thanh toán'}</p>

        <div class="total">
            Tổng tiền: <fmt:formatNumber value="${payment != null ? payment.totalAmount : 0}" groupingUsed="true"/> ₫
        </div>

        <div class="signature">
            <div>
                <p><strong>Khách hàng</strong></p>
                <p>(Ký và ghi rõ họ tên)</p>
            </div>
            <div>
                <p><strong>Người lập phiếu</strong></p>
                <p>(Ký và ghi rõ họ tên)</p>
            </div>
        </div>
    </body>
</html>
