package com.dentalclinic.dao;

import com.dentalclinic.model.User;
import com.dentalclinic.model.Role;
import com.dentalclinic.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setGender(rs.getString("gender"));
        user.setDob(rs.getDate("dob"));
        user.setAddress(rs.getString("address"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        Integer roleId = rs.getObject("role_id") != null ? rs.getInt("role_id") : null;
        user.setRoleId(roleId);
        if (roleId != null && hasColumn(rs, "role_name")) {
            Role role = new Role();
            role.setRoleId(roleId);
            role.setRoleName(rs.getString("role_name"));
            user.setRole(role);
        }
        return user;
    }

    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u "
                + "LEFT JOIN roles r ON u.role_id = r.role_id "
                + "ORDER BY u.user_id ASC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public User login(String email, String password) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u "
                + "LEFT JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.email = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedHash = rs.getString("password");
                if (com.dentalclinic.utils.PasswordUtils.checkPassword(password, storedHash)) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countDoctors() {
        String sql = "SELECT COUNT(*) FROM users u "
                + "JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'DOCTOR'";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getUsersByRole(String roleName) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = ? ORDER BY u.full_name";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getDoctors() {
        return getUsersByRole("DOCTOR");
    }

    public List<User> searchDoctors(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'DOCTOR' AND u.full_name LIKE ? ORDER BY u.full_name";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<User> getCustomers() {
        return getUsersByRole("CUSTOMER");
    }

    public User getCustomerByName(String fullName) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u JOIN roles r ON u.role_id = r.role_id "
                + "WHERE r.role_name = 'CUSTOMER' AND LOWER(u.full_name) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int id) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addUser(User user, Integer roleId) {
        String sql = "INSERT INTO users (full_name, email, password, phone, role_id, gender, dob, address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            if (roleId == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, roleId);
            }
            ps.setString(6, user.getGender());
            ps.setDate(7, user.getDob());
            ps.setString(8, user.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int addUserReturnId(User user, Integer roleId) {
        String sql = "INSERT INTO users (full_name, email, password, phone, role_id, gender, dob, address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            if (roleId == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, roleId);
            }
            ps.setString(6, user.getGender());
            ps.setDate(7, user.getDob());
            ps.setString(8, user.getAddress());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateUser(User user, Integer roleId) {
        String sql = "UPDATE users SET full_name = ?, email = ?, password = ?, phone = ?, role_id = ?, gender = ?, dob = ?, address = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            if (roleId == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, roleId);
            }
            ps.setString(6, user.getGender());
            ps.setDate(7, user.getDob());
            ps.setString(8, user.getAddress());
            ps.setInt(9, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT u.user_id, u.full_name, u.email, u.password, u.phone, u.gender, u.dob, u.address, u.created_at, u.role_id, r.role_name "
                + "FROM users u LEFT JOIN roles r ON u.role_id = r.role_id "
                + "WHERE u.email = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePassword(int userId, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
