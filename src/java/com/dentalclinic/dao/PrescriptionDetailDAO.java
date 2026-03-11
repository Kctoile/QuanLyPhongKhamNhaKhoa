package com.dentalclinic.dao;

import com.dentalclinic.model.PrescriptionDetail;
import com.dentalclinic.model.Medicine;
import com.dentalclinic.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDetailDAO {

    public void addOrUpdateDetail(int prescriptionId, int medicineId, int quantity, double unitPrice) {
        String checkSql = "SELECT prescribed_quantity FROM prescription_details WHERE prescription_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            checkPs.setInt(1, prescriptionId);
            checkPs.setInt(2, medicineId);
            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                // Update quantity and price if it already exists
                int currentQty = rs.getInt("prescribed_quantity");
                String updateSql = "UPDATE prescription_details SET prescribed_quantity = ?, unit_price = ? WHERE prescription_id = ? AND medicine_id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setInt(1, quantity); // Can either add or set directly based on logic. Let's set directly.
                    updatePs.setDouble(2, unitPrice);
                    updatePs.setInt(3, prescriptionId);
                    updatePs.setInt(4, medicineId);
                    updatePs.executeUpdate();
                }
            } else {
                // Insert new
                String insertSql = "INSERT INTO prescription_details (prescription_id, medicine_id, prescribed_quantity, purchased_quantity, unit_price) "
                        + "VALUES (?, ?, ?, 0, ?)";
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setInt(1, prescriptionId);
                    insertPs.setInt(2, medicineId);
                    insertPs.setInt(3, quantity);
                    insertPs.setDouble(4, unitPrice);
                    insertPs.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PrescriptionDetail> getDetailsByPrescriptionId(int prescriptionId) {
        List<PrescriptionDetail> list = new ArrayList<>();
        String sql = "SELECT pd.*, m.medicine_name, m.price, m.stock_quantity "
                + "FROM prescription_details pd "
                + "JOIN medicines m ON pd.medicine_id = m.medicine_id "
                + "WHERE pd.prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PrescriptionDetail pd = new PrescriptionDetail();
                pd.setPrescriptionId(rs.getInt("prescription_id"));
                pd.setMedicineId(rs.getInt("medicine_id"));
                pd.setPrescribedQuantity(rs.getInt("prescribed_quantity"));
                pd.setPurchasedQuantity(rs.getInt("purchased_quantity"));
                pd.setUnitPrice(rs.getDouble("unit_price"));

                Medicine m = new Medicine();
                m.setMedicineId(rs.getInt("medicine_id"));
                m.setMedicineName(rs.getString("medicine_name"));
                m.setPrice(rs.getDouble("price"));
                m.setStockQuantity(rs.getInt("stock_quantity"));
                pd.setMedicine(m);

                list.add(pd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePurchasedQuantity(int prescriptionId, int medicineId, int quantityToBuy) {
        String sql = "UPDATE prescription_details SET purchased_quantity = purchased_quantity + ? "
                + "WHERE prescription_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityToBuy);
            ps.setInt(2, prescriptionId);
            ps.setInt(3, medicineId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
