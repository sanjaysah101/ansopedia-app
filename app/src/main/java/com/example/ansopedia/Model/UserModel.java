package com.example.ansopedia.Model;

public class UserModel {
    private final int id;
    private final String userName;
    private final String email;
    private String displayName;
    private String isVerified;

    private String firstName;
    private String lastName;
    private String middleName;
    private String dob;

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public UserModel(int id, String userName, String email, String displayName, String isVerified, String firstName, String lastName, String middleName, String dob) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.displayName = displayName;
        this.isVerified = isVerified;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.dob = dob;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getDob() {
        return dob;
    }
//    "id": "1",
//        "username": "sanjay",
//        "email": "ssah007@gmail.com",
//        "slug": "",
//        "lastLogin": null,
//        "status": "1",
//        "displayName,": null,
//        "registeredAt": "0000-00-00 00:00:00",
//        "isVerified": "0",
//        "firstName": "Sanjay",
//        "lastName": "Sah",
//        "middleName": "Kumar",
//        "dob": "0000-00-00 00:00:00"

    public UserModel(int id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}
