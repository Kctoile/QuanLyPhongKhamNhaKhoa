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
                    <tr style="background-color: #f2f2f2;">
                        <th>STT</th>
                        <th onclick="sortTable(1)" style="cursor:pointer;" title="Click để sắp xếp">Bệnh nhân &#x21D5;</th>
                        <th onclick="sortTable(2)" style="cursor:pointer;" title="Click để sắp xếp">Bác sĩ &#x21D5;</th>
                        <th onclick="sortTable(3)" style="cursor:pointer;" title="Click để sắp xếp">Ngày hẹn &#x21D5;</th>
                        <th onclick="sortTable(4)" style="cursor:pointer;" title="Click để sắp xếp">Giờ hẹn &#x21D5;</th>
                        <th onclick="sortTable(5)" style="cursor:pointer;" title="Click để sắp xếp">Trạng thái &#x21D5;</th>
                        <th>Phòng ghi chú</th>
                        <th style="width: 100px;">Hành động</th>
                    </tr>

                    <c:forEach var="h" items="${list}" varStatus="status">
                        <tr>
                            <td style="text-align:center;">${status.count}</td>
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
                            <td style="text-align: center;">
                                <a href="appointment_admin?action=edit&id=${h.appointmentId}" style="color:#007bff; text-decoration:none; margin-right:8px; display:inline-block;" title="Sửa">Sửa</a>
                                <form method="post" action="appointment_admin" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa lịch hẹn này không?');">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="appointmentId" value="${h.appointmentId}">
                                    <button type="submit" style="color:#dc3545; background:none; border:none; padding:0; cursor:pointer; font-size:16px;" title="Xóa">Xóa</button>
                                </form>
                            </td>
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

                function sortTable(n) {
                    var table = document.querySelector("table");
                    var rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
                    switching = true;
                    // Mặc định sắp xếp tăng dần
                    dir = "asc"; 
                    while (switching) {
                        switching = false;
                        rows = table.rows;
                        for (i = 1; i < (rows.length - 1); i++) {
                            shouldSwitch = false;
                            x = rows[i].getElementsByTagName("TD")[n];
                            y = rows[i + 1].getElementsByTagName("TD")[n];
                            
                            let valX = x.textContent.trim().toLowerCase();
                            let valY = y.textContent.trim().toLowerCase();
                            
                            // Nếu đang ráng sắp xếp ID
                            if (n === 0) {
                                valX = parseInt(valX) || 0;
                                valY = parseInt(valY) || 0;
                            } 
                            // Nếu đang sắp xếp Ngày (định dạng dd/mm/yyyy)
                            else if (n === 3) {
                                let partsX = valX.split('/');
                                let partsY = valY.split('/');
                                if (partsX.length === 3 && partsY.length === 3) {
                                    valX = partsX[2] + partsX[1] + partsX[0];
                                    valY = partsY[2] + partsY[1] + partsY[0];
                                }
                            }
                            
                            if (dir == "asc") {
                                if (valX > valY) {
                                    shouldSwitch = true;
                                    break;
                                }
                            } else if (dir == "desc") {
                                if (valX < valY) {
                                    shouldSwitch = true;
                                    break;
                                }
                            }
                        }
                        if (shouldSwitch) {
                            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                            switching = true;
                            switchcount ++; 
                        } else {
                            // Nếu duyệt hết mà chưa đổi chỗ lần nào và đang là chiều asc, thì đổi thành desc và duyệt lại
                            if (switchcount == 0 && dir == "asc") {
                                dir = "desc";
                                switching = true;
                            }
                        }
                    }
                }
            </script>
        </div>
    </div>
</div>