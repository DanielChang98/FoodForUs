package com.example.myfoodappv2.user;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String email;
    private String user_id;
    private String username;
    private String avatar;
    private String phone;
    private String token;
    private int bonusPoint;

    public User(String email, String user_id, String username, String phone, String avatar, String token, int bonusPoint) {
        this.email = email;
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
        this.phone = phone;
        this.token = token;
        this.bonusPoint = bonusPoint;
    }

    public User() {

    }

    protected User(Parcel in) {
        email = in.readString();
        user_id = in.readString();
        username = in.readString();
        avatar = in.readString();
        phone = in.readString();
        token = in.readString();
        bonusPoint = in.readInt();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    public static Parcelable.Creator<User> getCREATOR() {
        return CREATOR;
    }

    public int getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(int bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {return phone;    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {return token;    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAvatar() {return avatar;    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(phone);
        dest.writeString(avatar);
    }
}
