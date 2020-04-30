package com.example.myfoodappv2;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class PickupSchedule {
    private String pickupScheduleId;
    private String foodDonationId;
    private String pickupBy;
    private Date date;
    private int foodAmount;

    public PickupSchedule(){
        //empty constructor required for firebase
    }

    public PickupSchedule(String foodDonationId, String pickupBy, Date date, int foodAmount) {
        this.foodDonationId = foodDonationId;
        this.pickupBy = pickupBy;
        this.date = date;
        this.foodAmount = foodAmount;
    }

    @Exclude
    public String getPickupScheduleId() {
        return pickupScheduleId;
    }

    @Exclude
    public void setPickupScheduleId(String pickupScheduleId) {
        this.pickupScheduleId = pickupScheduleId;
    }

    public String getFoodDonationId() {
        return foodDonationId;
    }

    public void setFoodDonationId(String foodDonationId) {
        this.foodDonationId = foodDonationId;
    }

    public String getPickupBy() {
        return pickupBy;
    }

    public void setPickupBy(String pickupBy) {
        this.pickupBy = pickupBy;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }
}
