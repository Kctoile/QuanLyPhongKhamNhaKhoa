<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="css/doctor.css">
<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Bác sĩ - Khám bệnh</title>       
    </head>

    <body>

        <div class="doctor-container">

            <div class="doctor-header">
                <h2>TRANG BÁC SĨ</h2>
                <p>Xin chào: ${sessionScope.user.fullName}</p>

                <div class="doctor-nav">
                    <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                    <a href="logout">Đăng xuất</a>
                </div>
            </div>

            <hr>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>

            <div class="card">
                <h3>Danh sách lịch khám của tôi</h3>

                <table class="doctor-table">
                    <tr>
                        <th>Mã</th>
                        <th>Khách hàng</th>
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
                            <td>
                                <c:forEach var="s" items="${l.services}">
                                    ${s.serviceName}<br />
                                </c:forEach>
                            </td>
                            <td>${l.room}</td>
                            <td>
                                <fmt:formatDate value="${l.appointmentDate}" pattern="dd/MM/yyyy" />
                                ${l.appointmentTime}
                            </td>
                            <td>${l.status}</td>
                            <td>
                                <c:if test="${l.status == 'Checked In'}">
                                    <a href="doctor?form=1&appointmentId=${l.appointmentId}">Khám & Kê Đơn</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
            <c:if test="${empty appointments}">
                <p>Chưa có lịch khám nào.</p>
            </c:if>

            <c:if test="${param.form == '1' && not empty param.appointmentId}">
                <hr>
                <div class="card form-section">
                    <h3>Nhập kết quả khám & Tạo đơn thuốc - Mã lịch hẹn: ${param.appointmentId}</h3>
                    <form action="doctor" method="post">
                        <input type="hidden" name="action" value="save_exam">
                        <input type="hidden" name="appointmentId" value="${param.appointmentId}">

                        <p><strong>Kết quả khám:</strong><br>
                            <textarea name="resultDetails" rows="4" cols="60" required
                                      placeholder="Nhập chẩn đoán, kết quả khám..."></textarea>
                        </p>

                        <p><strong>Chỉ định dịch vụ thêm (nếu có):</strong><br>
                            <select name="prescribedServiceIds" multiple style="width: 300px; height: 100px;">
                                <c:forEach var="dv" items="${services}">
                                    <option value="${dv.serviceId}">${dv.serviceName}</option>
                                </c:forEach>
                            </select><br><small>(Ctrl + Click để chọn nhiều)</small>
                        </p>

                        <p><strong>Kê Đơn Thuốc - Hướng dẫn chung:</strong><br>
                            <input type="text" name="instructions" size="70"
                                   placeholder="VD: Uống sau bữa ăn, ngày 3 lần...">
                        </p>

                        <p><strong>Kê thuốc:</strong></p>
                        <c:forEach var="m" items="${medicines}">
                            <div class="thuoc-row">
                                <input type="hidden" name="medicineIds" value="${m.medicineId}">
                                ${m.medicineName} (Tồn kho: ${m.stockQuantity}) -
                                <fmt:formatNumber value="${m.price}" type="number" /> đ/đơn vị:
                                <input type="number" name="quantities" min="0" value="0" style="width:60px">
                            </div>
                        </c:forEach>
                        <c:if test="${empty medicines}">
                            <p><em>Chưa có thuốc trong danh mục. Admin cần thêm thuốc trước.</em></p>
                        </c:if>
                        <p><button type="submit">Hoàn thành khám & Lưu đơn thuốc</button></p>
                    </form>
                </div>
            </c:if>


        </div>
    </body>
</html>