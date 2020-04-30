package com.example.myfoodappv2;

public class NotificationCard
{
    int notificationCardIcon;
    String notificationCardTitle;
    String getNotificationCardBody;

    public NotificationCard(int notificationCardImage, String notificationCardTitle, String getNotificationCardBody) {
        this.notificationCardIcon = notificationCardImage;
        this.notificationCardTitle = notificationCardTitle;
        this.getNotificationCardBody = getNotificationCardBody;
    }

    public int getNotificationCardIcon() {
        return notificationCardIcon;
    }

    public String getNotificationCardTitle() {
        return notificationCardTitle;
    }
}
