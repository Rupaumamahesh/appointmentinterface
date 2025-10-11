// In: doctor/view/PatientListView.java
package com.medibook.hospital.appointmentinterface.doctor.view;

import com.medibook.hospital.appointmentinterface.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class PatientListView {
    public Node getView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(25));

        Label title = new Label("My Patients");
        title.getStyleClass().add("page-title");

        TableView<Patient> table = new TableView<>();
        TableColumn<Patient, String> nameCol = new TableColumn<>("Patient Name");
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Patient, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(cellData -> cellData.getValue().dobProperty());

        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(cellData -> cellData.getValue().genderProperty());

        TableColumn<Patient, String> lastVisitCol = new TableColumn<>("Last Visit");
        lastVisitCol.setCellValueFactory(cellData -> cellData.getValue().lastVisitProperty());

        table.getColumns().addAll(nameCol, dobCol, genderCol, lastVisitCol);
        table.setItems(getDummyPatients());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // In a real app, you would add a click listener to the table rows
        // to open the full Patient Profile/EHR view.

        view.getChildren().addAll(title, table);
        return view;
    }

    private ObservableList<Patient> getDummyPatients() {
        return FXCollections.observableArrayList(
                new Patient("John Doe", "1985-05-20", "Male", "2025-10-10"),
                new Patient("Jane Smith", "1992-11-30", "Female", "2025-09-15"),
                new Patient("Peter Jones", "1978-01-12", "Male", "2025-10-28")
        );
    }
}
