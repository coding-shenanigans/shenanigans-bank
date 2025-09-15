package com.codingshenanigans.shenanigans_bank.dtos;

import jakarta.validation.constraints.*;

public class SignupRequest {
    @NotBlank(message = "The first name is required")
    @Size(min = 2, message = "The first name should have at least 2 characters")
    @Size(max = 50, message = "The first name should not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "The last name is required")
    @Size(min = 2, message = "The last name should have at least 2 characters")
    @Size(max = 50, message = "The last name should not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "The email is required")
    @Email(message = "The email is not valid")
    private String email;

    @NotBlank(message = "The password is required")
    @Size(min = 8, message = "The password should have at least 8 characters")
    @Size(max = 50, message = "The password should not exceed 50 characters")
    @Pattern(regexp = ".*[a-z].*", message = "The password should have at least 1 lower case letter")
    @Pattern(regexp = ".*[A-Z].*", message = "The password should have at least 1 upper case letter")
    @Pattern(regexp = ".*[0-9].*", message = "The password should have at least 1 number")
    @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "The password should have at least 1 non-alphanumeric character")
    private String password;

    public SignupRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
