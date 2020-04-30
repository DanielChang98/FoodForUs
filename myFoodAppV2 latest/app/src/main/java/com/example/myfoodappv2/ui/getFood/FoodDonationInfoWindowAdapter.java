package com.example.myfoodappv2.ui.getFood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

//adpter for info window in chooseFoodMapFagment

public class FoodDonationInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View infoWindow;
    private Context context;


    public FoodDonationInfoWindowAdapter(Context context) {
        this.context = context;
        infoWindow = LayoutInflater.from(context).inflate(R.layout.food_donation_info_wnidow, null);
    }

    //Function to display information into the info windows
    private void displayInfo(Marker marker, View view){
        FoodDonation foodDonation = (FoodDonation) marker.getTag();

        //load image
        String imageUri = foodDonation.getImageUri();
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView_infoWin_image);
        Picasso.get().load(imageUri).fit().centerCrop().placeholder(R.drawable.ic_image_placeholder).into(imageView);

        //load other info
        TextView textView_food = (TextView) view.findViewById(R.id.textView_infoWin_food);
        TextView textView_address= (TextView) view.findViewById(R.id.textView_infoWin_description);
        String food = foodDonation.getFood();
        String description = foodDonation.getDescription();
        textView_food.setText(food);
        textView_address.setText(description);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        displayInfo(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        displayInfo(marker, infoWindow);
        return infoWindow;
    }
}
