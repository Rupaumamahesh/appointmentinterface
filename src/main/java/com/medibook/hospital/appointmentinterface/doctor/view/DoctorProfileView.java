// In: doctor/view/DoctorProfileView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.dao.DoctorDAO;
import com.medibook.hospital.appointmentinterface.model.Doctor;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DoctorProfileView {

    public Node getView(int doctorId) {
        VBox view = new VBox(20);
        Label title = new Label("My Profile");
        title.getStyleClass().add("page-title");

        // Fetch the doctor's data from the database
        DoctorDAO doctorDAO = new DoctorDAO();
        Doctor doctor = doctorDAO.getDoctorById(doctorId);

        if (doctor != null) {
            // Use a GridPane for a clean, aligned, two-column layout
            GridPane grid = createProfileGrid();

            // Add the data to the grid using a helper method for clarity
            addRowToGrid(grid, 0, "Full Name:", doctor.getFullName());
            addRowToGrid(grid, 1, "Specialization:", doctor.getSpecialization());
            addRowToGrid(grid, 2, "Email:", doctor.getEmail());
            addRowToGrid(grid, 3, "Status:", doctor.getStatus());

            view.getChildren().addAll(title, grid);
        } else {
            // Fallback message if the doctor data could not be loaded
            Label errorLabel = new Label("Could not load doctor profile for ID: " + doctorId);
            view.getChildren().addAll(title, errorLabel);
        }

        return view;
    }

    /**
     * Creates and configures a GridPane for the profile layout.
     */
    private GridPane createProfileGrid() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("profile-grid");

        // Define column constraints for proper alignment
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.RIGHT); // Right-align the labels in the first column

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS); // Allow the values in the second column to expand

        grid.getColumnConstraints().addAll(col1, col2);
        return grid;
    }

    /**
     * Helper method to add a styled label-value pair to the GridPane.
     */
    private void addRowToGrid(GridPane grid, int row, String labelText, String valueText) {
        Label label = new Label(labelText);
        label.getStyleClass().add("profile-label");

        Label value = new Label(valueText);
        value.getStyleClass().add("profile-value");

        grid.add(label, 0, row);
        grid.add(value, 1, row);
    }
}