// In: dao/DoctorDAO.java
package com.medibook.hospital.appointmentinterface.dao;

import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import com.medibook.hospital.appointmentinterface.model.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

public class DoctorDAO {

    public Doctor getDoctorById(int doctorId) {
        // Updated SQL query to select from the new doctors table structure
        String sql = "SELECT id, full_name, specialization, email, status FROM doctors WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Read all the new columns from the result set
                return new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialization"),
                        rs.getString("email"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if doctor not found
    }
    // In dao/DoctorDAO.java

    // ADD THIS NEW METHOD
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        // Updated SQL to fetch all fields for all doctors
        String sql = "SELECT id, full_name, specialization, email, status FROM doctors ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialization"),
                        rs.getString("email"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    // In: dao/DoctorDAO.java

    // --- ADD THIS NEW METHOD ---
    /**
     * Finds the doctor's primary ID from the doctors table based on their user login ID.
     * @param userId The ID from the 'users' table.
     * @return The corresponding ID from the 'doctors' table, or -1 if not found.
     */
    public int getDoctorIdByUserId(int userId) {
        String sql = "SELECT id FROM doctors WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return an invalid ID if not found
    }
    // In: dao/DoctorDAO.java

// ... (your existing getDoctorById and getDoctorIdByUserId methods) ...

    // --- ADD THIS NEW METHOD FOR DOCTOR SEARCH ---
    /**
     * Searches for doctors, optionally filtering by name and/or specialty.
     * @param nameQuery The search term for the doctor's name (can be empty).
     * @param specialtyQuery The specialty to filter by (can be "All" or empty).
     * @return A List of Doctor objects matching the criteria.
     */
    public List<Doctor> searchDoctors(String nameQuery, String specialtyQuery) {
        List<Doctor> doctors = new ArrayList<>();

        // Start building the SQL query
        StringBuilder sql = new StringBuilder("SELECT id, full_name, specialization, email, status FROM doctors WHERE status = 'Active'");

        boolean hasNameQuery = nameQuery != null && !nameQuery.trim().isEmpty();
        boolean hasSpecialtyQuery = specialtyQuery != null && !specialtyQuery.trim().isEmpty() && !specialtyQuery.equalsIgnoreCase("All");

        if (hasNameQuery) {
            sql.append(" AND full_name LIKE ?");
        }
        if (hasSpecialtyQuery) {
            sql.append(" AND specialization = ?");
        }
        sql.append(" ORDER BY full_name");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (hasNameQuery) {
                pstmt.setString(paramIndex++, "%" + nameQuery + "%"); // Use LIKE for partial matches
            }
            if (hasSpecialtyQuery) {
                pstmt.setString(paramIndex, specialtyQuery);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("specialization"),
                        rs.getString("email"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    // In: dao/DoctorDAO.java

    /**
     * Updates an existing doctor's details.
     * @param doctor The Doctor object with the updated information.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateDoctor(Doctor doctor) {
        // Note: We don't update the email/username here as that's a more complex operation.
        // We focus on the fields an admin would typically manage.
        String sql = "UPDATE doctors SET full_name = ?, specialization = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, doctor.getFullName());
            pstmt.setString(2, doctor.getSpecialization());
            pstmt.setString(3, doctor.getStatus());
            pstmt.setInt(4, doctor.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a doctor and their associated user login.
     * This relies on the ON DELETE CASCADE constraint in your database schema.
     * @param doctorId The ID of the doctor to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteDoctor(int doctorId) {
        // By deleting the user record, the database's "ON DELETE CASCADE" rule
        // will automatically delete the corresponding doctor record.
        String sql = "DELETE FROM users WHERE id = (SELECT user_id FROM doctors WHERE id = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // In: dao/DoctorDAO.java

    /**
     * Registers a new doctor by creating a user record and a doctor record
     * within a single database transaction.
     * @return The new Doctor's ID if successful, otherwise -1.
     */
    public int registerDoctor(String fullName, String specialization, String email, String password) {
        String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Doctor')";
        String doctorSql = "INSERT INTO doctors (full_name, specialization, email, user_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        int newDoctorId = -1;

        try {
            conn = DatabaseConnection.getConnection();
            // Start a transaction
            conn.setAutoCommit(false);

            // --- Part 1: Insert into the 'users' table ---
            try (PreparedStatement userPstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userPstmt.setString(1, email); // Use email as the username
                userPstmt.setString(2, password); // In a real app, this should be hashed!
                userPstmt.executeUpdate();

                // Get the auto-generated user_id
                ResultSet generatedKeys = userPstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newUserId = generatedKeys.getInt(1);

                    // --- Part 2: Insert into the 'doctors' table using the new user_id ---
                    try (PreparedStatement doctorPstmt = conn.prepareStatement(doctorSql, Statement.RETURN_GENERATED_KEYS)) {
                        doctorPstmt.setString(1, fullName);
                        doctorPstmt.setString(2, specialization);
                        doctorPstmt.setString(3, email);
                        doctorPstmt.setInt(4, newUserId);
                        doctorPstmt.executeUpdate();

                        ResultSet docKeys = doctorPstmt.getGeneratedKeys();
                        if (docKeys.next()){
                            newDoctorId = docKeys.getInt(1);
                        }
                    }
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            // If both inserts were successful, commit the transaction
            conn.commit();

        } catch (SQLException e) {
            System.err.println("Transaction is being rolled back");
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return -1; // Return -1 on failure
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit mode
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return newDoctorId;
    }
}