<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ include file="admin_menu.jsp" %>

            <div style="margin-left: 240px; padding: 20px;">
                <h2>${formAction == 'update' ? 'SỬA NGƯỜI DÙNG' : 'THÊM NGƯỜI DÙNG'}</h2>

                <a href="nguoidung">← Quay lại Danh sách</a>
                <hr>

                <form action="nguoidung" method="post" style="max-width: 500px;">
                    <!-- Hidden fields for action and maND if editing -->
                    <input type="hidden" name="action" value="${formAction}">
                    <c:if test="${formAction == 'update'}">
                        <input type="hidden" name="maND" value="${user.maND}">
                    </c:if>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Họ tên *</label>
                        <input type="text" name="hoTen" value="${user.hoTen}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Email *</label>
                        <input type="email" name="email" value="${user.email}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Mật khẩu *</label>
                        <input type="text" name="matKhau" value="${user.matKhau}" required
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                        <c:if test="${not empty user}">
                            <small style="color: gray;">Sửa mật khẩu nếu cần thay đổi.</small>
                        </c:if>
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Số điện thoại</label>
                        <input type="text" name="soDienThoai" value="${user.soDienThoai}"
                            style="width: 100%; padding: 8px; box-sizing: border-box;">
                    </div>

                    <div style="margin-bottom: 15px;">
                        <label style="display:block; font-weight:bold; margin-bottom:5px;">Vai trò *</label>
                        <select name="maVaiTro" style="width: 100%; padding: 8px; box-sizing: border-box;" required>
                            <option value="1" ${user.vaiTro.maVaiTro==1 ? 'selected' : '' }>ADMIN</option>
                            <option value="2" ${user.vaiTro.maVaiTro==2 ? 'selected' : '' }>DOCTOR</option>
                            <option value="3" ${user.vaiTro.maVaiTro==3 ? 'selected' : '' }>STAFF</option>
                            <option value="4" ${user.vaiTro.maVaiTro==4 ? 'selected' : '' }>CUSTOMER</option>
                        </select>
                    </div>

                    <button type="submit"
                        style="padding: 10px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        Lưu thông tin
                    </button>
                </form>
            </div>