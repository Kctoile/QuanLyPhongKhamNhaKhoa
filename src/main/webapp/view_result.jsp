<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.dentalclinic.model.Appointment"%>
<%@page import="com.dentalclinic.model.ExaminationResult"%>
<%
    Appointment appt = (Appointment) request.getAttribute("appointment");
    ExaminationResult er = (ExaminationResult) request.getAttribute("examinationResult");
    if (appt == null) {
        response.sendRedirect(request.getContextPath() + "/doctor");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Kết quả khám bệnh</title>
        <style>
            * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
                font-family: 'Segoe UI', sans-serif;
            }
            body {
                background: #f0f4f8;
                padding: 40px;
                display: flex;
                justify-content: center;
            }
            .card {
                background: white;
                border-radius: 12px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                padding: 32px;
                max-width: 700px;
                width: 100%;
            }
            h1 {
                font-size: 20px;
                color: #1a73e8;
                margin-bottom: 24px;
                border-bottom: 2px solid #e8f0fe;
                padding-bottom: 12px;
            }
            .info-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 12px;
                margin-bottom: 24px;
            }
            .info-item label {
                font-size: 12px;
                color: #888;
                display: block;
            }
            .info-item span {
                font-size: 15px;
                color: #333;
                font-weight: 500;
            }
            .section {
                margin-bottom: 20px;
            }
            .section h3 {
                font-size: 14px;
                color: #555;
                margin-bottom: 8px;
                font-weight: 600;
            }
            .section p {
                background: #f8fafc;
                padding: 14px;
                border-radius: 8px;
                font-size: 14px;
                color: #333;
                line-height: 1.6;
            }
            .empty-msg {
                text-align: center;
                padding: 40px;
                color: #999;
                font-size: 15px;
            }
            .back-btn {
                display: inline-block;
                padding: 8px 20px;
                background: #1a73e8;
                color: white;
                text-decoration: none;
                border-radius: 6px;
                font-size: 13px;
                margin-top: 16px;
            }
            .back-btn:hover {
                background: #1557b0;
            }
        </style>
    </head>
    <body>
        <div class="card">
            <h1>Kết quả khám bệnh</h1>
            <div class="info-grid">
                <div class="info-item"><label>Bệnh nhân</label><span><%= appt.getPatient() != null ? appt.getPatient().getFullName() : "N/A" %></span></div>
                <div class="info-item"><label>Bác sĩ</label><span><%= appt.getDoctor() != null ? appt.getDoctor().getFullName() : "N/A" %></span></div>
                <div class="info-item"><label>Ngày khám</label><span><%= appt.getAppointmentDate() != null ? appt.getAppointmentDate().toString() : "N/A" %></span></div>
                <div class="info-item"><label>Phòng</label><span><%= appt.getRoom() != null ? appt.getRoom() : "—" %></span></div>
            </div>

            <% if (er != null) { %>
            <div class="section">
                <h3>Chẩn đoán / Kết quả</h3>
                <p><%= er.getResultDetails() != null ? er.getResultDetails() : "—" %></p>
            </div>
            <div class="section">
                <h3>Đơn thuốc</h3>
                <p><%= er.getPrescription() != null ? er.getPrescription() : "—" %></p>
            </div>
            <div class="section">
                <h3>Ghi chú bác sĩ</h3>
                <p><%= er.getDoctorNotes() != null ? er.getDoctorNotes() : "—" %></p>
            </div>
            <% } else { %>
            <div class="empty-msg">Bệnh nhân này chưa có kết quả khám trong hệ thống.</div>
            <% } %>

            <a href="javascript:history.back()" class="back-btn">Quay lại</a>
        </div>
    </body>
</html>
