<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Lễ tân - Quản lý lịch hẹn</title>
                </head>

                <body>

                    <h2>TRANG LỄ TÂN</h2>
                    <p>Xin chào: ${sessionScope.user.fullName}</p>
                    <a href="${pageContext.request.contextPath}/">Trang chủ</a> |
                    <a href="logout">Đăng xuất</a>
                    <hr>

                    <div style="display: flex; justify-content: center; margin-bottom: 30px;">
                        <div style="border: 1px solid #ccc; border-radius: 8px; padding: 24px; width: 420px; background: #f9f9f9; box-shadow: 0 2px 8px #eee;">
                            <h3 style="text-align:center; margin-bottom: 18px; color:#007bff;">Đặt lịch khám tại chỗ cho khách</h3>
                            <c:if test="${not empty sessionScope.error}">
                                <div style="color:red; margin-bottom:10px;">${sessionScope.error}</div>
                                <c:remove var="error" scope="session" />
                            </c:if>
                            <form action="staff" method="post">
                                <input type="hidden" name="action" value="book">
                                <div style="margin-bottom:12px;">
                                    <label for="patientId">Khách hàng:</label><br>
                                    <select name="patientId" id="patientId" style="width:100%; padding:6px;" required>
                    <option value="">-- Chọn khách hàng --</option>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.userId}">${c.fullName}</option>
                    </c:forEach>
                </select>
                                </div>
                                <div style="margin-bottom:12px;">
                                    <label for="doctorId">Bác sĩ:</label><br>
                                    <select name="doctorId" id="doctorId" style="width:100%; padding:6px;" required>
                    <option value="">-- Chọn bác sĩ --</option>
                    <c:forEach var="bs" items="${doctors}">
                        <option value="${bs.userId}">${bs.fullName}</option>
                    </c:forEach>
                </select>
                                </div>
                                <div style="margin-bottom:12px;">
                                    <label for="appointmentDate">Ngày khám:</label><br>
                                    <input type="date" name="appointmentDate" id="appointmentDate" style="width:100%; padding:6px;" required>
                                </div>
                                <div style="margin-bottom:12px;">
                                    <label for="appointmentTime">Giờ khám:</label><br>
                                    <select name="appointmentTime" id="appointmentTime" style="width:100%; padding:6px;" required>
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
                                <div style="margin-bottom:12px;">
                                    <label for="serviceIds">Dịch vụ (có thể chọn nhiều):</label><br>
                                    <select name="serviceIds" id="serviceIds" multiple required style="width:100%; height:100px; padding:6px;">
                    <c:forEach var="dv" items="${services}">
                        <option value="${dv.serviceId}">
                            <c:choose>
                                <c:when test="${dv.serviceName eq 'Trồng răng Implant'}">
                                    ${dv.serviceName} - 15000000 VNĐ
                                </c:when>
                                <c:otherwise>
                                    ${dv.serviceName} - ${dv.price} VNĐ
                                </c:otherwise>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select>
                                    <small>(Nhấn Ctrl để chọn nhiều dịch vụ)</small>
                                </div>
                                <div style="margin-bottom:12px;">
                                    <label for="room">Phòng:</label><br>
                                    <input type="text" name="room" id="room" style="width:100%; padding:6px;" required>
                                </div>
                                <div style="text-align:center;">
                                    <button type="submit" style="padding:8px 24px; background:#007bff; color:#fff; border:none; border-radius:4px; font-weight:bold; cursor:pointer;">Đặt lịch</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <c:if test="${not empty sessionScope.error}">
                        <p style="color:red">${sessionScope.error}</p>
                        <c:remove var="error" scope="session" />
                    </c:if>

                    <h3>Danh sách lịch hẹn</h3>
                    <table border="1" cellpadding="8">
                        <tr>
                            <th>Mã</th>
                            <th>Khách hàng</th>
                            <th>Bác sĩ</th>
                            <th>Dịch vụ</th>
                            <th>Phòng</th>
                            <th>Ngày - Giờ</th>
                            <th>Trạng thái</th>
                            <th>Hành động</th>
                        </tr>
                        <c:forEach var="l" items="${appointments}">
                            <tr>
                                <td>${l.appointmentId}</td>
                                <td>${l.patient != null ? l.patient.fullName : 'Khách vãng lai'}</td>
                                <td>${l.doctor != null ? l.doctor.fullName : 'Chưa phân công'}</td>
                                <td>${l.room != null ? l.room : 'Chưa xếp'}</td>
                                <td>
                                    <fmt:formatDate value="${l.appointmentDate}" pattern="dd/MM/yyyy" /> ${l.appointmentTime}
                                </td>
                                <td>${l.status}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${l.status == 'Pending'}">
                                            <form action="staff" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="update_status">
                                                <input type="hidden" name="appointmentId" value="${l.appointmentId}">
                                                <input type="hidden" name="status" value="CONFIRMED"> Phòng: <input type="text" name="room" size="5" value="${l.room}" required>
                                                <button type="submit" onclick="return confirm('Xác nhận lịch đặt trước này?');">Xác nhận</button>
                                            </form>
                                        </c:when>
                                        <c:when test="${l.status == 'CONFIRMED'}">
                                            <form action="staff" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="update_status">
                                                <input type="hidden" name="appointmentId" value="${l.appointmentId}">
                                                <input type="hidden" name="status" value="Checked In"> Phòng: <input type="text" name="room" size="5" value="${l.room}" required>
                                                <button type="submit" onclick="return confirm('Xác nhận khách đã tới (Checked In)?');">Check In</button>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </body>

                </html>