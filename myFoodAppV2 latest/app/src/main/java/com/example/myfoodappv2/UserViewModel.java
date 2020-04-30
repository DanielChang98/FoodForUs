package com.example.myfoodappv2;

import androidx.lifecycle.ViewModel;

import com.example.myfoodappv2.user.User;

public class UserViewModel extends ViewModel {
    private User user;
    public User getUser(){
        return user;
    };

    public void setUser(User user)
    {
        this.user = new User(user.getEmail(), user.getUser_id(), user.getUsername(), user.getPhone(),
                user.getAvatar(), user.getToken(), user.getBonusPoint());
    }
}
