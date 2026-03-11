<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>
    <link rel="stylesheet" href="css/user_form.css">

    <div class="admin-content">

    <h2>${formAction == 'update' ? 'SỬA NGƯỜI DÙNG' : 'THÊM NGƯỜI DÙNG'}</h2>

    <a href="users">← Quay lại Danh sách</a>
    <hr>

    <form action="users" method="post" style="max-width: 500px;">
                    <input type="hidden" name="action" value="${formAction}">
                    <c:if test="${formAction == 'update'}">
                        <input type="hidden" name="userId" value="${user.userId}">
                    </c:if>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Họ tên *</label>
                        <input type="text" name="fullName" value="${user.fullName}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Email *</label>
                        <input type="email" name="email" value="${user.email}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Mật khẩu *</label>
                        <input type="text" name="password" value="${user.password}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                        <c:if test="${not empty user}">
                            <small style="color: gray;">Sửa mật khẩu nếu cần thay đổi.</small>
                        </c:if>
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Số điện thoại</label>
                        <input type="text" name="phone" value="${user.phone}"
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Vai trò *</label>
                        <select name="roleId" style="width: 100%; padding: 8px; box-sizing: border-box;" required>
                            <option value="1" ${user.role !=null && user.role.roleId==1 ? 'selected' : '' }>ADMIN
                            </option>
                            <option value="2" ${user.role !=null && user.role.roleId==2 ? 'selected' : '' }>DOCTOR
                            </option>
                            <option value="3" ${user.role !=null && user.role.roleId==3 ? 'selected' : '' }>STAFF
                            </option>
                            <option value="4" ${user.role !=null && user.role.roleId==4 ? 'selected' : '' }>CUSTOMER
                            </option>
                        </select>
                    </div>

                    <button type="submit"
                        style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        Lưu thông tin
                    </button>
                </form>
            </div>
  </div>