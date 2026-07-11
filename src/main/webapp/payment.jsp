<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // === KIỂM TRA ĐĂNG NHẬP ===
    jakarta.servlet.http.HttpSession s = request.getSession(false);
    if (s == null || s.getAttribute("role") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh toán lịch hẹn</title>
        <link rel="stylesheet" href="css/global.css">
        <link rel="stylesheet" href="css/payment.css">
    </head>
    <body>
        <main class="payment-page">
            <section class="payment-shell">
                <header class="payment-header">
                    <div>
                        <p class="eyebrow">Payment Gateway</p>
                        <h1>Thanh toán lịch hẹn #${appointment.appointmentId}</h1>
                        <p class="subtle">
                            ${appointment.patient != null ? appointment.patient.fullName : 'Khách hàng'}
                            · <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy" />
                            · ${appointment.appointmentTime}
                        </p>
                    </div>
                    <nav class="payment-nav">
                        <c:if test="${payment != null && payment.paid}">
                            <a href="${pageContext.request.contextPath}/invoice-print?appointmentId=${appointment.appointmentId}" style="background-color: #1a2a4a; color: #fff; padding: 6px 12px; border-radius: 4px; font-weight: bold; text-decoration: none; margin-right: 10px;">
                                In hóa đơn
                            </a>
                        </c:if>
                        <a href="${canManagePayments ? 'staff' : 'appointments'}">Quay lại</a>
                        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
                    </nav>
                </header>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-error">${sessionScope.error}</div>
                    <c:remove var="error" scope="session" />
                </c:if>
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success">${sessionScope.success}</div>
                    <c:remove var="success" scope="session" />
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <div class="payment-grid">
                    <section class="invoice-panel">
                        <div class="section-heading">
                            <h2>Chi tiết hóa đơn</h2>
                            <span class="status-pill ${payment != null ? payment.status : 'UNPAID'}">
                                ${payment != null ? payment.statusLabel : 'Chưa thanh toán'}
                            </span>
                        </div>

                        <table class="invoice-table">
                            <thead>
                                <tr>
                                    <th>Loại</th>
                                    <th>Nội dung</th>
                                    <th>SL</th>
                                    <th>Đơn giá</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${items}">
                                    <tr>
                                        <td>${item.itemType}</td>
                                        <td>${item.itemName}</td>
                                        <td>${item.quantity}</td>
                                        <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="₫" /></td>
                                        <td><fmt:formatNumber value="${item.total}" type="currency" currencySymbol="₫" /></td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty items}">
                                    <tr>
                                        <td colspan="5" class="empty-row">Chưa có dịch vụ hoặc thuốc để thanh toán.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>

                        <div class="total-row">
                            <span>Tổng cần thu</span>
                            <strong><fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫" /></strong>
                        </div>

                        <c:if test="${payment != null}">
                            <div class="transaction-box">
                                <div>
                                    <span>Mã giao dịch</span>
                                    <strong>${payment.transactionCode}</strong>
                                </div>
                                <div>
                                    <span>Phương thức</span>
                                    <strong>${payment.methodLabel}</strong>
                                </div>
                                <c:if test="${not empty payment.cardBrand}">
                                    <div>
                                        <span>Thẻ</span>
                                        <strong>${payment.cardBrand} · **** ${payment.cardLast4}</strong>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </section>

                    <section class="gateway-panel">
                        <div class="section-heading">
                            <h2>Phương thức thanh toán</h2>
                        </div>

                        <div class="method-card">
                            <div class="method-title">
                                <span>Chuyển khoản QR</span>
                                <c:if test="${payment != null && payment.method == 'BANK_TRANSFER'}">
                                    <em>${payment.statusLabel}</em>
                                </c:if>
                            </div>
                            <div class="qr-wrap">
                                <img src="${qrImageUrl}" alt="Mã QR chuyển khoản">
                                <div class="bank-info">
                                    <p><strong>Ngân hàng:</strong> ${bankName}</p>
                                    <p><strong>Số tài khoản:</strong> ${bankAccount}</p>
                                    <p><strong>Chủ tài khoản:</strong> ${bankAccountName}</p>
                                    <p><strong>Nội dung:</strong> ${transferReference}</p>
                                </div>
                            </div>
                            <form method="post" action="payment" class="method-form">
                                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                <input type="hidden" name="action" value="pay">
                                <input type="hidden" name="method" value="BANK_TRANSFER">
                                <textarea id="notesBankTransfer" name="notes" rows="2" placeholder="Ghi chú chuyển khoản nếu có"></textarea>
                                <button type="submit" class="btn btn-outline">Tạo yêu cầu chuyển khoản</button>
                            </form>
                            <c:if test="${canManagePayments && payment != null && payment.method == 'BANK_TRANSFER' && payment.status == 'PENDING'}">
                                <div class="confirm-row">
                                    <form method="post" action="payment">
                                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                        <input type="hidden" name="action" value="confirm_bank_transfer">
                                        <button type="submit" class="btn btn-primary">Xác nhận đã nhận tiền</button>
                                    </form>
                                    <form method="post" action="payment">
                                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                        <input type="hidden" name="action" value="mark_failed">
                                        <button type="submit" class="btn btn-danger">Báo thất bại</button>
                                    </form>
                                </div>
                            </c:if>
                        </div>

                        <c:if test="${canManagePayments}">
                            <div class="method-card compact">
                                <div class="method-title">
                                    <span>Tiền mặt</span>
                                </div>
                                <form method="post" action="payment" class="method-form inline">
                                    <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                    <input type="hidden" name="action" value="pay">
                                    <input type="hidden" name="method" value="CASH">
                                    <input id="notesCash" type="text" name="notes" placeholder="Ghi chú thu ngân">
                                    <button type="submit" class="btn btn-primary">Ghi nhận tiền mặt</button>
                                </form>
                            </div>

                            <div class="method-card compact">
                                <div class="method-title">
                                    <span>Thẻ tín dụng</span>
                                </div>
                                <form method="post" action="payment" class="card-form">
                                    <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                                    <input type="hidden" name="action" value="pay">
                                    <input type="hidden" name="method" value="CREDIT_CARD">
                                    <select id="cardBrand" name="cardBrand" required>
                                        <option value="">Loại thẻ</option>
                                        <option value="VISA">VISA</option>
                                        <option value="MASTERCARD">Mastercard</option>
                                        <option value="JCB">JCB</option>
                                        <option value="AMEX">American Express</option>
                                        <option value="NAPAS">NAPAS</option>
                                    </select>
                                    <input id="cardLast4" type="text" name="cardLast4" maxlength="4" pattern="[0-9]{4}" placeholder="4 số cuối" required>
                                    <input id="notesCard" type="text" name="notes" placeholder="Mã POS hoặc ghi chú">
                                    <button type="submit" class="btn btn-primary">Xác nhận thẻ</button>
                                </form>
                            </div>
                        </c:if>
                    </section>
                </div>
            </section>
        </main>
    </body>
</html>
