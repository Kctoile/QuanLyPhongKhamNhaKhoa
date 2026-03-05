<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ include file="admin_menu.jsp" %>

            <div style="margin-left: 240px; padding: 20px;">
                <h2>QUẢN LÝ NGƯỜI DÙNG</h2>

                <a href="admin">← Quay lại Dashboard</a>
                <br />
                <a href="nguoidung?action=add"
                    style="display:inline-block; margin-top:20px; margin-bottom:10px; padding:8px 15px; background-color:#4CAF50; color:white; text-decoration:none; border-radius:4px;">+
                    Thêm người dùng</a>

                <div style="margin-bottom: 20px;">
                    <strong>Phân loại: </strong>
                    <a href="nguoidung?role=ALL"
                        style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${empty currentRole || currentRole == 'ALL' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Tất
                        cả</a>
                    <a href="nguoidung?role=ADMIN"
                        style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'ADMIN' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Admin</a>
                    <a href="nguoidung?role=STAFF"
                        style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'STAFF' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Staff</a>
                    <a href="nguoidung?role=CUSTOMER"
                        style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'CUSTOMER' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Customer</a>
                    <a href="nguoidung?role=DOCTOR"
                        style="padding: 5px 10px; margin-right: 5px; text-decoration: none; border-radius: 4px; ${currentRole == 'DOCTOR' ? 'background-color: #007BFF; color: white;' : 'background-color: #e9ecef; color: black;'}">Doctor</a>
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
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td>${u.maND}</td>
                            <td>${u.hoTen}</td>
                            <td>${u.email}</td>
                            <td>${u.soDienThoai}</td>
                            <td>${u.vaiTro.tenVaiTro}</td>
                            <td>
                                <a href="nguoidung?action=edit&amp;id=${u.maND}">Sửa</a> |
                                <a href="nguoidung?action=delete&amp;id=${u.maND}"
                                    onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>