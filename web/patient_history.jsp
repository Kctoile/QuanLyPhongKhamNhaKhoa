<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Lịch sử khám bệnh</title>
    <link rel="stylesheet" href="css/doctor.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>

<body>
    <div class="doctor-layout">
        <header class="doctor-header">
            <div class="header-title">
                <h2>Cổng Thông Tin Bác Sĩ</h2>
                <p>Chi tiết hồ sơ bệnh án (HSBA) cũ của Bệnh nhân</p>
            </div>
            <div class="header-actions">
                <span class="welcome-text">Xin chào, <span class="user-name">BS. ${sessionScope.user.fullName}</span></span>
                <a href="${pageContext.request.contextPath}/" class="btn-link"><i class="fas fa-home"></i> Trang chủ</a>
                <a href="logout" class="btn-link btn-logout"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
            </div>
        </header>

        <main class="doctor-content">
            <div class="data-card">
                <div class="card-header">
                    <h3><i class="fas fa-history"></i> Lịch Sử Khám Bệnh - Hồ Sơ Bệnh Án</h3>
                    <a href="doctor" class="btn-action" style="background: #e2e8f0; color: #334155;"><i class="fas fa-arrow-left"></i> Quay lại lịch khám</a>
                </div>
                
                <div class="table-responsive">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th style="width: 15%">Ngày khám</th>
                                <th style="width: 35%"><i class="fas fa-stethoscope"></i> Chẩn đoán / Kết quả</th>
                                <th style="width: 25%"><i class="fas fa-pills"></i> Đơn thuốc</th>
                                <th style="width: 25%"><i class="fas fa-comment-medical"></i> Ghi chú Bác sĩ</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="result" items="${history}">
                                <tr>
                                    <td style="font-weight: 600;">
                                        <fmt:formatDate value="${result.examinationDate}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td>
                                        <div class="result-content" style="padding: 12px; font-size: 14px; background: #f8fafc; border:none; margin:0;">
                                            ${result.resultDetails}
                                        </div>
                                    </td>
                                    <td>
                                        <c:if test="${not empty result.prescription}">
                                            <div style="font-size: 13px; color: #0f172a;">${result.prescription}</div>
                                        </c:if>
                                        <c:if test="${empty result.prescription}">
                                            <span style="color:#94a3b8; font-style:italic;">Không kê toa</span>
                                        </c:if>
                                    </td>
                                    <td>${result.doctorNotes}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty history}">
                                <tr>
                                    <td colspan="4" class="empty-state">
                                        <i class="fas fa-folder-open empty-icon" style="font-size: 40px; color: #cbd5e1; margin-bottom: 10px; display: block;"></i>
                                        Bệnh nhân này chưa có lịch sử khám bệnh lưu trong hệ thống.
                                    </td>
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