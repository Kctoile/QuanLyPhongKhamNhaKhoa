<%@ page contentType="text/html;charset=UTF-8" %>

<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>
    <link rel="stylesheet" href="css/admin_dashboard.css">

    <div class="admin-content">

        <h2>ADMIN DASHBOARD</h2>

        <p style="display:flex; align-items:center; gap:12px">
            Xin chào: <strong>${sessionScope.user.fullName}</strong> (ADMIN)
            
        </p>

        <hr>
<div class="dashboard-cards">

    <div class="card">
        <h3>Tổng số User</h3>
        <p>${totalUsers}</p>
    </div>

    <div class="card">
        <h3>Tổng số Doctor</h3>
        <p>${totalDoctors}</p>
    </div>

    <div class="card">
        <h3>Lịch hẹn hôm nay</h3>
        <p>${totalAppointmentsToday}</p>
    </div>

    <div class="card">
        <h3>Tổng số thuốc</h3>
        <p>${totalThuoc}</p>
    </div>

</div>
    </div>

</div>
