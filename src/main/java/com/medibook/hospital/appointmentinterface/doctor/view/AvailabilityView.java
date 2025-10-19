// In: doctor/view/AvailabilityView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.database.DatabaseConnection;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvailabilityView {

    public Node getView(int doctorId) {
        VBox view = new VBox(20);
        Label title = new Label("My Availability");
        title.getStyleClass().add("page-title");

        VBox availabilityList = new VBox(10);
        Label subTitle = new Label("Your current weekly schedule:");
        subTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");
        availabilityList.getChildren().add(subTitle);

        // SQL query to fetch availability, ordered correctly by day of the week
        String sql = "SELECT day_of_week, start_time, end_time FROM availability WHERE doctor_id = ? ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Create a styled row for each entry using a helper method
                Node availabilityRow = createAvailabilityRow(
                        rs.getString("day_of_week"),
                        rs.getTime("start_time").toString(),
                        rs.getTime("end_time").toString()
                );
                availabilityList.getChildren().add(availabilityRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            availabilityList.getChildren().add(new Label("Error loading availability."));
        }

        view.getChildren().addAll(title, availabilityList);
        return view;
    }

    /**
     * Helper method to create a single styled row for the availability display.
     */
    private Node createAvailabilityRow(String day, String start, String end) {
        Label dayLabel = new Label(day);
        dayLabel.getStyleClass().add("availability-day");

        Label timeLabel = new Label(start + " - " + end);
        timeLabel.getStyleClass().add("availability-time");

        // Use a Region as a spacer to push the time to the far right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(dayLabel, spacer, timeLabel);
        row.getStyleClass().add("availability-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }
}