<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>In Đơn Thuốc - ${appointment.appointmentId}</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            color: #333;
            line-height: 1.5;
            margin: 0;
            padding: 20px;
            background-color: #f9f9f9;
        }
        .print-container {
            max-width: 800px;
            margin: 0 auto;
            background: #fff;
            padding: 40px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            border-radius: 8px;
        }
        .header-print {
            text-align: center;
            border-bottom: 2px solid #333;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .header-print h1 {
            font-size: 24px;
            margin: 0 0 5px 0;
            color: #1a2a4a;
            text-transform: uppercase;
            letter-spacing: 1px;
        }
        .header-print .clinic-info {
            font-size: 13px;
            color: #666;
            margin: 5px 0;
        }
        .header-print h2 {
            font-size: 20px;
            margin: 15px 0 0 0;
            color: #333;
            letter-spacing: 0.5px;
        }
        .meta-info {
            display: flex;
            justify-content: space-between;
            margin-bottom: 25px;
            font-size: 14px;
        }
        .meta-group p {
            margin: 6px 0;
        }
        .diagnosis-card {
            border: 1px solid #ddd;
            background-color: #fafafa;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 25px;
        }
        .diagnosis-card p {
            margin: 5px 0;
        }
        .prescription-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 25px;
        }
        .prescription-table th, .prescription-table td {
            border: 1px solid #ddd;
            padding: 10px 12px;
            text-align: left;
        }
        .prescription-table th {
            background-color: #f2f2f2;
            font-weight: 600;
        }
        .text-center {
            text-align: center;
        }
        .doctor-notes-section {
            margin-bottom: 40px;
            border-top: 1px dashed #ccc;
            padding-top: 15px;
        }
        .doctor-notes-section h3 {
            font-size: 15px;
            margin: 0 0 8px 0;
            color: #1a2a4a;
        }
        .signature-section {
            display: flex;
            justify-content: flex-end;
            margin-top: 50px;
            page-break-inside: avoid;
        }
        .signature-block {
            text-align: center;
            width: 250px;
        }
        .signature-space {
            height: 100px;
        }
        .no-print-actions {
            max-width: 800px;
            margin: 20px auto;
            display: flex;
            justify-content: space-between;
        }
        .btn {
            display: inline-block;
            padding: 10px 20px;
            font-size: 14px;
            font-weight: 600;
            text-decoration: none;
            border-radius: 4px;
            cursor: pointer;
            border: none;
            transition: all 0.2s;
        }
        .btn-print {
            background-color: #1a2a4a;
            color: #fff;
        }
        .btn-print:hover {
            background-color: #2c426f;
        }
        .btn-back {
            background-color: #e0e0e0;
            color: #333;
        }
        .btn-back:hover {
            background-color: #d5d5d5;
        }

        @media print {
            body {
                background-color: #fff;
                padding: 0;
                font-size: 12pt;
            }
            .print-container {
                box-shadow: none;
                padding: 0;
                margin: 0;
                max-width: 100%;
            }
            .no-print-actions {
                display: none !important;
            }
        }
    </style>
</head>
<body>

    <div class="no-print-actions">
        <button onclick="window.history.back();" class="btn btn-back">Quay lại</button>
        <button onclick="window.print();" class="btn btn-print">In đơn thuốc</button>
    </div>

    <div class="print-container">
        <div class="header-print">
            <h1>Dental Clinic Center</h1>
            <div class="clinic-info">Địa chỉ: 123 Đường Ba Tháng Hai, Quận 10, TP. Hồ Chí Minh</div>
            <div class="clinic-info">Hotline: 1900 1234 · Email: contact@dentalclinic.com</div>
            <h2>ĐƠN THUỐC</h2>
        </div>

        <div class="meta-info">
            <div class="meta-group">
                <p><strong>Bệnh nhân:</strong> ${appointment.patient.fullName}</p>
                <p><strong>Ngày sinh / Tuổi:</strong> <fmt:formatDate value="${appointment.patient.dob}" pattern="dd/MM/yyyy" /></p>
                <p><strong>Giới tính:</strong> ${appointment.patient.gender}</p>
            </div>
            <div class="meta-group">
                <p><strong>Mã đơn thuốc:</strong> DT-${appointment.appointmentId}</p>
                <p><strong>Ngày kê đơn:</strong> <fmt:formatDate value="${examinationResult.examinationDate}" pattern="dd/MM/yyyy" /></p>
                <p><strong>Bác sĩ kê đơn:</strong> BS. ${appointment.doctor.fullName}</p>
            </div>
        </div>

        <div class="diagnosis-card">
            <p><strong>Chẩn đoán bệnh:</strong></p>
            <p style="margin-top: 5px; color: #333;">${examinationResult.resultDetails}</p>
        </div>

        <h3 style="font-size: 16px; border-bottom: 1px solid #333; padding-bottom: 5px; margin-bottom: 15px;">Chỉ định dùng thuốc</h3>
        <table class="prescription-table">
            <thead>
                <tr>
                    <th style="width: 8%;" class="text-center">STT</th>
                    <th>Tên thuốc</th>
                    <th style="width: 15%;" class="text-center">Số lượng</th>
                    <th>Hướng dẫn sử dụng</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${details}" varStatus="status">
                    <tr>
                        <td class="text-center">${status.index + 1}</td>
                        <td><strong>${item.medicine.medicineName}</strong></td>
                        <td class="text-center">${item.prescribedQuantity}</td>
                        <td>${prescription.instructions}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty details}">
                    <tr>
                        <td colspan="4" class="text-center">Chưa có thông tin kê đơn thuốc chi tiết.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <c:if test="${not empty examinationResult.doctorNotes}">
            <div class="doctor-notes-section">
                <h3>Lời dặn bác sĩ:</h3>
                <p style="margin: 0; color: #555;">${examinationResult.doctorNotes}</p>
            </div>
        </c:if>

        <div class="signature-section">
            <div class="signature-block">
                <p>Ngày <fmt:formatDate value="${examinationResult.examinationDate}" pattern="dd" /> tháng <fmt:formatDate value="${examinationResult.examinationDate}" pattern="MM" /> năm <fmt:formatDate value="${examinationResult.examinationDate}" pattern="yyyy" /></p>
                <p><strong>Bác sĩ điều trị</strong></p>
                <p style="font-size: 11px; color: #777;">(Ký, ghi rõ họ tên)</p>
                <div class="signature-space"></div>
                <p><strong>BS. ${appointment.doctor.fullName}</strong></p>
            </div>
        </div>
    </div>

</body>
</html>
