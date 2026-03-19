<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="admin-layout">
    <div style="margin: 30px auto; padding: 20px; max-width: 600px; border: 1px solid #ccc; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
        <h2 style="text-align: center; color: #007bff;">SỬA LỊCH HẸN (LỄ TÂN)</h2>
        <form action="staff" method="post" style="margin-top: 20px;">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
            
            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Khách hàng: </label>
                <select name="patientId" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.userId}" ${c.userId == appointment.patientId ? 'selected' : ''}>${c.fullName}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Bác sĩ: </label>
                <select name="doctorId" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <c:forEach var="d" items="${doctors}">
                        <option value="${d.userId}" ${d.userId == appointment.doctorId ? 'selected' : ''}>${d.fullName}</option>
                    </c:forEach>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Ngày hẹn: </label>
                <input type="date" name="appointmentDate" value="${appointment.appointmentDate}" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Giờ hẹn: </label>
                <input type="time" name="appointmentTime" value="${appointment.appointmentTime}" required style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Trạng thái: </label>
                <select name="status" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
                    <option value="Pending" ${appointment.status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="CONFIRMED" ${appointment.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                    <option value="Checked In" ${appointment.status == 'Checked In' ? 'selected' : ''}>Checked In</option>
                    <option value="Checked Out" ${appointment.status == 'Checked Out' ? 'selected' : ''}>Checked Out</option>
                    <option value="Completed" ${appointment.status == 'Completed' ? 'selected' : ''}>Completed</option>
                    <option value="Cancelled" ${appointment.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                </select>
            </div>

            <div style="margin-bottom: 12px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Phòng khám: </label>
                <input type="text" name="room" value="${appointment.room}" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
            </div>

            <div style="margin-bottom: 20px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Ghi chú: </label>
                <textarea name="notes" rows="4" style="width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px;">${appointment.notes}</textarea>
            </div>

            <div style="text-align: center;">
                <button type="submit" style="padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">Lưu Thay Đổi</button>
                <a href="staff" style="margin-left: 15px; text-decoration: none; color: #555;">Hủy bỏ</a>
            </div>
        </form>
    </div>
</div>
