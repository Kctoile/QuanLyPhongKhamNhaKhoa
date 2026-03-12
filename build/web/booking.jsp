<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <link rel="stylesheet" href="css/booking.css" />
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
            <title>Đặt lịch khám</title>

            <link rel="stylesheet" href="css/style.css">

        </head>

        <body>

            <div class="page-wrapper">

                <div class="card">

                    <h2 class="title">Đặt lịch khám</h2>

                    <p class="welcome">Xin chào: ${sessionScope.user.fullName}</p>

                    <div class="nav">
                        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                        <a href="logout">Đăng xuất</a>
                    </div>

                    <hr>

                    <p class="error">${error}</p>
                    <p class="success">${sessionScope.successMessage}</p>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <form action="booking" method="post" class="booking-form">

                        <label>Chọn bác sĩ:</label>
                        <select name="doctorId" required>
                        <option value="">-- Chọn bác sĩ --</option>
                        <c:forEach var="bs" items="${doctors}">
                            <option value="${bs.userId}">${bs.fullName}</option>
                        </c:forEach>
                    </select>

                        <label>Ngày khám:</label>
                        <input type="date" name="appointmentDate" required>

                        <label>Giờ khám:</label>
                        <select name="appointmentTime" required>
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

                        <label>Dịch vụ (Có thể chọn nhiều):</label>

                        <select name="serviceIds" multiple required class="service-select">
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

                        <p class="note">(Nhấn Ctrl để chọn nhiều dịch vụ)</p>

                        <label>Ghi chú:</label>

                        <textarea name="notes" rows="3"></textarea>

                        <button type="submit" class="btn-submit">Đặt lịch</button>

                    </form>

                </div>
            </div>

        </body>

        </html>