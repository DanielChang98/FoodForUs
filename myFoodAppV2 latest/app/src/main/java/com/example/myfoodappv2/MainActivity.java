package com.example.myfoodappv2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable logoAnimation;
    private static int SPLASH_TIME_OUT = 3000; //This is 3 seconds
    //Notification initial setup
    public static final String CHANNEL_ID = "foodapp_C1";
    private static final String CHANNEL_NAME = "foodapp_channel";
    private static final String CHANNEL_DESCRIPTION = "foodapp_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        ImageView foodImage = (ImageView) findViewById(R.id.food_image);
        foodImage.setBackgroundResource(R.drawable.animation1);
        logoAnimation = (AnimationDrawable) foodImage.getBackground();
        logoAnimation.start();

        //Notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do any action here. Now we are moving to next page
                //Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                Intent homeIntent = new Intent(MainActivity.this, Login.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

