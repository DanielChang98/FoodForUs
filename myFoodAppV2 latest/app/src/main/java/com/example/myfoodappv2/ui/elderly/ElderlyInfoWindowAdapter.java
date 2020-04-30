package com.example.myfoodappv2.ui.elderly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfoodappv2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class ElderlyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View infoWindow;
    private Context context;

    public ElderlyInfoWindowAdapter(Context context) {
        this.context = context;
        infoWindow = LayoutInflater.from(context).inflate(R.layout.elderly_info_window, null);
    }

    //Function to display information into the info windows
    private void displayInfo(Marker marker, View view){
        Elderly elderly = (Elderly) marker.getTag();

        //load image
        String imageUri = elderly.getImageUri();
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView_infoWin_image);
        Picasso.get().load(imageUri).fit().centerCrop().placeholder(R.drawable.ic_image_placeholder).into(imageView);

        //load other info
        TextView textView_elderly = (TextView) view.findViewById(R.id.textView_infoWin_elderly);
        TextView textView_address= (TextView) view.findViewById(R.id.textView_infoWin_description);
        String name = elderly.getName();
        String address = elderly.getAddress();
        textView_elderly.setText(name);
        textView_address.setText(address);
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