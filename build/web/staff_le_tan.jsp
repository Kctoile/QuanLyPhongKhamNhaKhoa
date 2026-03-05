<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Lễ tân - Quản lý lịch hẹn</title>
        </head>

        <body>

            <h2>TRANG LỄ TÂN</h2>
            <p>Xin chào: ${sessionScope.user.hoTen}</p>
            <a href="dichvu">Dịch vụ</a> |
            <c:if test="${sessionScope.role == 'ADMIN'}"><a href="admin">Dashboard</a> | </c:if>
            <a href="logout">Đăng xuất</a>
            <hr>

            <h3>1. Danh sách lịch hẹn</h3>
            <table border="1" cellpadding="8">
                <tr>
                    <th>Mã</th>
                    <th>Khách hàng</th>
                    <th>Bác sĩ</th>
                    <th>Dịch vụ</th>
                    <th>Ngày - Giờ</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                <c:forEach var="l" items="${listLichHen}">
                    <tr>
                        <td>${l.maLich}</td>
                        <td>${l.tenKhachHang}</td>
                        <td>${l.tenBacSi}</td>
                        <td>${l.tenDichVu}</td>
                        <td>${l.ngayKham} ${l.gioKham}</td>
                        <td>${l.trangThai}</td>
                        <td>
                            <c:if test="${l.trangThai == 'Chờ xác nhận' or l.trangThai == 'Đã xác nhận'}">
                                <a href="staff?action=vaophong&maLich=${l.maLich}"
                                    onclick="return confirm('Xác nhận hướng dẫn khách vào phòng?');">Vào phòng</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <hr>
            <h3>2. Đặt lịch trực tiếp (Walk-in)</h3>
            <form action="staff" method="post">
                Khách hàng: <select name="maND" required>
                    <option value="">-- Chọn khách --</option>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.maND}">${c.hoTen} - ${c.email}</option>
                    </c:forEach>
                </select><br><br>
                Bác sĩ: <select name="maBacSi" required>
                    <c:forEach var="bs" items="${doctors}">
                        <option value="${bs.maND}">${bs.hoTen}</option>
                    </c:forEach>
                </select><br><br>
                Ngày: <input type="date" name="ngayKham" required>
                Giờ: <select name="gioKham" required>
                    <option value="08:00">08:00</option>
                    <option value="08:30">08:30</option>
                    <option value="09:00">09:00</option>
                    <option value="09:30">09:30</option>
                    <option value="10:00">10:00</option>
                    <option value="10:30">10:30</option>
                    <option value="11:00">11:00</option>
                    <option value="13:00">13:00</option>
                    <option value="14:00">14:00</option>
                    <option value="15:00">15:00</option>
                    <option value="16:00">16:00</option>
                    <option value="17:00">17:00</option>
                    <option value="18:00">18:00</option>
                    <option value="19:00">19:00</option>
                </select><br><br>
                Dịch vụ: <select name="maDV" required>
                    <c:forEach var="dv" items="${services}">
                        <option value="${dv.maDV}">${dv.tenDV}</option>
                    </c:forEach>
                </select><br><br>
                Ghi chú: <input type="text" name="ghiChu"><br><br>
                <button type="submit">Đặt lịch</button>
            </form>

        </body>

        </html>