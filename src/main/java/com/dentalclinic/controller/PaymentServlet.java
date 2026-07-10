package com.dentalclinic.controller;

import com.dentalclinic.dao.AppointmentDAO;
import com.dentalclinic.dao.PaymentDAO;
import com.dentalclinic.model.Appointment;
import com.dentalclinic.model.Payment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private static final String METHOD_CASH = "CASH";
    private static final String METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    private static final String METHOD_CREDIT_CARD = "CREDIT_CARD";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_PAID = "PAID";
    private static final String STATUS_FAILED = "FAILED";

    private final transient AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final transient PaymentDAO paymentDAO = new PaymentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Appointment appointment = getAllowedAppointment(request, response, session);
        if (appointment == null) {
            return;
        }

        forwardPaymentPage(request, response, session, appointment);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Appointment appointment = getAllowedAppointment(request, response, session);
        if (appointment == null) {
            return;
        }

        String role = getRole(session);
        boolean canManagePayments = canManagePayments(role);
        String action = request.getParameter("action");
        Payment currentPayment = paymentDAO.getByAppointmentId(appointment.getAppointmentId());

        if ("confirm_bank_transfer".equals(action)) {
            if (!canManagePayments) {
                session.setAttribute("error", "Bạn không có quyền xác nhận thanh toán.");
                response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
                return;
            }
            paymentDAO.markStatus(appointment.getAppointmentId(), STATUS_PAID, "Đã nhận chuyển khoản");
            session.setAttribute("success", "Đã xác nhận thanh toán chuyển khoản.");
            response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
            return;
        }

        if ("mark_failed".equals(action)) {
            if (!canManagePayments) {
                session.setAttribute("error", "Bạn không có quyền cập nhật thanh toán.");
                response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
                return;
            }
            if (currentPayment != null && currentPayment.isPaid()) {
                session.setAttribute("error", "Giao dịch đã thanh toán không thể chuyển sang thất bại.");
                response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
                return;
            }
            paymentDAO.markStatus(appointment.getAppointmentId(), STATUS_FAILED, "Giao dịch không thành công");
            session.setAttribute("success", "Đã cập nhật giao dịch thất bại.");
            response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
            return;
        }

        if (currentPayment != null && currentPayment.isPaid()) {
            session.setAttribute("success", "Lịch hẹn này đã thanh toán.");
            response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
            return;
        }

        String method = normalizeMethod(request.getParameter("method"));
        if (method == null) {
            request.setAttribute("error", "Vui lòng chọn phương thức thanh toán hợp lệ.");
            forwardPaymentPage(request, response, session, appointment);
            return;
        }

        if (!canManagePayments && !METHOD_BANK_TRANSFER.equals(method)) {
            request.setAttribute("error", "Vui lòng liên hệ lễ tân để thanh toán tiền mặt hoặc thẻ.");
            forwardPaymentPage(request, response, session, appointment);
            return;
        }

        Payment payment = buildPayment(request, appointment, method, canManagePayments);
        if (payment == null) {
            forwardPaymentPage(request, response, session, appointment);
            return;
        }

        Payment savedPayment = paymentDAO.saveOrUpdate(payment);
        if (savedPayment == null) {
            request.setAttribute("error", "Không thể tạo giao dịch thanh toán. Vui lòng thử lại.");
            forwardPaymentPage(request, response, session, appointment);
            return;
        }

        if (METHOD_BANK_TRANSFER.equals(method)) {
            session.setAttribute("success", "Đã tạo yêu cầu chuyển khoản. Vui lòng quét QR và chờ xác nhận.");
        } else {
            session.setAttribute("success", "Đã ghi nhận thanh toán thành công.");
        }
        response.sendRedirect("payment?appointmentId=" + appointment.getAppointmentId());
    }

    private Appointment getAllowedAppointment(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        int appointmentId;
        try {
            appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        } catch (Exception e) {
            response.sendRedirect("appointments");
            return null;
        }

        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment == null) {
            response.sendRedirect("appointments");
            return null;
        }

        String role = getRole(session);
        Integer userId = (Integer) session.getAttribute("userId");
        boolean owner = appointment.getPatientId() != null && appointment.getPatientId().equals(userId);
        if (!canManagePayments(role) && !owner) {
            response.sendRedirect("appointments");
            return null;
        }
        return appointment;
    }

    private void forwardPaymentPage(HttpServletRequest request, HttpServletResponse response, HttpSession session,
            Appointment appointment) throws ServletException, IOException {
        BigDecimal amount = paymentDAO.calculateAppointmentAmount(appointment.getAppointmentId());
        Payment payment = paymentDAO.getByAppointmentId(appointment.getAppointmentId());
        String transferReference = payment != null && payment.getGatewayReference() != null
                ? payment.getGatewayReference()
                : buildTransferReference(appointment.getAppointmentId());

        request.setAttribute("appointment", appointment);
        request.setAttribute("payment", payment);
        request.setAttribute("items", paymentDAO.getPaymentItems(appointment.getAppointmentId()));
        request.setAttribute("amount", amount);
        request.setAttribute("canManagePayments", canManagePayments(getRole(session)));
        request.setAttribute("transferReference", transferReference);
        request.setAttribute("bankName", env("PAYMENT_BANK_LABEL", "VCB"));
        request.setAttribute("bankAccount", env("PAYMENT_BANK_ACCOUNT", "0123456789"));
        request.setAttribute("bankAccountName", env("PAYMENT_BANK_NAME", "PHONG KHAM NHA KHOA"));
        request.setAttribute("qrImageUrl", buildQrImageUrl(amount, transferReference));
        request.getRequestDispatcher("payment.jsp").forward(request, response);
    }

    private Payment buildPayment(HttpServletRequest request, Appointment appointment, String method, boolean canManagePayments) {
        BigDecimal amount = paymentDAO.calculateAppointmentAmount(appointment.getAppointmentId());
        Payment existing = paymentDAO.getByAppointmentId(appointment.getAppointmentId());
        String transactionCode = existing != null && existing.getTransactionCode() != null
                ? existing.getTransactionCode()
                : "PAY-" + appointment.getAppointmentId() + "-" + System.currentTimeMillis();
        String reference = buildTransferReference(appointment.getAppointmentId());

        Payment payment = new Payment();
        payment.setAppointmentId(appointment.getAppointmentId());
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setTransactionCode(transactionCode);
        payment.setGatewayReference(reference);
        payment.setQrContent(buildQrContent(amount, reference));
        payment.setNotes(request.getParameter("notes"));

        if (METHOD_BANK_TRANSFER.equals(method)) {
            payment.setStatus(STATUS_PENDING);
            return payment;
        }

        if (METHOD_CASH.equals(method)) {
            payment.setStatus(STATUS_PAID);
            payment.setPaidAt(new Timestamp(System.currentTimeMillis()));
            payment.setGatewayReference("CASH-" + appointment.getAppointmentId());
            return payment;
        }

        if (METHOD_CREDIT_CARD.equals(method)) {
            String cardBrand = request.getParameter("cardBrand");
            String cardLast4 = request.getParameter("cardLast4");
            if (!canManagePayments || cardBrand == null || cardBrand.isBlank()
                    || cardLast4 == null || !cardLast4.matches("\\d{4}")) {
                request.setAttribute("error", "Vui lòng nhập hãng thẻ và 4 số cuối hợp lệ.");
                return null;
            }
            payment.setStatus(STATUS_PAID);
            payment.setPaidAt(new Timestamp(System.currentTimeMillis()));
            payment.setCardBrand(cardBrand);
            payment.setCardLast4(cardLast4);
            payment.setGatewayReference("CARD-" + appointment.getAppointmentId() + "-" + System.currentTimeMillis());
            return payment;
        }

        return null;
    }

    private String normalizeMethod(String method) {
        if (METHOD_CASH.equals(method) || METHOD_BANK_TRANSFER.equals(method) || METHOD_CREDIT_CARD.equals(method)) {
            return method;
        }
        return null;
    }

    private String getRole(HttpSession session) {
        Object role = session.getAttribute("role");
        return role == null ? "" : role.toString().trim().toUpperCase();
    }

    private boolean canManagePayments(String role) {
        return "STAFF".equals(role) || "ADMIN".equals(role);
    }

    private String buildTransferReference(int appointmentId) {
        return "DENTAL-" + appointmentId;
    }

    private String buildQrContent(BigDecimal amount, String reference) {
        return "Chuyen khoan " + amount.setScale(0, RoundingMode.HALF_UP).toPlainString() + " VND - " + reference;
    }

    private String buildQrImageUrl(BigDecimal amount, String reference) {
        String bankBin = env("PAYMENT_BANK_BIN", "970436");
        String bankAccount = env("PAYMENT_BANK_ACCOUNT", "0123456789");
        String accountName = env("PAYMENT_BANK_NAME", "PHONG KHAM NHA KHOA");
        String roundedAmount = amount.setScale(0, RoundingMode.HALF_UP).toPlainString();
        String encodedReference = URLEncoder.encode(reference, StandardCharsets.UTF_8);
        String encodedName = URLEncoder.encode(accountName, StandardCharsets.UTF_8);
        return "https://img.vietqr.io/image/" + bankBin + "-" + bankAccount
                + "-compact2.png?amount=" + roundedAmount
                + "&addInfo=" + encodedReference
                + "&accountName=" + encodedName;
    }

    private String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
