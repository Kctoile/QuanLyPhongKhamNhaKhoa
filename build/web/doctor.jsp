<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Bác sĩ - Khám bệnh</title>
                    <link rel="stylesheet" href="css/doctor.css">
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
                </head>

                <body>
                    <div class="doctor-layout">
                        <header class="doctor-header">
                            <div class="header-title">
                                <h2>Cổng Thông Tin Bác Sĩ</h2>
                                <p>Quản lý lịch khám & Kê đơn thuốc</p>
                            </div>
                            <div class="header-actions">
                                <span class="welcome-text">Xin chào, <span class="user-name">BS. ${sessionScope.user.fullName}</span></span>
                                <a href="${pageContext.request.contextPath}/" class="btn-link"><i class="fas fa-home"></i> Trang chủ</a>
                                <a href="logout" class="btn-link btn-logout"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
                            </div>
                        </header>

                        <main class="doctor-content">
                            <c:if test="${not empty error}">
                                <div class="alert alert-error">
                                    <i class="fas fa-exclamation-circle"></i> ${error}
                                </div>
                            </c:if>

                            <section class="list-section">
                                <div class="data-card">
                                    <div class="card-header">
                                        <h3><i class="fas fa-calendar-check"></i> Danh sách lịch hẹn khám của tôi</h3>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="data-table">
                                            <thead>
                                                <tr>
                                                    <th class="col-id">Mã</th>
                                                    <th class="col-patient">Bệnh nhân</th>
                                                    <th>Dịch vụ</th>
                                                    <th>Phòng</th>
                                                    <th>Thời gian</th>
                                                    <th>Trạng thái</th>
                                                    <th style="text-align: center;">Hành động</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="l" items="${appointments}">
                                                    <tr>
                                                        <td class="col-id">#${l.appointmentId}</td>
                                                        <td class="col-patient">${l.patient != null ? l.patient.fullName : 'Khách vãng lai'}</td>
                                                        <td>
                                                            <div class="service-tags">
                                                                <c:forEach var="s" items="${l.services}">
                                                                    <span class="tag">${s.serviceName}</span>
                                                                </c:forEach>
                                                            </div>
                                                        </td>
                                                        <td><span class="room-badge">${l.room}</span></td>
                                                        <td>
                                                            <div class="datetime-info">
                                                                <i class="far fa-calendar-alt"></i>
                                                                <fmt:formatDate value="${l.appointmentDate}" pattern="dd/MM/yyyy" />
                                                                <br>
                                                                <i class="far fa-clock"></i> ${l.appointmentTime}
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <span class="status-badge status-${fn:replace(l.status, ' ', '-')}">
                                                ${l.status}
                                            </span>
                                                        </td>
                                                        <td style="text-align: center;">
                                                            <c:choose>
                                                                <c:when test="${l.status == 'Checked In'}">
                                                    <div style="display: flex; gap: 5px; justify-content: center;">
                                                        <a href="doctor?form=1&appointmentId=${l.appointmentId}" class="btn-action primary" title="Khám & Kê Đơn">
                                                            <i class="fas fa-stethoscope"></i> Khám Bệnh
                                                        </a>
                                                        <c:if test="${l.patient != null}">
                                                            <a href="doctor?action=view_history&patientId=${l.patient.userId}" class="btn-action" style="background: #e2e8f0; color: #475569;" title="Xem Lịch sử Khám">
                                                                <i class="fas fa-history"></i> HSBA
                                                            </a>
                                                        </c:if>
                                                    </div>
                                                </c:when>
                                                                <c:when test="${l.status == 'Completed'}">
                                                                    <a href="doctor?form=2&appointmentId=${l.appointmentId}" class="btn-action info" title="Xem Kết Quả">
                                                                        <i class="fas fa-clipboard-list"></i> Xem KQ
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="text-muted"><i class="fas fa-minus"></i></span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty appointments}">
                                                    <tr>
                                                        <td colspan="7" class="empty-state">
                                                            <i class="fas fa-inbox empty-icon"></i>
                                                            <p>Chưa có lịch khám nào được phân công cho bạn.</p>
                                                        </td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </section>

                            <c:if test="${param.form == '1' && not empty param.appointmentId}">
                                <section class="form-section fade-in">
                                    <div class="data-card form-card">
                                        <div class="card-header">
                                            <h3><i class="fas fa-file-medical"></i> Khám bệnh & Kê đơn thuốc</h3>
                                            <span class="badge-id">Mã lịch hẹn: #${param.appointmentId}</span>
                                        </div>

                                        <form action="doctor" method="post" class="medical-form">
                                            <input type="hidden" name="action" value="save_exam">
                                            <input type="hidden" name="appointmentId" value="${param.appointmentId}">

                                            <div class="form-group">
                                                <label><i class="fas fa-notes-medical"></i> Kết quả chẩn đoán bệnh lý:</label>
                                                <textarea name="resultDetails" rows="4" class="form-control" required placeholder="Nhập chi tiết chẩn đoán, tình trạng sức khỏe..."></textarea>
                                            </div>

                                            <div class="form-row">
                                                <div class="form-group col-half">
                                                    <label><i class="fas fa-syringe"></i> Chỉ định dịch vụ lâm sàng thêm:</label>
                                                    <select name="prescribedServiceIds" multiple class="form-control select-multiple">
                                        <c:forEach var="dv" items="${services}">
                                            <option value="${dv.serviceId}">${dv.serviceName}</option>
                                        </c:forEach>
                                    </select>
                                                    <small class="help-text"><i class="fas fa-info-circle"></i> Giữ phím Ctrl/Cmd để chọn nhiều dịch vụ</small>
                                                </div>
                                                <div class="form-group col-half">
                                                    <label><i class="fas fa-book-medical"></i> Hướng dẫn liệu trình / Lời dặn chung:</label>
                                                    <textarea name="instructions" rows="4" class="form-control" placeholder="Ví dụ: Uống thuốc sau ăn 30 phút, kiêng đồ ngọt, tái khám sau 1 tháng..."></textarea>
                                                </div>
                                            </div>

                                            <div class="prescription-section">
                                                <h4 class="section-subtitle"><i class="fas fa-pills"></i> Kê đơn thuốc điều trị</h4>
                                                <div class="medicines-grid">
                                                    <c:forEach var="m" items="${medicines}">
                                                        <div class="medicine-item">
                                                            <input type="hidden" name="medicineIds" value="${m.medicineId}">
                                                            <div class="med-info">
                                                                <span class="med-name">${m.medicineName}</span>
                                                                <div class="med-meta">
                                                                    <span class="stock">Kho: ${m.stockQuantity}</span>
                                                                    <span class="price"><fmt:formatNumber value="${m.price}" type="number" /> đ/viên</span>
                                                                </div>
                                                            </div>
                                                            <div class="med-action">
                                                                <label>Số lượng:</label>
                                                                <input type="number" name="quantities" class="form-control qty-input" min="0" max="${m.stockQuantity}" value="0">
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                                <c:if test="${empty medicines}">
                                                    <div class="alert alert-warning">
                                                        <i class="fas fa-exclamation-triangle"></i> Kho thuốc hiện đang trống. Cần liên hệ quản trị viên cập nhật danh mục thuốc.
                                                    </div>
                                                </c:if>
                                            </div>

                                            <div class="form-actions">
                                                <button type="submit" class="btn-submit"><i class="fas fa-check-circle"></i> Hoàn Thành & Lưu Hồ Sơ Medical</button>
                                            </div>
                                        </form>
                                    </div>
                                </section>
                            </c:if>

                            <c:if test="${param.form == '2' && not empty param.appointmentId}">
                                <section class="result-section fade-in">
                                    <div class="data-card result-card">
                                        <div class="card-header">
                                            <h3><i class="fas fa-clipboard-check"></i> Chi tiết hồ sơ bệnh án</h3>
                                            <span class="badge-id">Mã lịch hẹn: #${param.appointmentId}</span>
                                        </div>
                                        <div class="result-content">
                                            ${appointmentResult}
                                        </div>
                                    </div>
                                </section>
                            </c:if>
                        </main>
                    </div>
                </body>

                </html>