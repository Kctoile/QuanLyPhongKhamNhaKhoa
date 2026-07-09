<%@ page pageEncoding="UTF-8" %>

<link rel="stylesheet" href="css/admin_menu.css">
<div class="admin-sidebar">

    <h3>ADMIN PANEL</h3>

    <div class="menu">
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <a href="admin">Dashboard</a>
        <a href="users">Quản lý người dùng</a>
        <a href="services">Quản lý dịch vụ</a>
        <a href="medicines">Quản lý thuốc</a>
        <a href="appointment_admin">Quản lý lịch hẹn</a>
    </div>

    <a href="logout" class="logout">Đăng xuất</a>

</div>