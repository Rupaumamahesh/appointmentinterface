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
}