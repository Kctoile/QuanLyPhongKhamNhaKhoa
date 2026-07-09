package com.dentalclinic.dao;

import com.dentalclinic.model.PasswordResetToken;
import com.dentalclinic.utils.DBConnection;
import java.sql.*;

public class PasswordResetTokenDAO {

    public boolean createToken(int userId, String token, Timestamp expiry) {
        String sql = "INSERT INTO password_reset_tokens (user_id, token, expiry) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, token);
            ps.setTimestamp(3, expiry);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PasswordResetToken getValidToken(String token) {
        String sql = "SELECT * FROM password_reset_tokens "
                + "WHERE token = ? AND is_used = FALSE AND expiry > CURRENT_TIMESTAMP";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PasswordResetToken prt = new PasswordResetToken();
                prt.setTokenId(rs.getInt("token_id"));
                prt.setUserId(rs.getInt("user_id"));
                prt.setToken(rs.getString("token"));
                prt.setExpiry(rs.getTimestamp("expiry"));
                prt.setUsed(rs.getBoolean("is_used"));
                prt.setCreatedAt(rs.getTimestamp("created_at"));
                return prt;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean markTokenUsed(String token) {
        String sql = "UPDATE password_reset_tokens SET is_used = TRUE WHERE token = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteExpiredTokens() {
        String sql = "DELETE FROM password_reset_tokens WHERE expiry < CURRENT_TIMESTAMP";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
