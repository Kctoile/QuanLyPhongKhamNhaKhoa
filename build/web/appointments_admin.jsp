<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="admin-layout">

    <%@ include file="admin_menu.jsp" %>

   <div style="margin-left: 30px; padding: 6px;" class="appointment-container">

        <div class="appointments-page">

            <div style="padding:20px;">
                <h2>QUẢN LÝ LỊCH HẸN (TỔNG HỢP)</h2>
                <p><em>Chỉ xem toàn bộ lịch sử lịch hẹn trong hệ thống. Lễ tân sẽ thao tác đổi trạng thái.</em></p>
                 <table border="1" cellpadding="10" style="border-collapse: collapse; width: 100%;">
                    <tr style="background-color: #f2f2f2;">
                        <th>ID</th>
                        <th>Bệnh nhân</th>
                        <th>Bác sĩ</th>
                        <th>Ngày hẹn</th>
                        <th>Giờ hẹn</th>
                        <th>Trạng thái</th>
                        <th>Phòng ghi chú</th>
                    </tr>

                    <c:forEach var="h" items="${list}">
                        <tr>
                            <td>${h.appointmentId}</td>
                            <td>${h.patient != null ? h.patient.fullName : 'Khách vãng lai'}</td>
                            <td>${h.doctor != null ? h.doctor.fullName : 'Chưa phân công'}</td>
                            <td>
                                <fmt:formatDate value="${h.appointmentDate}" pattern="dd/MM/yyyy" />
                            </td>
                            <td>${h.appointmentTime}</td>
                            <td>
                                <span
                                    style="font-weight:bold; color: ${h.status == 'Pending' ? 'orange' : (h.status == 'Checked In' ? 'blue' : (h.status == 'Completed' ? 'green' : 'red'))}">
                                    ${h.status}
                                </span>
                            </td>
                            <td>${h.room}</td>
                        </tr>
                    </c:forEach>
                </table>
            </div>

            <div style="margin-bottom: 20px;">
                <label for="search">Tìm kiếm lịch hẹn:</label>
                <input type="text" id="search" placeholder="Nhập tên khách hàng hoặc mã lịch hẹn..." onkeyup="filterAppointments()" style="width: 250px; padding: 5px; margin-bottom: 10px;">
            </div>

            <script>
                function filterAppointments() {
                    const input = document.getElementById('search').value.toLowerCase();
                    const rows = document.querySelectorAll('table tr');
                    rows.forEach((row, index) => {
                        if (index === 0) return; // Skip header row
                        const cells = row.getElementsByTagName('td');
                        const customerName = cells[1]?.textContent.toLowerCase() || '';
                        const appointmentId = cells[0]?.textContent.toLowerCase() || '';
                        row.style.display = customerName.includes(input) || appointmentId.includes(input) || input === '' ? '' : 'none';
                    });
                }
            </script>
        </div>
    </div>
</div>