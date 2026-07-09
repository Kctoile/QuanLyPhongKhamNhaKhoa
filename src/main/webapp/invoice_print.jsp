<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>In Hóa Đơn - ${appointment.appointmentId}</title>
    <style>
        body {
            font-family: "Segoe UI", Arial, sans-serif;
            color: #333;
            line-height: 1.4;
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
            margin-bottom: 30px;
            font-size: 14px;
        }
        .meta-group p {
            margin: 6px 0;
        }
        .invoice-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }
        .invoice-table th, .invoice-table td {
            border: 1px solid #ddd;
            padding: 10px 12px;
            text-align: left;
        }
        .invoice-table th {
            background-color: #f2f2f2;
            font-weight: 600;
        }
        .text-right {
            text-align: right;
        }
        .text-center {
            text-align: center;
        }
        .total-section {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            margin-bottom: 40px;
            font-size: 16px;
        }
        .total-row {
            display: flex;
            justify-content: space-between;
            width: 300px;
            margin: 5px 0;
            padding: 5px 0;
        }
        .total-row.grand-total {
            border-top: 2px solid #333;
            font-weight: bold;
            font-size: 18px;
            color: #1a2a4a;
            padding-top: 10px;
        }
        .signature-section {
            display: flex;
            justify-content: space-between;
            margin-top: 50px;
            page-break-inside: avoid;
        }
        .signature-block {
            text-align: center;
            width: 200px;
        }
        .signature-space {
            height: 80px;
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
        <button onclick="window.print();" class="btn btn-print">In hóa đơn</button>
    </div>

    <div class="print-container">
        <div class="header-print">
            <h1>Dental Clinic Center</h1>
            <div class="clinic-info">Địa chỉ: 123 Đường Ba Tháng Hai, Quận 10, TP. Hồ Chí Minh</div>
            <div class="clinic-info">Hotline: 1900 1234 · Email: contact@dentalclinic.com</div>
            <h2>HÓA ĐƠN THANH TOÁN</h2>
        </div>

        <div class="meta-info">
            <div class="meta-group">
                <p><strong>Mã hóa đơn:</strong> HD-${appointment.appointmentId}</p>
                <p><strong>Ngày in:</strong> <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm" /></p>
                <p><strong>Ngày khám:</strong> <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy" /></p>
            </div>
            <div class="meta-group">
                <p><strong>Khách hàng:</strong> ${appointment.patient.fullName}</p>
                <p><strong>Bác sĩ phụ trách:</strong> BS. ${appointment.doctor.fullName}</p>
                <p><strong>Phòng khám:</strong> ${appointment.room}</p>
            </div>
        </div>

        <table class="invoice-table">
            <thead>
                <tr>
                    <th style="width: 8%;" class="text-center">STT</th>
                    <th style="width: 15%;">Loại</th>
                    <th>Nội dung chi tiết</th>
                    <th style="width: 10%;" class="text-center">SL</th>
                    <th style="width: 18%;" class="text-right">Đơn giá</th>
                    <th style="width: 18%;" class="text-right">Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${items}" varStatus="status">
                    <tr>
                        <td class="text-center">${status.index + 1}</td>
                        <td>${item.itemType}</td>
                        <td>${item.itemName}</td>
                        <td class="text-center">${item.quantity}</td>
                        <td class="text-right">
                            <fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="" maxFractionDigits="0" /> ₫
                        </td>
                        <td class="text-right">
                            <fmt:formatNumber value="${item.total}" type="currency" currencySymbol="" maxFractionDigits="0" /> ₫
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty items}">
                    <tr>
                        <td colspan="6" class="text-center">Không có khoản phí thanh toán nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <div class="total-section">
            <div class="total-row">
                <span>Phương thức:</span>
                <strong>${payment != null ? payment.methodLabel : 'Chưa chọn'}</strong>
            </div>
            <div class="total-row">
                <span>Trạng thái:</span>
                <strong>${payment != null ? payment.statusLabel : 'Chưa thanh toán'}</strong>
            </div>
            <div class="total-row grand-total">
                <span>Tổng tiền:</span>
                <span><fmt:formatNumber value="${amount}" type="currency" currencySymbol="" maxFractionDigits="0" /> ₫</span>
            </div>
        </div>

        <div class="signature-section">
            <div class="signature-block">
                <p><strong>Khách hàng</strong></p>
                <p style="font-size: 12px; color: #777;">(Ký và ghi rõ họ tên)</p>
                <div class="signature-space"></div>
            </div>
            <div class="signature-block">
                <p><strong>Người lập phiếu</strong></p>
                <p style="font-size: 12px; color: #777;">(Ký và ghi rõ họ tên)</p>
                <div class="signature-space"></div>
            </div>
        </div>
    </div>

</body>
</html>
