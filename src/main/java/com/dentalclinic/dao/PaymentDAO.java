package com.dentalclinic.dao;

import com.dentalclinic.model.Payment;
import com.dentalclinic.model.PaymentItem;
import com.dentalclinic.utils.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public Payment getByAppointmentId(int appointmentId) {
        String sql = "SELECT * FROM payments WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapPayment(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment saveOrUpdate(Payment payment) {
        Payment existing = getByAppointmentId(payment.getAppointmentId());
        if (existing == null) {
            return insert(payment);
        }
        payment.setPaymentId(existing.getPaymentId());
        update(payment);
        return getByAppointmentId(payment.getAppointmentId());
    }

    public boolean markStatus(int appointmentId, String status, String notes) {
        String sql = "UPDATE payments SET status = ?, notes = ?, paid_at = ?, updated_at = CURRENT_TIMESTAMP "
                + "WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, notes);
            if ("PAID".equalsIgnoreCase(status)) {
                ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            } else {
                ps.setNull(3, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(4, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public BigDecimal calculateAppointmentAmount(int appointmentId) {
        BigDecimal serviceTotal = queryAmount(
                "SELECT COALESCE(SUM(s.price), 0) AS total "
                        + "FROM appointment_services aps "
                        + "JOIN services s ON aps.service_id = s.service_id "
                        + "WHERE aps.appointment_id = ?",
                appointmentId);

        BigDecimal medicineTotal = queryAmount(
                "SELECT COALESCE(SUM(CAST(pd.prescribed_quantity AS DECIMAL(12,2)) * pd.unit_price), 0) AS total "
                        + "FROM examination_results er "
                        + "JOIN prescriptions p ON p.result_id = er.result_id "
                        + "JOIN prescription_details pd ON pd.prescription_id = p.prescription_id "
                        + "WHERE er.appointment_id = ?",
                appointmentId);

        return serviceTotal.add(medicineTotal);
    }

    public List<PaymentItem> getPaymentItems(int appointmentId) {
        List<PaymentItem> items = new ArrayList<>();
        addServiceItems(items, appointmentId);
        addMedicineItems(items, appointmentId);
        return items;
    }

    private Payment insert(Payment payment) {
        String sql = "INSERT INTO payments (appointment_id, amount, method, status, transaction_code, "
                + "gateway_reference, card_brand, card_last4, qr_content, notes, paid_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bindPayment(ps, payment);
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setPaymentId(rs.getInt(1));
                    }
                }
                return payment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean update(Payment payment) {
        String sql = "UPDATE payments SET amount = ?, method = ?, status = ?, transaction_code = ?, "
                + "gateway_reference = ?, card_brand = ?, card_last4 = ?, qr_content = ?, notes = ?, "
                + "paid_at = ?, updated_at = CURRENT_TIMESTAMP WHERE appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, payment.getAmount());
            ps.setString(2, payment.getMethod());
            ps.setString(3, payment.getStatus());
            ps.setString(4, payment.getTransactionCode());
            ps.setString(5, payment.getGatewayReference());
            ps.setString(6, payment.getCardBrand());
            ps.setString(7, payment.getCardLast4());
            ps.setString(8, payment.getQrContent());
            ps.setString(9, payment.getNotes());
            setNullableTimestamp(ps, 10, payment.getPaidAt());
            ps.setInt(11, payment.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void bindPayment(PreparedStatement ps, Payment payment) throws Exception {
        ps.setInt(1, payment.getAppointmentId());
        ps.setBigDecimal(2, payment.getAmount());
        ps.setString(3, payment.getMethod());
        ps.setString(4, payment.getStatus());
        ps.setString(5, payment.getTransactionCode());
        ps.setString(6, payment.getGatewayReference());
        ps.setString(7, payment.getCardBrand());
        ps.setString(8, payment.getCardLast4());
        ps.setString(9, payment.getQrContent());
        ps.setString(10, payment.getNotes());
        setNullableTimestamp(ps, 11, payment.getPaidAt());
    }

    private void setNullableTimestamp(PreparedStatement ps, int index, Timestamp value) throws Exception {
        if (value == null) {
            ps.setNull(index, java.sql.Types.TIMESTAMP);
        } else {
            ps.setTimestamp(index, value);
        }
    }

    private Payment mapPayment(ResultSet rs) throws Exception {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setAppointmentId(rs.getInt("appointment_id"));
        payment.setAmount(rs.getBigDecimal("amount"));
        payment.setMethod(rs.getString("method"));
        payment.setStatus(rs.getString("status"));
        payment.setTransactionCode(rs.getString("transaction_code"));
        payment.setGatewayReference(rs.getString("gateway_reference"));
        payment.setCardBrand(rs.getString("card_brand"));
        payment.setCardLast4(rs.getString("card_last4"));
        payment.setQrContent(rs.getString("qr_content"));
        payment.setNotes(rs.getString("notes"));
        payment.setPaidAt(rs.getTimestamp("paid_at"));
        payment.setCreatedAt(rs.getTimestamp("created_at"));
        payment.setUpdatedAt(rs.getTimestamp("updated_at"));
        return payment;
    }

    private BigDecimal queryAmount(String sql, int appointmentId) {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal value = rs.getBigDecimal("total");
                    return value != null ? value : BigDecimal.ZERO;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private void addServiceItems(List<PaymentItem> items, int appointmentId) {
        String sql = "SELECT s.service_name, s.price "
                + "FROM appointment_services aps "
                + "JOIN services s ON aps.service_id = s.service_id "
                + "WHERE aps.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentItem item = new PaymentItem();
                    item.setItemType("Dịch vụ");
                    item.setItemName(rs.getString("service_name"));
                    item.setQuantity(1);
                    item.setUnitPrice(rs.getBigDecimal("price"));
                    item.setTotal(rs.getBigDecimal("price"));
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMedicineItems(List<PaymentItem> items, int appointmentId) {
        String sql = "SELECT m.medicine_name, pd.prescribed_quantity, pd.unit_price, "
                + "(CAST(pd.prescribed_quantity AS DECIMAL(12,2)) * pd.unit_price) AS total "
                + "FROM examination_results er "
                + "JOIN prescriptions p ON p.result_id = er.result_id "
                + "JOIN prescription_details pd ON pd.prescription_id = p.prescription_id "
                + "JOIN medicines m ON m.medicine_id = pd.medicine_id "
                + "WHERE er.appointment_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentItem item = new PaymentItem();
                    item.setItemType("Thuốc");
                    item.setItemName(rs.getString("medicine_name"));
                    item.setQuantity(rs.getInt("prescribed_quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setTotal(rs.getBigDecimal("total"));
                    items.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
