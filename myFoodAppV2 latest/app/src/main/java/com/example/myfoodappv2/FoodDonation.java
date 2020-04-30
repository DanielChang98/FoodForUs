package com.example.myfoodappv2;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class FoodDonation {
    private String foodDonationId;
    private String donorId;
    private Date date;
    private String food;
    private String imageUri;
    private String description;
    private int amount;
    private String donorAddress;
    private String donorContactNo;
    private double donorLocationLatitude;
    private double donorLocationLongitude;

    public FoodDonation(){

    }

    public FoodDonation(String donorId, Date date, String food, String description, int amount, String donorAddress, Double donorLocationLatitude, Double donorLocationLongitude,  String donorContactNo, String imageUri) {
        this.donorId = donorId;
        this.date = date;
        this.food = food;
        this.description = description;
        this.amount = amount;
        this.donorAddress = donorAddress;
        this.donorLocationLatitude = donorLocationLatitude;
        this.donorLocationLongitude = donorLocationLongitude;
        this.donorContactNo = donorContactNo;
        this.imageUri = imageUri;
    }

    public String getFoodDonationId() {
        return foodDonationId;
    }

    public void setFoodDonationId(String foodDonationId) {
        this.foodDonationId = foodDonationId;
    }



    @Exclude
    public String getDonorId() {
        return donorId;
    }

    @Exclude
    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDonorAddress() {
        return donorAddress;
    }

    public void setDonorAddress(String donorAddress) {
        this.donorAddress = donorAddress;
    }

    public String getDonorContactNo() {
        return donorContactNo;
    }

    public void setDonorContactNo(String donorContactNo) {
        this.donorContactNo = donorContactNo;
    }

    public double getDonorLocationLatitude() {
        return donorLocationLatitude;
    }

    public void setDonorLocationLatitude(double donorLocationLatitude) {
        this.donorLocationLatitude = donorLocationLatitude;
    }

    public double getDonorLocationLongitude() {
        return donorLocationLongitude;
    }

    public void setDonorLocationLongitude(double donorLocationLongitude) {
        this.donorLocationLongitude = donorLocationLongitude;
    }
}
