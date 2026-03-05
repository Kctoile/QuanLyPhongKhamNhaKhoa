<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="admin_menu.jsp" %>
        <h2>ADMIN DASHBOARD</h2>

        <p>Xin chào: ${sessionScope.user.hoTen} (ADMIN)</p>

        <hr>

        <table border="1" cellpadding="10">
            <tr>
                <td>Tổng số User</td>
                <td>${totalUsers}</td>
            </tr>
            <tr>
                <td>Tổng số Doctor</td>
                <td>${totalDoctors}</td>
            </tr>
            <tr>
                <td>Lịch hẹn hôm nay</td>
                <td>${totalAppointmentsToday}</td>
            </tr>

            <tr>
                <td>Tổng số thuốc</td>
                <td>${totalThuoc}</td>
            </tr>
        </table>

        <hr>

        <a href="nguoidung">Quản lý người dùng</a> |
        <a href="dichvu">Quản lý dịch vụ</a> |
        <a href="thuoc">Quản lý thuốc</a> |
        <a href="lichhen_admin">Quản lý lịch hẹn</a> |
        <a href="logout">Đăng xuất</a>