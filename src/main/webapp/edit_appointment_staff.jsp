<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || (!"STAFF".equalsIgnoreCase((String) s.getAttribute("role")) && !"ADMIN".equalsIgnoreCase((String) s.getAttribute("role")))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa lịch hẹn</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f0f4f8;
                padding: 20px;
            }
            .header {
                background: #1a2a4a;
                color: #fff;
                padding: 15px 30px;
                border-radius: 8px;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .header h1 {
                font-size: 20px;
            }
            .header a {
                color: #4fc3f7;
                text-decoration: none;
            }
            .card {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                padding: 20px;
                max-width: 600px;
                margin: 0 auto;
            }
            .card h3 {
                color: #1a2a4a;
                border-bottom: 2px solid #e8ecf1;
                padding-bottom: 10px;
                margin-bottom: 20px;
            }
            .form-group {
                margin-bottom: 16px;
            }
            .form-group label {
                display: block;
                font-weight: 600;
                margin-bottom: 6px;
                font-size: 14px;
                color: #333;
            }
            .form-group input, .form-group select, .form-group textarea {
                width: 100%;
                padding: 10px 12px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
            }
            .form-group input:focus, .form-group select:focus {
                outline: none;
                border-color: #1a2a4a;
            }
            .btn-submit {
                width: 100%;
                padding: 12px;
                background: #1a2a4a;
                color: #fff;
                border: none;
                border-radius: 6px;
                font-size: 16px;
                cursor: pointer;
                font-weight: 600;
            }
            .btn-submit:hover {
                background: #2c3e6b;
            }
            .btn-back {
                display: inline-block;
                margin-top: 12px;
                color: #1a2a4a;
                text-decoration: none;
                font-size: 14px;
            }
            .btn-back:hover {
                text-decoration: underline;
            }
            .error {
                background: #f8d7da;
                color: #721c24;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
            }
            .success {
                background: #d4edda;
                color: #155724;
                padding: 12px;
                border-radius: 6px;
                margin-bottom: 16px;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>Chỉnh sửa lịch hẹn</h1>
            <a href="staff">Quay lại</a>
        </div>
        <div class="card">
            <h3>Thông tin lịch hẹn #${appointment.appointmentId}</h3>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="success">${message}</div>
            </c:if>

            <form action="staff" method="post">
                <input type="hidden" name="action" value="update" />
                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}" />
                <!-- FIX: fallback patientId = 0 khi null -->
                <input type="hidden" name="patientId" value="${appointment.patientId != null ? appointment.patientId : '0'}" />
                <input type="hidden" name="notes" value="${appointment.notes}" />

                <div class="form-group">
                    <label>Bệnh nhân</label>
                    <input type="text" value="${appointment.patient.fullName}" readonly />
                </div>
                <div class="form-group">
                    <label for="doctorId">Bác sĩ</label>
                    <select id="doctorId" name="doctorId" required>
                        <c:forEach var="doc" items="${doctors}">
                            <option value="${doc.userId}" ${doc.userId == appointment.doctorId ? 'selected' : ''}>${doc.fullName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="appointmentDate">Ngày khám</label>
                    <input type="date" id="appointmentDate" name="appointmentDate"
                           value="<fmt:formatDate value='${appointment.appointmentDate}' pattern='yyyy-MM-dd' />" required />
                </div>
                <div class="form-group">
                    <label for="appointmentTime">Giờ khám</label>
                    <input type="time" id="appointmentTime" name="appointmentTime"
                           value="<fmt:formatDate value='${appointment.appointmentTime}' pattern='HH:mm' />" required />
                </div>
                <div class="form-group">
                    <label for="room">Phòng</label>
                    <input type="text" id="room" name="room" value="${appointment.room}" placeholder="VD: 1, 2, 3..." />
                </div>
                <div class="form-group">
                    <label for="status">Trạng thái</label>
                    <select id="status" name="status">
                        <option value="Pending" ${appointment.status == 'Pending' ? 'selected' : ''}>Chờ xác nhận</option>
                        <option value="CONFIRMED" ${appointment.status == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                        <option value="Checked In" ${appointment.status == 'Checked In' ? 'selected' : ''}>Checked In</option>
                        <option value="Completed" ${appointment.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                        <option value="Cancelled" ${appointment.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                    </select>
                </div>
                <button type="submit" class="btn-submit">Lưu thay đổi</button>
            </form>
            <a href="staff" class="btn-back">Quay lại danh sách</a>
        </div>
    </body>
</html>
