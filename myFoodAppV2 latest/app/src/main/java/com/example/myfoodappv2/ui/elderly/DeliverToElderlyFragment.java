package com.example.myfoodappv2.ui.elderly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.ElderlyViewModel;
import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.ui.getFood.FoodListFragment;
import com.example.myfoodappv2.ui.home.HomeFragment;
import com.example.myfoodappv2.user.ClusterMarker;
import com.example.myfoodappv2.user.MyClusterManagerRenderer;
import com.example.myfoodappv2.user.User;
import com.example.myfoodappv2.user.UserLocation;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class DeliverToElderlyFragment extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    SupportMapFragment mapFragment;
    private Elderly elderly;
    private ElderlyViewModel viewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference elderlyCollection = db.collection("Elderly");
    private CollectionReference userLocationCollection = db.collection("UserLocations");
    private static final String TAG = "Deliver to Elderly";
    private UserLocation mUserPosition;
    private HomeActivity homeActivity;
    private ArrayList<User> mUserList = new ArrayList<>();
    private ArrayList<UserLocation> mUserLocations = new ArrayList<>();
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer mClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private GoogleMap mGoogleMap;
    public static double elderlyLatitude;
    public static double elderlyLongitude;
    private FirebaseAuth fAuth;
    int bonusPoint = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //views
        View root = inflater.inflate(R.layout.fragment_choose_elderly_map, container, false);
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        Button button_changeView = (Button)root.findViewById(R.id.button_CfChangeView);

        //variable
        viewModel = ViewModelProviders.of(getActivity()).get(ElderlyViewModel.class);

        //display map
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        setUserPosition();
        return root;
    }

    private void resetMap(){
        if(mGoogleMap != null) {
            mGoogleMap.clear();

            if(mClusterManager != null){
                mClusterManager.clearItems();
            }

            if (mClusterMarkers.size() > 0) {
                mClusterMarkers.clear();
                mClusterMarkers = new ArrayList<>();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        if(mGoogleMap!=null)
            resetMap();

        //add markers of elderly on the google map
        elderlyCollection.whereEqualTo("status", "available")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Elderly elderly = documentSnapshot.toObject(Elderly.class);
                            elderly.setElderlyId(documentSnapshot.getId());

                            LatLng location = new LatLng(elderly.getElderlyLocationLatitude(), elderly.getElderlyLocationLongitude());
                            Marker marker;
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(elderly.getName()));
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            marker.setTag(elderly);
                        }
                    }
                });

        CameraUpdate elderlyLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(5.4164, 100.3327), 10);
        googleMap.animateCamera(elderlyLocation);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setInfoWindowAdapter(new ElderlyInfoWindowAdapter(getActivity()));
        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        Elderly selectedElderly = (Elderly)marker.getTag();
        viewModel.setElderly(selectedElderly);
        viewModel.getElderly().setElderlyId(selectedElderly.getElderlyId());
        final String elderlyId = selectedElderly.getElderlyId();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Open Google Map for navigation to this location?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        String latitude = String.valueOf(marker.getPosition().latitude);
                        String longitude = String.valueOf(marker.getPosition().longitude);
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");

                        try {
                            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                elderlyLatitude = marker.getPosition().latitude;
                                elderlyLongitude = marker.getPosition().longitude;
                                startActivity(mapIntent);

                                ((HomeActivity)getActivity()).clearViewModel();
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
                                fragmentTransaction.commit();
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: NullPointerException: Couldn't open map." + e.getMessage());
                            Toast.makeText(getActivity(), "Couldn't open map", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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

    private void setUserPosition() {
        for (UserLocation userLocation : mUserLocations) {
            if (userLocation.getUser().getUser_id().equals(FirebaseAuth.getInstance().getUid())) {
                mUserPosition = userLocation;
            }
        }
    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText("Deliver Successful");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void addBonus() {
        fAuth = FirebaseAuth.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        //update the amount of the foodDonation
        String currentDocument = "Users/" + userId;
        bonusPoint = 50;
        db.document(currentDocument).update("bonusPoint", bonusPoint);
    }

}
