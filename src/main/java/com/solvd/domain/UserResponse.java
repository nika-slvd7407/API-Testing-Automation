package com.solvd.domain;

public class UserResponse {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;
    private int statusCode;
    private String message;

    public UserResponse() {
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getStatus() { return status; }
    public int getStatusCode() { return statusCode; } // getter

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setGender(String gender) { this.gender = gender; }
    public void setStatus(String status) { this.status = status; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}