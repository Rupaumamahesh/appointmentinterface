// In: dao/UserDAO.java
package com.medibook.hospital.appointmentinterface.dao;

// --- FIX 1: Import the User class ---
import com.medibook.hospital.appointmentinterface.dao.User; // Assuming User.java is in the 'dao' package
import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User validateLogin(String username, String password, String role) {
        String sql = "SELECT id, role FROM users WHERE username = ? AND password = ? AND role = ?";

        // --- FIX 3: Added closing parenthesis to the 'try' statement ---
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // --- FIX 2: Corrected the setString syntax ---
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Login successful, return a User object with the ID and role
                return new User(rs.getInt("id"), rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Login failed, return null
        return null;
    }
    // --- ADD THIS NEW METHOD ---
    // In: dao/UserDAO.java

    /**
     * Changes a user's password after verifying their current password.
     * @param userId The ID of the user.
     * @param currentPassword The current password provided by the user.
     * @param newPassword The new password to set.
     * @return true if the password was successfully changed, false otherwise.
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        // Step 1: Verify the current password is correct.
        String verifySql = "SELECT password FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement verifyPstmt = conn.prepareStatement(verifySql)) {

            verifyPstmt.setInt(1, userId);
            ResultSet rs = verifyPstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                // In a real app with hashing, you'd use a bcrypt.check() method here.
                // For now, we do a simple string comparison.
                if (!dbPassword.equals(currentPassword)) {
                    System.out.println("Password verification failed: Current password does not match.");
                    return false; // Current password was incorrect
                }
            } else {
                return false; // User not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Step 2: If verification passed, update to the new password.
        String updateSql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

            // In a real app, you would HASH the newPassword here before saving.
            updatePstmt.setString(1, newPassword);
            updatePstmt.setInt(2, userId);

            return updatePstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}