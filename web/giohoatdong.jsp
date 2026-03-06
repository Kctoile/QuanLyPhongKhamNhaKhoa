<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Giờ hoạt động - Nha Khoa ABC</title>
        </head>

        <body>

            <h2>GIỜ HOẠT ĐỘNG</h2>
            <a href="${pageContext.request.contextPath}/">Trang chủ</a> |
            <a href="dichvu">Xem dịch vụ & giá</a> | <a href="datlich">Đặt lịch</a> | <c:if
                test="${empty sessionScope.user}"><a href="login.jsp">Đăng nhập</a></c:if>
            <hr>

            <p><strong>Phòng khám nha khoa ABC</strong></p>
            <table border="1" cellpadding="10">
                <tr>
                    <th>Thứ</th>
                    <th>Giờ mở cửa</th>
                </tr>
                <tr>
                    <td>Thứ 2 - Thứ 6</td>
                    <td>8:00 - 20:00</td>
                </tr>
                <tr>
                    <td>Thứ 7</td>
                    <td>8:00 - 17:00</td>
                </tr>
                <tr>
                    <td>Chủ nhật</td>
                    <td>Nghỉ</td>
                </tr>
            </table>

            <p>Hotline: 1900-3333 | Email: nhakhoaabc@gmail.com</p>

        </body>

        </html>