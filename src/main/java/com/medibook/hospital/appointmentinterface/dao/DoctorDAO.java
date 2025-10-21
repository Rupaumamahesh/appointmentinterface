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
}