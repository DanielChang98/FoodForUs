package com.example.myfoodappv2.ui.bonus;

public class Voucher {
    private String voucherId = "";
    private String userId= "";
    private String name= "";
    private String expiredDate= "";
    private int imageId= 0;
    private String address= "";
    private String status= "";
    private String value= "";

    public Voucher(){

    }

    public Voucher(String voucherId, String userId, String name, String expiredDate, int imageId, String address, String status, String value) {
        this.voucherId = voucherId;
        this.userId = userId;
        this.name = name;
        this.expiredDate = expiredDate;
        this.imageId = imageId;
        this.address = address;
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public int getImageId() {
        return imageId;
    }

    public String getAddress() {
        return address;
    }

    public String getValue() {
        return value;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
