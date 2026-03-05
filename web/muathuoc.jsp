<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <title>Mua thuốc theo đơn</title>
            </head>

            <body>

                <h2>MUA THUỐC THEO ĐƠN</h2>
                <p>Xin chào: ${sessionScope.user.hoTen}</p>
                <a href="datlich">Đặt lịch</a> | <a href="dichvu">Dịch vụ</a> | <a href="logout">Đăng xuất</a>
                <hr>

                <c:if test="${empty donThuocList}">
                    <p>Bạn chưa có đơn thuốc nào. Vui lòng khám bệnh để bác sĩ kê đơn.</p>
                </c:if>

                <c:forEach var="don" items="${donThuocList}">
                    <div style="border:1px solid #ccc; padding:15px; margin:10px 0;">
                        <h4>Đơn thuốc #${don.maDon} - Lịch ${don.maLich}</h4>
                        <p>Hướng dẫn: ${don.huongDan}</p>

                        <table border="1" cellpadding="8">
                            <tr>
                                <th>Thuốc</th>
                                <th>Đơn giá</th>
                                <th>Số lượng mua</th>
                            </tr>
                            <c:forEach var="ct" items="${don.chiTiet}">
                                <tr>
                                    <td>${ct.tenThuoc}</td>
                                    <td>
                                        <fmt:formatNumber value="${ct.donGia}" type="number" /> đ
                                    </td>
                                    <td>
                                        ${ct.soLuong}
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>

                    </div>
                </c:forEach>

            </body>

            </html>