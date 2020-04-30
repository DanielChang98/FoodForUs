package com.example.myfoodappv2;

public class DeliveryTask {
    private String userId;
    private String elderlyId;
    private String elderlyLatitude;
    private String elderlyLongitude;

    public DeliveryTask(){

    }

    public DeliveryTask(String userId, String elderlyId, String elderlyLatitude, String elderlyLongitude) {
        this.userId = userId;
        this.elderlyId = elderlyId;
        this.elderlyLatitude = elderlyLatitude;
        this.elderlyLongitude = elderlyLongitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(String elderlyId) {
        this.elderlyId = elderlyId;
    }

    public String getElderlyLatitude() {
        return elderlyLatitude;
    }

    public void setElderlyLatitude(String elderlyLatitude) {
        this.elderlyLatitude = elderlyLatitude;
    }

    public String getElderlyLongitude() {
        return elderlyLongitude;
    }

    public void setElderlyLongitude(String elderlyLongitude) {
        this.elderlyLongitude = elderlyLongitude;
    }
}
