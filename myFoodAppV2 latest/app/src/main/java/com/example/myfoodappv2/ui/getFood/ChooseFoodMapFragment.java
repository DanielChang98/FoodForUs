package com.example.myfoodappv2.ui.getFood;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.SharedViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ChooseFoodMapFragment extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    SupportMapFragment mapFragment;
    private SharedViewModel viewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference foodDonationCollection = db.collection("FoodDonation");


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //views
        View root = inflater.inflate(R.layout.fragment_choose_food_map, container, false);
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        Button button_changeView = (Button)root.findViewById(R.id.button_CfChangeView);

        //variable
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        //display map
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        button_changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to foodListFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new FoodListFragment());
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //add markers of donation location on the google map
        foodDonationCollection.whereGreaterThanOrEqualTo("amount", 1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            FoodDonation foodDonation = documentSnapshot.toObject(FoodDonation.class);
                            foodDonation.setFoodDonationId(documentSnapshot.getId());

                            LatLng location = new LatLng(foodDonation.getDonorLocationLatitude(), foodDonation.getDonorLocationLongitude());
                            Marker marker;
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(foodDonation.getFood()));
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            marker.setTag(foodDonation);

                        }
                    }
                });
        CameraUpdate donationLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(5.4164, 100.3327), 10);
        googleMap.animateCamera(donationLocation);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new FoodDonationInfoWindowAdapter(getActivity()));
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        //call showInfoWindow the second time because the image won't load for the 1st time
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                marker.showInfoWindow();

            }
        }, 100);
        return true;
    }


    //When the info window of the map is clicked,
    //pass the selected foodDonation info to the getFoodFragment
    //through the shared view model
    @Override
    public void onInfoWindowClick(Marker marker) {
        FoodDonation selectedFoodDonation = (FoodDonation)marker.getTag();
        viewModel.setFoodDonation(selectedFoodDonation);
        viewModel.getFoodDonation().setFoodDonationId(selectedFoodDonation.getFoodDonationId());

        //call GetFoodFragment
        GetFoodFragment getFoodFragment = new GetFoodFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, getFoodFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
