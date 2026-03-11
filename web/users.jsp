<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="css/user.css"/>
<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>

    <div class="admin-content">
        <h2>QUẢN LÝ NGƯỜI DÙNG</h2>
        <c:if test="${not empty param.message}">
            <div
                style="padding:10px; background:#d4edda; color:#155724; border:1px solid #c3e6cb; border-radius:4px; margin-top:10px;">
                ${fn:escapeXml(param.message)}</div>
            </c:if>
            <c:if test="${not empty param.error}">
            <div
                style="padding:10px; background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; border-radius:4px; margin-top:10px;">
                ${fn:escapeXml(param.error)}</div>
            </c:if>

        <a class="back_dashboard" href="admin">← Quay lại Dashboard</a>
        <br />
        <a href="users?action=add"
           style="display:inline-block; margin-top:20px; margin-bottom:10px; padding:8px 15px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:4px;">+
            Thêm người dùng</a>

        <form action="RecomputeDisplayOrder" method="post" style="display:inline-block; margin-left:10px;">
            <button type="submit"
                    style="margin-top:20px; padding:8px 12px; background:#17a2b8; color:#fff; border:none; border-radius:4px; cursor:pointer;">Cập
                nhật STT</button>
        </form>

        <div style="margin-bottom: 20px;">
            <strong>Phân loại: </strong>
            <a href="users?role=ALL"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${empty currentRole || currentRole == 'ALL' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Tất
                cả</a>
            <a href="users?role=ADMIN"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'ADMIN' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Admin</a>
            <a href="users?role=STAFF"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'STAFF' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Staff</a>
            <a href="users?role=CUSTOMER"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'CUSTOMER' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Customer</a>
            <a href="users?role=DOCTOR"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'DOCTOR' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Doctor</a>
            <a href="users?role=UNASSIGNED"
               style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'UNASSIGNED' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Chưa
                gán vai trò</a>
        </div>

        <hr>

        <table border="1" cellpadding="10" style="border-collapse: collapse; width: 100%;">
            <tr style="background-color: #f2f2f2;">
                <th>ID</th>
                <th>Họ tên</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Thao tác</th>
            </tr>

            <!-- Hiển thị toàn bộ user -->
            <c:forEach var="u" items="${users}" varStatus="status">
                <tr>
                    <td>${u.userId}</td>
                    <td>${u.fullName}</td>
                    <td>${u.email}</td>
                    <td>${u.phone}</td>
                    <td>
                        <c:choose>
                            <c:when test="${u.role != null}">${u.role.roleName}</c:when>
                            <c:otherwise>CHƯA GÁN</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="users?action=edit&amp;id=${u.userId}">Sửa</a> |
                        <a href="users?action=delete&amp;id=${u.userId}"
                           onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
                </div>
