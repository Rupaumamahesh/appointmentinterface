// In: dao/PatientDAO.java
package com.medibook.hospital.appointmentinterface.dao;

import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import com.medibook.hospital.appointmentinterface.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // --- "READ" METHODS ---

    /**
     * Fetches a list of ALL patients from the database.
     * Used for the "Manage Patients" view in the Admin Portal.
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT id, full_name, date_of_birth, gender FROM patients ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    /**
     * Fetches a list of unique patients who have appointments with a specific doctor.
     * Used for the "My Patients" view in the Doctor Portal.
     */
    public List<Patient> getPatientsForDoctor(int doctorId) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT DISTINCT p.id, p.full_name, p.date_of_birth, p.gender " +
                "FROM patients p " +
                "JOIN appointments a ON p.id = a.patient_id " +
                "WHERE a.doctor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patients.add(new Patient(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }


    // --- "CREATE" METHODS ---

    /**
     * UPDATED version of your original method.
     * Registers a new patient with basic info from the public login screen.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerPatient(String fullName, String email, String password) {
        // This method assumes date of birth and gender are not collected at initial sign-up
        return registerPatientWithCredentials(fullName, null, null, email, password);
    }

    /**
     * Registers a new patient with detailed credentials.
     * This is a transactional operation: it succeeds or fails as a whole.
     * Used by the "Register New Patient" dialog in the Admin Portal.
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerPatientWithCredentials(String fullName, LocalDate dateOfBirth, String gender, String username, String password) {
        String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Patient')";
        String patientSql = "INSERT INTO patients (full_name, date_of_birth, gender, user_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Step 1: Insert into the 'users' table
            int newUserId;
            try (PreparedStatement userPstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                userPstmt.setString(1, username);
                userPstmt.setString(2, password); // In a real app, this should be hashed
                int affectedRows = userPstmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = userPstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // Step 2: Insert into the 'patients' table using the new user ID
            try (PreparedStatement patientPstmt = conn.prepareStatement(patientSql)) {
                patientPstmt.setString(1, fullName);
                patientPstmt.setDate(2, (dateOfBirth != null) ? java.sql.Date.valueOf(dateOfBirth) : null);
                patientPstmt.setString(3, gender);
                patientPstmt.setInt(4, newUserId);
                patientPstmt.executeUpdate();
            }

            conn.commit(); // If both inserts succeed, commit the changes
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // If any error occurs, undo all changes
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restore default behavior
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}