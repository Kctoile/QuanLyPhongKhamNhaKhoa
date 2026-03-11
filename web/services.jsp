<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="css/services.css"/>
<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>

    <div class="admin-content">

        <div class="service-page">

            <div class="service-container">
                <h2 class="page-title">QUẢN LÝ DỊCH VỤ</h2>

                <a href="add_service.jsp"
                   class="btn-add">
                    + Thêm Dịch Vụ Mới
                </a>

                <!-- Form Tìm Kiếm -->
                <form action="services" method="get" class="search-form">
                    <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tên dịch vụ..."
                           class="search-input">

                    <button type="submit" class="btn-search">
                        Tìm kiếm
                    </button>

                    <c:if test="${not empty keyword}">
                        <a href="services" class="clear-search">
                            Xóa tìm kiếm
                        </a>
                    </c:if>
                </form>

                <table class="service-table">
                    <tr>
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
                            <td class="action-buttons">

                                <a
                                    href="edit_service.jsp?id=${s.serviceId}&name=${s.serviceName}&desc=${s.description}&price=${s.price}&duration=${s.durationMinutes}"
                                    class="btn-edit">Sửa</a>

                                <a href="services?action=delete&id=${s.serviceId}"
                                   class="btn-delete"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa dịch vụ này?');">Xóa</a>

                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>

    </div>

</div>