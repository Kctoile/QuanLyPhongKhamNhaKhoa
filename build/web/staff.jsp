<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Lễ tân - Quản lý lịch hẹn</title>
                    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
                    <link rel="stylesheet" href="css/staff.css">
                </head>

                <body>
                    <div class="staff-layout">
                        <header class="staff-header">
                            <div class="header-title">
                                <h2>TRANG LỄ TÂN</h2>
                                <p>Quản lý lịch hẹn & Đón tiếp khách hàng</p>
                            </div>
                            <div class="header-actions">
                                <span class="welcome-text">Xin chào, <span class="user-name">${sessionScope.user.fullName}</span></span>
                                <a href="${pageContext.request.contextPath}/" class="btn-link"><i class="fas fa-home"></i> Trang chủ</a>
                                <a href="logout" class="btn-link btn-logout"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
                            </div>
                        </header>

                        <main class="staff-content">
                            <c:if test="${not empty sessionScope.error}">
                                <div class="alert alert-error">
                                    <i class="fas fa-exclamation-circle"></i> ${sessionScope.error}
                                </div>
                                <c:remove var="error" scope="session" />
                            </c:if>

                            <c:if test="${not empty sessionScope.success}">
                                <script>
                                    alert("${sessionScope.success}");
                                </script>
                                <c:remove var="success" scope="session" />
                            </c:if>

                            <div class="booking-card">
                                <div class="card-header">
                                    <h3><i class="fas fa-calendar-plus"></i> Đặt lịch khám tại chỗ cho khách</h3>
                                </div>

                                <form action="staff" method="post">
                                    <input type="hidden" name="action" value="book">
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="patientName">Khách hàng:</label>
                                            <input type="text" name="patientName" id="patientName" class="form-control" list="customerList" placeholder="Nhập tên khách hàng..." required>
                                            <datalist id="customerList">
                                <c:forEach var="c" items="${customers}">
                                    <option value="${c.fullName}"></option>
                                </c:forEach>
                            </datalist>
                                        </div>
                                        <div class="form-group">
                                            <label for="doctorId">Bác sĩ:</label>
                                            <select name="doctorId" id="doctorId" class="form-control" required>
                                <option value="">-- Chọn bác sĩ --</option>
                                <c:forEach var="bs" items="${doctors}">
                                    <option value="${bs.userId}">${bs.fullName}</option>
                                </c:forEach>
                            </select>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="appointmentDate">Ngày khám:</label>
                                            <input type="date" name="appointmentDate" id="appointmentDate" class="form-control" required>
                                        </div>
                                        <div class="form-group">
                                            <label for="appointmentTime">Giờ khám:</label>
                                            <select name="appointmentTime" id="appointmentTime" class="form-control" required>
                                <option value="">-- Chọn giờ --</option>
                                <option value="08:00">08:00</option>
                                <option value="08:30">08:30</option>
                                <option value="09:00">09:00</option>
                                <option value="09:30">09:30</option>
                                <option value="10:00">10:00</option>
                                <option value="10:30">10:30</option>
                                <option value="11:00">11:00</option>
                                <option value="11:30">11:30</option>
                                <option value="13:00">13:00</option>
                                <option value="13:30">13:30</option>
                                <option value="14:00">14:00</option>
                                <option value="14:30">14:30</option>
                                <option value="15:00">15:00</option>
                                <option value="15:30">15:30</option>
                                <option value="16:00">16:00</option>
                                <option value="16:30">16:30</option>
                                <option value="17:00">17:00</option>
                                <option value="17:30">17:30</option>
                                <option value="18:00">18:00</option>
                                <option value="18:30">18:30</option>
                                <option value="19:00">19:00</option>
                                <option value="19:30">19:30</option>
                                <option value="20:00">20:00</option>
                            </select>
                                        </div>
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label for="serviceIds">Dịch vụ:</label>
                                            <select name="serviceIds" id="serviceIds" multiple class="form-control" required style="height: 120px;">
                                <c:forEach var="dv" items="${services}">
                                    <option value="${dv.serviceId}">${dv.serviceName} - <fmt:formatNumber value="${dv.price}" type="currency" currencySymbol="₫" /></option>
                                </c:forEach>
                            </select>
                                        </div>
                                    </div>
                                    <div class="form-actions">
                                        <button type="submit" class="btn-primary"><i class="fas fa-calendar-check"></i> Đặt lịch ngay</button>
                                    </div>
                                </form>
                            </div>

                            <div class="list-card">
                                <div class="card-header">
                                    <h3><i class="fas fa-list-alt"></i> Danh sách lịch hẹn</h3>
                                </div>

                                <div style="margin-bottom: 25px; display: flex; align-items: center;">
                                    <label for="search" style="font-weight: 600; font-size: 15px;">Tìm kiếm lịch hẹn:</label>
                                    <input type="text" id="search" class="form-control" placeholder="Nhập tên khách hàng hoặc mã lịch hẹn..." onkeyup="filterAppointments()" style="width: 320px; display: inline-block; margin-left: 15px;">
                                </div>

                                <script>
                                    function filterAppointments() {
                                        const input = document.getElementById('search').value.toLowerCase();
                                        const rows = document.querySelectorAll('.data-table tbody tr');
                                        rows.forEach((row, index) => {
                                            const cells = row.getElementsByTagName('td');
                                            const customerName = cells[1] ? .textContent.toLowerCase() || '';
                                            const appointmentId = cells[0] ? .textContent.toLowerCase() || '';
                                            row.style.display = customerName.includes(input) || appointmentId.includes(input) || input === '' ? '' : 'none';
                                        });
                                    }
                                </script>

                                <div class="table-responsive">
                                    <table class="data-table">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Khách hàng</th>
                                                <th>Bác sĩ</th>
                                                <th>Ngày</th>
                                                <th>Giờ</th>
                                                <th>Phòng</th>
                                                <th style="text-align:center;">Trạng thái</th>
                                                <th style="text-align:center;">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="appt" items="${appointments}" varStatus="status">
                                                <tr>
                                                    <td style="text-align:center;">${status.count}</td>
                                                    <td style="font-weight: 500;">${appt.patient.fullName}</td>
                                                    <td>BS. ${appt.doctor.fullName}</td>
                                                    <td>
                                                        <fmt:formatDate value="${appt.appointmentDate}" pattern="dd/MM/yyyy" />
                                                    </td>
                                                    <td>${appt.appointmentTime}</td>
                                                    <td>${appt.room}</td>
                                                    <td style="text-align:center;">
                                                        <span class="status-badge status-${fn:replace(appt.status, ' ', '-')}">${appt.status}</span>
                                                    </td>
                                                    <td style="text-align:center; min-width: 250px;">
                                                        <form method="post" action="staff" style="margin-bottom: 8px; display:flex; gap:5px; justify-content:center;">
                                                            <input type="hidden" name="appointmentId" value="${appt.appointmentId}" />
                                                            <button type="submit" name="action" value="checkin" style="background:#0ea5e9; color:white; border:none; padding:5px 10px; border-radius:4px; font-weight:600; cursor:pointer;" title="Đã đến phòng khám">Check In</button>
                                                            <button type="submit" name="action" value="checkout" style="background:#6366f1; color:white; border:none; padding:5px 10px; border-radius:4px; font-weight:600; cursor:pointer;" title="Đã khám xong">Check Out</button>
                                                            <button type="submit" name="action" value="complete" style="background:#10b981; color:white; border:none; padding:5px 10px; border-radius:4px; font-weight:600; cursor:pointer;" title="Hoàn tất hồ sơ">Complete</button>
                                                        </form>
                                                        <div>
                                                            <a href="staff?action=edit&id=${appt.appointmentId}" style="color:#0ea5e9; text-decoration:none; margin-right:15px; font-weight:600;" title="Sửa lịch"><i class="fas fa-edit"></i> Sửa</a>
                                                            <form method="post" action="staff" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa lịch hẹn này không?');">
                                                                <input type="hidden" name="action" value="delete">
                                                                <input type="hidden" name="appointmentId" value="${appt.appointmentId}">
                                                                <button type="submit" style="color:#ef4444; background:none; border:none; padding:0; cursor:pointer; font-weight:600;" title="Xóa lịch"><i class="fas fa-trash-alt"></i> Xóa</button>
                                                            </form>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty appointments}">
                                                <tr>
                                                    <td colspan="8" style="text-align:center; padding: 30px; color:#64748b;">Chưa có lịch hẹn nào được đăng ký.</td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </main>
                    </div>
                </body>

                </html>