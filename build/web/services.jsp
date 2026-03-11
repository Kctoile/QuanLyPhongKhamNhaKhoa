<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ include file="admin_menu.jsp" %>

            <div style="margin-left: 240px; padding: 20px;">
                <h2>QUẢN LÝ DỊCH VỤ</h2>

                <a href="add_service.jsp"
                    style="display:inline-block; margin-bottom:15px; padding:8px 15px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:4px;">
                    + Thêm Dịch Vụ Mới
                </a>

                <!-- Form Tìm Kiếm -->
                <form action="services" method="get" style="margin-bottom: 20px;">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tên dịch vụ..."
                        style="padding: 8px; width: 250px;">
                    <button type="submit"
                        style="padding: 8px 15px; background-color: #008CBA; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="services" style="margin-left: 10px; text-decoration: none; color: red;">Xóa tìm
                            kiếm</a>
                    </c:if>
                </form>

                <table border="1" cellpadding="10" style="border-collapse: collapse; width: 100%;">
                    <tr style="background-color: #f2f2f2;">
                        <th>ID</th>
                        <th>Tên dịch vụ</th>
                        <th>Mô tả</th>
                        <th>Giá (VNĐ)</th>
                        <th>Thời lượng (phút)</th>
                        <th>Thao tác</th>
                    </tr>

                    <c:forEach var="s" items="${list}">
                        <tr>
                            <td>${s.serviceId}</td>
                            <td>${s.serviceName}</td>
                            <td>${s.description}</td>
                            <td>${s.price}</td>
                            <td>${s.durationMinutes != null ? s.durationMinutes : 'N/A'}</td>
                            <td>
                                <a
                                    href="edit_service.jsp?id=${s.serviceId}&name=${s.serviceName}&desc=${s.description}&price=${s.price}&duration=${s.durationMinutes}">Sửa</a>
                                |
                                <a href="services?action=delete&id=${s.serviceId}"
                                    onclick="return confirm('Bạn có chắc chắn muốn xóa dịch vụ này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>