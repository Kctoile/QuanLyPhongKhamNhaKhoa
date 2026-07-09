<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!"ADMIN".equalsIgnoreCase((String) s.getAttribute("role"))) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <style>
            body {
                font-family: 'Segoe UI', sans-serif;
                background: #f0f4f8;
                padding: 20px;
                margin: 0;
            }
            .header {
                background: #1a2a4a;
                color: #fff;
                padding: 15px 30px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .header a {
                color: #4fc3f7;
                text-decoration: none;
            }
            .menu {
                background: #fff;
                border-radius: 8px;
                padding: 15px 20px;
                margin: 20px 0;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
            }
            .menu a {
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-size: 14px;
                font-weight: 500;
            }
            .menu a.active {
                background: #1a2a4a;
                color: #fff;
            }
            .menu a:not(.active) {
                background: #e8ecf1;
                color: #333;
            }
            .menu a:not(.active):hover {
                background: #d0d5dc;
            }
            .stats {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 20px;
            }
            .stat-card {
                background: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                text-align: center;
            }
            .stat-card .number {
                font-size: 36px;
                font-weight: bold;
                color: #1a2a4a;
            }
            .stat-card .label {
                font-size: 14px;
                color: #666;
                margin-top: 5px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>ADMIN DASHBOARD</h1>
            <div>
                <span>Xin chào: <strong>${sessionScope.fullName}</strong> (ADMIN)</span>
                <a href="logout" style="margin-left: 15px;">Đăng xuất</a>
            </div>
        </div>

        <div class="menu">
            <a href="admin" class="active">Dashboard</a>
            <a href="users">Quản lý User</a>
            <a href="services">Dịch vụ</a>
            <a href="medicines">Thuốc</a>
            <a href="appointments_admin">Lịch hẹn</a>
            <a href="payment">Thanh toán</a>
        </div>

        <div class="stats">
            <div class="stat-card">
                <div class="number">${totalUsers}</div>
                <div class="label">Tổng số User</div>
            </div>
            <div class="stat-card">
                <div class="number">${totalDoctors}</div>
                <div class="label">Tổng số Doctor</div>
            </div>
            <div class="stat-card">
                <div class="number">${totalAppointmentsToday}</div>
                <div class="label">Lịch hẹn hôm nay</div>
            </div>
            <div class="stat-card">
                <div class="number">${totalThuoc}</div>
                <div class="label">Tổng số thuốc</div>
            </div>
        </div>
    </body>
</html>
