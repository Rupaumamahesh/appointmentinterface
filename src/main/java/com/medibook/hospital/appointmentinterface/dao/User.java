// In: dao/User.java
package com.medibook.hospital.appointmentinterface.dao;

public class User {

    // 1. Fields to hold the user's data
    private final int id;
    private final String role;
    // You can add more fields here later, like fullName or username

    /**
     * 2. Constructor: This method is called when you do "new User(...)"
     * It takes the data and saves it to the fields.
     * @param id The user's ID from the database.
     * @param role The user's role (e.g., "Patient", "Doctor") from the database.
     */
    public User(int id, String role) {
        this.id = id;
        this.role = role;
    }

    /**
     * 3. Getters: These are public methods that let other classes read the data.
     */
    public int getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}