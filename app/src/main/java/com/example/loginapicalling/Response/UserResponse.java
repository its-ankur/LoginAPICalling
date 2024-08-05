// UserResponse.java
package com.example.loginapicalling.Response;

public class UserResponse {

    // Field to store the user ID
    private int id;

    // Field to store the user's first name
    private String firstName;

    // Field to store the user's last name
    private String lastName;

    // Field to store the user's maiden name
    private String maidenName;

    // Field to store the user's gender
    private String gender;

    // Field to store the user's email address
    private String email;

    // Field to store the user's username
    private String username;

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for firstName
    public String getFirstName() {
        return firstName;
    }

    // Setter for firstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Getter for lastName
    public String getLastName() {
        return lastName;
    }

    // Setter for lastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Getter for maidenName
    public String getMaidenName() {
        return maidenName;
    }

    // Setter for maidenName
    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    // Getter for gender
    public String getGender() {
        return gender;
    }

    // Setter for gender
    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Override toString method to provide a string representation of the UserResponse object
    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", maidenName='" + maidenName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
