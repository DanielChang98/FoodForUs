package com.example.myfoodappv2.ui.elderly;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.ElderlyViewModel;
import com.example.myfoodappv2.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseElderlyLocationMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private static final String[] PLACES = new String[]{
            "Penang", "Batu Ferringhi, Penang",
            "Georgetown, Penang", "Tanjung Bungah, Penang",
            "Gurney, Penang", "Seberang Perai, Penang",
            "Bayan Lepas, Penang"
    };

    private ElderlyViewModel viewModel;
    private SupportMapFragment mapFragment;
    private AutoCompleteTextView searchText;
    private GoogleMap googleMap;
    private ElderlyPlaceInfo placeInfo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //views
        View view = inflater.inflate(R.layout.fragment_elderly_choose_location_map, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_chooseLocation);
        searchText = (AutoCompleteTextView) view.findViewById(R.id.input_search_place);
        ImageView button_searchText = (ImageView) view.findViewById(R.id.button_searchLocation);
        Button button_confirm = (Button)view.findViewById(R.id.button_confirmLocation);

        //variables
        viewModel = ViewModelProviders.of(getActivity()).get(ElderlyViewModel.class);

        //display map
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        mapFragment.getMapAsync(this);

        //setting dropdown menu for searchText
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, PLACES);
        searchText.setAdapter(adapter);

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                String selectedPlace = adapter.getItem(position).toString();
                setPlaceByAddress(selectedPlace);
            }
        });

        button_searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlaceByAddress(searchText.getText().toString());
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(placeInfo == null)
                    Toast.makeText(getActivity(), "No address selected", Toast.LENGTH_SHORT).show();
                else {
                    //clear all the markers in on the map
                    googleMap.clear();
                    //pass the info of the location selected chooseLocationMapFragment through viewModel
                    viewModel.setPlaceInfo(new ElderlyPlaceInfo(placeInfo.getPlaceName(), placeInfo.getLatitude(), placeInfo.getLongitude()));
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.nav_host_fragment, new ElderlyFragment());
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    //Function to process the string address entered by user in the searchText
    void setPlaceByAddress(String selectedPlace) {
        LatLng selectedGeocode = convertAddress(selectedPlace);
        //if the location is found, save location details in placeInfo and place a marker on the location
        if (selectedGeocode.latitude != -90.0 && selectedGeocode.longitude != -140.0) {
            placeInfo = new ElderlyPlaceInfo(selectedPlace, selectedGeocode.latitude, selectedGeocode.longitude);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(selectedGeocode)
                    .title(selectedPlace));
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
            marker.setTag(selectedPlace);
            CameraUpdate elderlyLocation = CameraUpdateFactory.newLatLngZoom(selectedGeocode, 15);
            googleMap.animateCamera(elderlyLocation);
        } else
            Toast.makeText(getActivity(), "cannot find address", Toast.LENGTH_SHORT).show();
    }


    //Function to convert address to latitude and longitude
    public LatLng convertAddress(String elderlyAdd) {
        Geocoder geoCoder = new Geocoder(getActivity());
        LatLng geocode = new LatLng(-500, -500);
        if (elderlyAdd != null && elderlyAdd.trim() != "") {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(elderlyAdd, 3);
                for (int i = 0; i < addressList.size(); i++) {
                    geocode = new LatLng(addressList.get(i).getLatitude(), addressList.get(i).getLongitude());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return geocode;
    }

    //Function to convert Latlng to address
    public String convertLatLng(LatLng latlng) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
        String address = addresses.get(0).getAddressLine(0);

        return address;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(ElderlyViewModel.class);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;
        CameraUpdate donationLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(5.4164, 100.3327), 10); //zoom to penang
        googleMap.animateCamera(donationLocation);

        //when a point is clicked,
        //- clear other existing markers on the google map
        //-set new marker
        //-find the address of the point clicked and show in searchText
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                googleMap.clear();
                try {
                    String address = convertLatLng(point);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(point)
                            .title(address);
                    googleMap.addMarker(markerOptions);
                    searchText.setText(address);
                    placeInfo = new ElderlyPlaceInfo(address, point.latitude, point.longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
}
