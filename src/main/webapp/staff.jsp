<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dentalclinic.model.User"%>
<%@page import="com.dentalclinic.model.Appointment"%>
<%@page import="com.dentalclinic.dao.AppointmentDAO"%>
<%@page import="java.util.List"%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"STAFF".equalsIgnoreCase((String) session.getAttribute("role"))) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    AppointmentDAO appointmentDAO = new AppointmentDAO();
    List<Appointment> todayAppointments = appointmentDAO.getAppointmentsByDate(new java.sql.Date(System.currentTimeMillis()));
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Lễ tân - Dental Clinic</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            body {
                background-color: #f0f4f8;
                min-height: 100vh;
            }
            .header {
                background: linear-gradient(135deg, #1a73e8, #0d47a1);
                color: white;
                padding: 16px 32px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                box-shadow: 0 2px 8px rgba(0,0,0,0.15);
            }
            .header h1 {
                font-size: 22px;
                font-weight: 600;
            }
            .header .user-info {
                display: flex;
                align-items: center;
                gap: 20px;
                font-size: 14px;
            }
            .header .user-info a {
                color: white;
                text-decoration: none;
                padding: 6px 16px;
                border: 1px solid rgba(255,255,255,0.4);
                border-radius: 6px;
                transition: 0.2s;
                font-size: 13px;
            }
            .header .user-info a:hover {
                background: rgba(255,255,255,0.15);
            }
            .container {
                max-width: 1200px;
                margin: 24px auto;
                padding: 0 20px;
            }
            .stats {
                display: flex;
                gap: 16px;
                margin-bottom: 24px;
                flex-wrap: wrap;
            }
            .stat-card {
                flex: 1;
                min-width: 160px;
                background: white;
                border-radius: 10px;
                padding: 18px 20px;
                box-shadow: 0 1px 4px rgba(0,0,0,0.08);
            }
            .stat-card .number {
                font-size: 28px;
                font-weight: 700;
                color: #1a73e8;
            }
            .stat-card .label {
                font-size: 13px;
                color: #666;
                margin-top: 4px;
            }
            .toolbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 16px;
                flex-wrap: wrap;
                gap: 12px;
            }
            .toolbar h2 {
                font-size: 18px;
                color: #333;
            }
            .toolbar .actions {
                display: flex;
                gap: 10px;
            }
            .toolbar .actions a, .toolbar .actions button {
                padding: 8px 18px;
                border-radius: 6px;
                font-size: 13px;
                text-decoration: none;
                cursor: pointer;
                border: none;
                font-weight: 500;
            }
            .btn-primary {
                background: #1a73e8;
                color: white;
            }
            .btn-primary:hover {
                background: #1557b0;
            }
            .btn-outline {
                background: white;
                color: #1a73e8;
                border: 1px solid #1a73e8;
            }
            .btn-outline:hover {
                background: #e8f0fe;
            }
            .table-wrapper {
                background: white;
                border-radius: 10px;
                box-shadow: 0 1px 4px rgba(0,0,0,0.08);
                overflow-x: auto;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                min-width: 800px;
            }
            th {
                background: #f8fafc;
                padding: 12px 14px;
                font-size: 12px;
                font-weight: 600;
                text-transform: uppercase;
                color: #555;
                text-align: left;
                border-bottom: 2px solid #e2e8f0;
            }
            td {
                padding: 12px 14px;
                font-size: 14px;
                border-bottom: 1px solid #f0f0f0;
                color: #333;
            }
            tr:hover {
                background: #f8faff;
            }
            .status-badge {
                display: inline-block;
                padding: 3px 10px;
                border-radius: 12px;
                font-size: 12px;
                font-weight: 500;
            }
            .status-Pending {
                background: #fff3cd;
                color: #856404;
            }
            .status-CONFIRMED {
                background: #cce5ff;
                color: #004085;
            }
            .status-Checked-In {
                background: #d4edda;
                color: #155724;
            }
            .status-Completed {
                background: #e2e3e5;
                color: #383d41;
            }
            .status-Cancelled {
                background: #f8d7da;
                color: #721c24;
            }
            .action-btn {
                padding: 4px 12px;
                border-radius: 5px;
                font-size: 12px;
                cursor: pointer;
                border: none;
                font-weight: 500;
                text-decoration: none;
                display: inline-block;
                margin-right: 4px;
            }
            .btn-checkin {
                background: #28a745;
                color: white;
            }
            .btn-checkin:hover {
                background: #218838;
            }
            .btn-cancel {
                background: #dc3545;
                color: white;
            }
            .btn-cancel:hover {
                background: #c82333;
            }
            .btn-view {
                background: #6c757d;
                color: white;
            }
            .btn-view:hover {
                background: #5a6268;
            }
            .btn-edit {
                background: #ffc107;
                color: #333;
            }
            .btn-edit:hover {
                background: #e0a800;
            }
            .btn-disabled {
                background: #e2e3e5;
                color: #999;
                cursor: not-allowed;
            }
            .empty {
                text-align: center;
                padding: 40px;
                color: #999;
                font-size: 15px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>Dental Clinic — Lễ tân</h1>
            <div class="user-info">
                <span>Xin chào, <%= user.getFullName() != null ? user.getFullName().toUpperCase() : "STAFF" %></span>
                <a href="<%= request.getContextPath() %>/LogoutServlet">Đăng xuất</a>
            </div>
        </div>
        <div class="container">
            <div class="stats">
                <div class="stat-card">
                    <div class="number"><%= todayAppointments != null ? todayAppointments.size() : 0 %></div>
                    <div class="label">Lịch hẹn hôm nay</div>
                </div>
            </div>
            <div class="toolbar">
                <h2>Danh sách lịch hẹn hôm nay</h2>
                <div class="actions">
                    <a href="<%= request.getContextPath() %>/register.jsp" class="btn-primary">+ Đăng ký bệnh nhân</a>
                    <button class="btn-outline" onclick="window.location.reload()">Làm mới</button>
                </div>
            </div>
            <div class="table-wrapper">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Bệnh nhân</th>
                            <th>Bác sĩ</th>
                            <th>Giờ</th>
                            <th>Phòng</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            if (todayAppointments != null && !todayAppointments.isEmpty()) {
                                int index = 1;
                                for (Appointment a : todayAppointments) {
                                    String rawStatus = a.getStatus() != null ? a.getStatus().replace(" ", "-") : "";
                                    String statusClass = "status-" + rawStatus;
                                    boolean canCheckIn = "CONFIRMED".equalsIgnoreCase(a.getStatus());
                                    boolean isCheckedIn = "Checked In".equalsIgnoreCase(a.getStatus());
                                    boolean isPending = "Pending".equalsIgnoreCase(a.getStatus());
                                    String patientName = a.getPatient() != null ? a.getPatient().getFullName() : "N/A";
                                    String doctorName = a.getDoctor() != null ? a.getDoctor().getFullName() : "N/A";
                                    String timeStr = a.getAppointmentTime() != null ? a.getAppointmentTime().toString().substring(0, 5) : "N/A";
                                    String roomStr = a.getRoom() != null ? a.getRoom() : "—";
                        %>
                        <tr>
                            <td><%= index++ %></td>
                            <td><strong><%= patientName %></strong></td>
                            <td><%= doctorName %></td>
                            <td><%= timeStr %></td>
                            <td><%= roomStr %></td>
                            <td><span class="status-badge <%= statusClass %>"><%= a.getStatus() %></span></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/staff?action=edit&id=<%= a.getAppointmentId() %>" class="action-btn btn-edit">Sửa</a>
                                <% if (isPending) { %>
                                <form action="<%= request.getContextPath() %>/ConfirmAppointmentServlet" method="POST" style="display:inline;">
                                    <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">
                                    <button type="submit" class="action-btn btn-checkin">Xác nhận</button>
                                </form>
                                <form action="<%= request.getContextPath() %>/CancelAppointmentServlet" method="POST" style="display:inline;" onsubmit="return confirm('Xác nhận huỷ lịch hẹn này?')">
                                    <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">
                                    <button type="submit" class="action-btn btn-cancel">Huỷ</button>
                                </form>
                                <% } else if (canCheckIn) { %>
                                <form action="<%= request.getContextPath() %>/CheckInServlet" method="POST" style="display:inline;">
                                    <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">
                                    <button type="submit" class="action-btn btn-checkin">Check-in</button>
                                </form>
                                <form action="<%= request.getContextPath() %>/CancelAppointmentServlet" method="POST" style="display:inline;" onsubmit="return confirm('Xác nhận huỷ lịch hẹn này?')">
                                    <input type="hidden" name="appointmentId" value="<%= a.getAppointmentId() %>">
                                    <button type="submit" class="action-btn btn-cancel">Huỷ</button>
                                </form>
                                <% } else if (isCheckedIn) { %>
                                <span class="action-btn btn-disabled">Đã check-in</span>
                                <% } else if ("Completed".equalsIgnoreCase(a.getStatus())) { %>
                                <a href="<%= request.getContextPath() %>/ViewResultServlet?appointmentId=<%= a.getAppointmentId() %>" class="action-btn btn-view">Xem KQ</a>
                                <% } else if ("Cancelled".equalsIgnoreCase(a.getStatus())) { %>
                                <span class="action-btn btn-disabled">Đã huỷ</span>
                                <% } else { %>
                                <span class="action-btn btn-disabled"><%= a.getStatus() %></span>
                                <% } %>
                            </td>
                        </tr>
                        <%
                                }
                            } else {
                        %>
                        <tr><td colspan="7" class="empty">Không có lịch hẹn nào hôm nay.</td></tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
