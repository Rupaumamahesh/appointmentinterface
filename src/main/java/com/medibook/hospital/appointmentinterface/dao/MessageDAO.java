// In: dao/MessageDAO.java
package com.medibook.hospital.appointmentinterface.dao;

import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import com.medibook.hospital.appointmentinterface.model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * Fetches all messages for a specific doctor from the database.
     */
    public List<Message> getMessagesForDoctor(int doctorId) {
        List<Message> messages = new ArrayList<>();
        // Example SQL - your table structure might be different
        String sql = "SELECT sender_name, subject, received_timestamp FROM messages WHERE recipient_doctor_id = ? ORDER BY received_timestamp DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String from = rs.getString("sender_name");
                String subject = rs.getString("subject");
                String timestamp = rs.getString("received_timestamp"); // Or format it as needed

                messages.add(new Message(from, subject, timestamp));
            }

        } catch (SQLException e) {
            System.err.println("Database error fetching messages: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }
// In: dao/MessageDAO.java
// ... (your existing class code) ...

    /**
     * You would also add other methods here later, for example:
     * public boolean sendMessage(Message message, int recipientId) { ... }
     * public Message getMessageDetails(int messageId) { ... }
     */

    // --- ADD THIS TEST METHOD ---
    public static void main(String[] args) {
        MessageDAO dao = new MessageDAO();

        // From our setup script, Dr. Smith's ID is 1. Let's test with that.
        int testDoctorId = 1;

        System.out.println("Testing DAO: Fetching messages for doctor ID: " + testDoctorId);
        List<Message> messages = dao.getMessagesForDoctor(testDoctorId);

        if (messages.isEmpty()) {
            System.out.println("RESULT: No messages found. Check the doctor ID and the database table.");
            System.out.println("Possible issues: Database connection failed, or no messages for this doctor in the 'messages' table.");
        } else {
            System.out.println("SUCCESS! Found " + messages.size() + " messages:");
            for (Message msg : messages) {
                System.out.println("  -> From: " + msg.getFrom() + " | Subject: " + msg.getSubject());
            }
        }
    }
}
    /**
     * You would also add other methods here later, for example:
     * public boolean sendMessage(Message message, int recipientId) { ... }
     * public Message getMessageDetails(int messageId) { ... }
     */
