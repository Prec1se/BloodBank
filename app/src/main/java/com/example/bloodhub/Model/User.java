package com.example.bloodhub.Model;

public class User {
    String name, email, bloodgroup, location, phonenumber;
    boolean available;

    public User() {
    }

    public User(String name, String email, String bloodgroup, String location, String phonenumber, boolean available) {
        this.name = name;
        this.email = email;
        this.bloodgroup = bloodgroup;
        this.location = location;
        this.phonenumber = phonenumber;
        this.available = available;
    }

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public boolean getAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }
}
