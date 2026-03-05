<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <html>

        <head>
            <title>Danh sách dịch vụ</title>
        </head>

        <body>

            <h2>Danh sách dịch vụ & giá</h2>

            <a href="giohoatdong.jsp">Giờ hoạt động</a> |
            <c:if test="${sessionScope.role eq 'CUSTOMER'}">
                <a href="datlich">Đặt lịch khám</a> |
            </c:if>
            <c:if test="${sessionScope.role eq 'ADMIN'}">
                <a href="admin">Dashboard</a> | <a href="dichvu?action=add">➕ Thêm dịch vụ</a>
            </c:if>

            <table>
                <tr>
                    <th>Mã DV</th>
                    <th>Tên dịch vụ</th>
                    <th>Mô tả</th>
                    <th>Giá (VNĐ)</th>
                    <th>Thời gian (phút)</th>
                    <c:if test="${sessionScope.role eq 'ADMIN'}">
                        <th>Hành động</th>
                    </c:if>
                </tr>

                <c:forEach var="dv" items="${listDichVu}">
                    <tr>
                        <td>${dv.maDV}</td>
                        <td>${dv.tenDV}</td>
                        <td>${dv.moTa}</td>
                        <td>${dv.gia}</td>
                        <td>${dv.thoiGian}</td>

                        <c:if test="${sessionScope.role == 'ADMIN'}">
                            <td>
                                <a href="dichvu?action=edit&id=${dv.maDV}">Sửa</a> |
                                <a href="dichvu?action=delete&id=${dv.maDV}"
                                    onclick="return confirm('Bạn có chắc muốn xóa?');">
                                    Xóa
                                </a>
                            </td>
                        </c:if>
                    </tr>
                </c:forEach>

                <c:if test="${empty listDichVu}">
                    <tr>
                        <td colspan="6">Không có dịch vụ nào.</td>
                    </tr>
                </c:if>
            </table>

        </body>

        </html>