<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ và Toa thuốc</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .result-container {
            background: white;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.08);
            max-width: 800px;
            margin: 40px auto;
            font-family: Arial, sans-serif;
            border-top: 5px solid #0ea5e9;
        }
        .result-header { border-bottom: 2px solid #f1f5f9; padding-bottom: 15px; margin-bottom: 25px; }
        .result-header h2 { margin: 0; color: #1e293b; font-size: 24px; text-align: center; }
        .info-row { display: flex; justify-content: space-between; margin-bottom: 12px; color: #475569; font-size: 15px; }
        .section-title { font-size: 18px; color: #0f172a; font-weight: bold; margin-top: 30px; margin-bottom: 15px; display: flex; align-items: center; gap: 8px; border-left: 4px solid #0ea5e9; padding-left: 10px;}
        .card-box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 20px; line-height: 1.6; color: #334155; }
        .btn-back { display: inline-block; margin-top: 25px; padding: 12px 24px; background: #e2e8f0; color: #334155; text-decoration: none; border-radius: 6px; font-weight: bold; transition: 0.2s;}
        .btn-back:hover { background: #cbd5e1; }
    </style>
</head>
<body style="background: #f1f5f9; margin: 0;">
    <div class="page-wrapper">
        <div class="nav" style="background:#fff; padding:15px; border-bottom:1px solid #ddd; text-align:right; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
            <a href="${pageContext.request.contextPath}/" style="margin-right:20px; text-decoration:none; color:#333; font-weight:bold;">Trang chủ</a>
            <a href="appointments" style="margin-right:20px; text-decoration:none; color:#333; font-weight:bold;">Quản lý Lịch hẹn</a>
            <a href="logout" style="text-decoration:none; color:#ef4444; font-weight:bold;">Đăng xuất</a>
        </div>

        <div class="result-container">
            <div class="result-header">
                <h2><i class="fas fa-file-medical-alt"></i> CHI TIẾT HỒ SƠ BỆNH ÁN</h2>
            </div>
            
            <div class="info-row">
                <span><strong>Mã lịch hẹn:</strong> #${appt.appointmentId}</span>
                <span><strong>Ngày khám:</strong> <fmt:formatDate value="${appt.appointmentDate}" pattern="dd/MM/yyyy" /> ${appt.appointmentTime}</span>
            </div>
            <div class="info-row">
                <span><strong>Khách hàng:</strong> ${appt.patient.fullName}</span>
                <span><strong>Bác sĩ khám:</strong> BS. ${appt.doctor.fullName}</span>
            </div>

            <c:if test="${not empty result}">
                <div class="section-title">1. Kết quả chẩn đoán</div>
                <div class="card-box">
                    ${result.resultDetails}
                </div>

                <div class="section-title">2. Đơn thuốc & Lời dặn</div>
                <div class="card-box" style="white-space: pre-line;">
<c:if test="${not empty result.prescription}">
${result.prescription}
</c:if>
<c:if test="${empty result.prescription}">
<span style="color: #94a3b8; font-style: italic;">Không có kê đơn thuốc trong lần khám này.</span>
</c:if>

<c:if test="${not empty result.doctorNotes}">
<br><hr style="border: 0; border-top: 1px dashed #cbd5e1; margin: 15px 0;"><strong>Ghi chú của bác sĩ:</strong>
${result.doctorNotes}
</c:if>
                </div>
            </c:if>
            
            <c:if test="${empty result}">
                <div class="card-box" style="text-align: center; color: #94a3b8; padding: 40px; font-style: italic;">
                    <i class="fas fa-box-open" style="font-size: 30px; margin-bottom: 10px;"></i><br>
                    Hồ sơ bệnh án chưa được cập nhật chi tiết cho lịch hẹn này.
                </div>
            </c:if>

            <div style="text-align: center;">
                <a href="appointments" class="btn-back"><i class="fas fa-arrow-left"></i> Quay lại danh sách lịch khám</a>
            </div>
        </div>
    </div>
</body>
</html>
