// In: dao/AppointmentDAO.java
package com.medibook.hospital.appointmentinterface.dao;

import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import com.medibook.hospital.appointmentinterface.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AppointmentDAO {

    // --- METHODS FOR DOCTOR DASHBOARD COUNTS ---
    public int getTotalAppointmentsToday(int doctorId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAppointmentsByStatusToday(int doctorId, String status) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = CURDATE() AND status = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // --- ADD THIS METHOD FOR THE DOCTOR'S SCHEDULE VIEW ---
    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.id, a.appointment_date, a.appointment_time, a.status, " +
                "p.full_name AS patient_name, d.full_name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.doctor_id = ? " +
                "ORDER BY a.appointment_date, a.appointment_time";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    // In: dao/AppointmentDAO.java

    // --- ADD THIS NEW METHOD FOR THE PATIENT DASHBOARD ---
    public Appointment getUpcomingAppointmentForPatient(int patientId) {
        String sql = "SELECT a.id, a.appointment_date, a.appointment_time, a.status, d.full_name AS doctor_name " +
                "FROM appointments a JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.patient_id = ? AND a.appointment_date >= CURDATE() " +
                "ORDER BY a.appointment_date ASC, a.appointment_time ASC LIMIT 1"; // Order ascending to get the *next* one

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Note: We don't have the patient's name in this query, so we pass null for that argument.
                return new Appointment(
                        rs.getInt("id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        null, // patientName is not needed for this view
                        rs.getString("doctor_name"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no upcoming appointments are found
    }
    // --- METHODS FOR FETCHING APPOINTMENT LISTS ---

    /**
     * Fetches a list of all appointments for the Admin's Master Schedule.
     */
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.id, a.appointment_date, a.appointment_time, a.status, " +
                "p.full_name AS patient_name, d.full_name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "ORDER BY a.appointment_date DESC, a.appointment_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("patient_name"), // 4th argument
                        rs.getString("doctor_name"),  // 5th argument
                        rs.getString("status")       // 6th argument
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    /**
     * Fetches a list of all appointments for a specific patient.
     * @param patientId The ID of the patient.
     * @return A List of Appointment objects.
     */
    public List<Appointment> getAppointmentsForPatient(int patientId) {
        // --- THIS IS THE CORRECTED QUERY ---
        // It now fetches both doctor and patient names
        String sql = "SELECT a.id, a.appointment_date, a.appointment_time, a.status, " +
                "p.full_name AS patient_name, d.full_name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.patient_id = ? " +
                "ORDER BY a.appointment_date DESC, a.appointment_time DESC;";

        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // --- THIS IS THE CORRECTED CONSTRUCTOR CALL ---
                // It now passes all 6 required arguments in the correct order
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("patient_name"), // 4th argument
                        rs.getString("doctor_name"),  // 5th argument
                        rs.getString("status")       // 6th argument
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
    /**
     * Fetches a doctor's weekly availability schedule.
     * @return A Map where the key is the DayOfWeek and the value is an array of [startTime, endTime].
     */
    public Map<DayOfWeek, LocalTime[]> getDoctorAvailability(int doctorId) {
        Map<DayOfWeek, LocalTime[]> availabilityMap = new HashMap<>();
        String sql = "SELECT day_of_week, start_time, end_time FROM availability WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DayOfWeek day = DayOfWeek.valueOf(rs.getString("day_of_week").toUpperCase());
                LocalTime start = rs.getTime("start_time").toLocalTime();
                LocalTime end = rs.getTime("end_time").toLocalTime();
                availabilityMap.put(day, new LocalTime[]{start, end});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availabilityMap;
    }

    /**
     * Fetches a set of already booked appointment times for a specific doctor on a given date.
     * @return A Set of LocalTime objects representing the booked slots.
     */
    public Set<LocalTime> getBookedSlots(int doctorId, LocalDate date) {
        Set<LocalTime> bookedSlots = new HashSet<>();
        String sql = "SELECT appointment_time FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bookedSlots.add(rs.getTime("appointment_time").toLocalTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookedSlots;
    }

    public boolean bookAppointment(int doctorId, int patientId, LocalDate date, LocalTime time, String reason) {
        String sql = "INSERT INTO appointments (doctor_id, patient_id, appointment_date, appointment_time, status, reason) VALUES (?, ?, ?, ?, 'Pending', ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            pstmt.setInt(2, patientId);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setTime(4, java.sql.Time.valueOf(time));
            pstmt.setString(5, reason);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // --- ADD THIS NEW METHOD FOR CANCELLING ---
    /**
     * Updates an appointment's status to "Cancelled".
     * @param appointmentId The ID of the appointment to cancel.
     * @return true if the update was successful, false otherwise.
     */
    public boolean cancelAppointment(int appointmentId) {
        String sql = "UPDATE appointments SET status = 'Cancelled' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}