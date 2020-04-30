package com.example.myfoodappv2.ui.elderly;

public class Elderly {
    private String elderlyId;
    private String name;
    private String description;
    private String imageUri;
    private String gender;
    private String address;
    private String contact;
    private String status;
    private double elderlyLocationLatitude;
    private double elderlyLocationLongitude;

    public Elderly(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Elderly(String elderlyId, String name, String gender, String description, String address, String status,
                   Double elderlyLocationLatitude, Double elderlyLocationLongitude, String contact, String imageUri)
    {
        this.elderlyId = elderlyId;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.address = address;
        this.status = status;
        this.elderlyLocationLatitude = elderlyLocationLatitude;
        this.elderlyLocationLongitude = elderlyLocationLongitude;
        this.contact = contact;
        this.imageUri = imageUri;
    }

    public String getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(String elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getElderlyLocationLatitude() {
        return elderlyLocationLatitude;
    }

    public void setElderlyLocationLatitude(double elderlyLocationLatitude) {
        this.elderlyLocationLatitude = elderlyLocationLatitude;
    }

    public double getElderlyLocationLongitude() {
        return elderlyLocationLongitude;
    }

    public void setElderlyLocationLongitude(double elderlyLocationLongitude) {
        this.elderlyLocationLongitude = elderlyLocationLongitude;
    }

}