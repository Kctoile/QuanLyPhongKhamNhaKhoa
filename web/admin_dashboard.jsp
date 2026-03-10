<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="admin_menu.jsp" %>
        <h2>ADMIN DASHBOARD</h2>

        <p style="display:flex; align-items:center; gap:12px">Xin chào: <strong>${sessionScope.user.hoTen}</strong> (ADMIN)
            <a href="logout" style="margin-left:12px;">Đăng xuất</a>
        </p>

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
        <!-- Links are available in the left admin menu; avoid duplicate links here -->