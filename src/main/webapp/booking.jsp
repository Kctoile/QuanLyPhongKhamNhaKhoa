<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Đặt lịch khám tại phòng khám nha khoa">
    <title>Đặt Lịch Khám - Dental Clinic</title>
    <link rel="stylesheet" href="css/global.css">
    <link rel="stylesheet" href="css/booking.css">
</head>
<body class="booking-layout">

    <header class="booking-header">
        <div class="booking-header-left">
            <h1>🦷 Đặt Lịch Khám</h1>
            <p>Phòng Khám Nha Khoa</p>
        </div>
        <nav class="booking-header-nav" aria-label="Điều hướng trang">
            <a href="${pageContext.request.contextPath}/">Trang chủ</a>
            <a href="appointments">Lịch của tôi</a>
            <a href="logout">Đăng xuất</a>
        </nav>
    </header>

    <main class="booking-content" id="main-content">

        <div class="patient-banner">
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z"/></svg>
            <div>
                <span>Bệnh nhân: </span><strong>${sessionScope.user.fullName}</strong>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error" role="alert">${error}</div>
        </c:if>
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error" role="alert">${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success" role="alert">${sessionScope.successMessage}</div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <div class="booking-card">
            <div class="booking-card-header">
                <h2>Thông tin đặt lịch</h2>
            </div>
            <div class="booking-card-body">
                <form action="booking" method="post">
                    <div class="form-grid">
                        <div class="field-wrap">
                            <label class="field-label" for="doctorId">Bác sĩ <span style="color:var(--color-destructive)">*</span></label>
                            <select id="doctorId" name="doctorId" class="field-control" required>
                                <option value="">— Chọn bác sĩ —</option>
                                <c:forEach var="doctor" items="${doctors}">
                                    <option value="${doctor.userId}">${doctor.fullName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="field-wrap">
                            <label class="field-label" for="appointmentDate">Ngày khám <span style="color:var(--color-destructive)">*</span></label>
                            <input type="date" id="appointmentDate" name="appointmentDate"
                                   class="field-control" min="${minAppointmentDate}" required>
                        </div>
                        <div class="field-wrap">
                            <label class="field-label" for="appointmentTime">Giờ khám <span style="color:var(--color-destructive)">*</span></label>
                            <select id="appointmentTime" name="appointmentTime" class="field-control" required>
                                <option value="">— Chọn giờ —</option>
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

                    <fieldset class="service-fieldset">
                        <legend>Dịch vụ</legend>
                        <div class="service-grid">
                            <c:forEach var="service" items="${services}">
                                <label class="service-option">
                                    <input type="checkbox" name="serviceIds" value="${service.serviceId}">
                                    <div class="service-option-box">
                                        <span class="service-name-text">${service.serviceName}</span>
                                        <span class="service-price-text">${service.price} VNĐ</span>
                                        <span class="service-check">
                                            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="3" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/></svg>
                                            Đã chọn
                                        </span>
                                    </div>
                                </label>
                            </c:forEach>
                        </div>
                    </fieldset>

                    <div class="field-wrap">
                        <label class="field-label" for="notes">Ghi chú</label>
                        <textarea id="notes" name="notes" class="notes-area"
                                  placeholder="Triệu chứng, yêu cầu đặc biệt hoặc khung giờ linh hoạt..."></textarea>
                    </div>

                    <button type="submit" class="btn-book">
                        <svg fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2.5" aria-hidden="true"><path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 012.25-2.25h13.5A2.25 2.25 0 0121 7.5v11.25m-18 0A2.25 2.25 0 005.25 21h13.5A2.25 2.25 0 0021 18.75m-18 0v-7.5A2.25 2.25 0 015.25 9h13.5A2.25 2.25 0 0121 11.25v7.5m-9-6h.008v.008H12v-.008zM12 15h.008v.008H12V15zm0 2.25h.008v.008H12v-.008zM9.75 15h.008v.008H9.75V15zm0 2.25h.008v.008H9.75v-.008zM7.5 15h.008v.008H7.5V15zm0 2.25h.008v.008H7.5v-.008zm6.75-4.5h.008v.008h-.008v-.008zm0 2.25h.008v.008h-.008V15zm0 2.25h.008v.008h-.008v-.008zm2.25-4.5h.008v.008H16.5v-.008zm0 2.25h.008v.008H16.5V15z"/></svg>
                        Xác nhận đặt lịch
                    </button>
                </form>
            </div>
        </div>
    </main>
</body>
</html>
