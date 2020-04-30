package com.example.myfoodappv2.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfoodappv2.ui.elderly.DeliverToElderlyFragment;
import com.example.myfoodappv2.ui.elderly.ElderlyFragment;
import com.example.myfoodappv2.ui.getFood.ChooseFoodMapFragment;
import com.example.myfoodappv2.ui.donateFood.DonateFoodFragment;
import com.example.myfoodappv2.R;

public class HomeFragment  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        Button button_donateFood = view.findViewById(R.id.button_donateFood);
        Button button_getFood = view.findViewById(R.id.button_getFood);
        Button button_registerElderly = view.findViewById(R.id.button_registerElderly);
        Button button_redeem = view.findViewById(R.id.button_redeem);

        button_donateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to donateFoodFragment
                DonateFoodFragment donateFoodFragment = new DonateFoodFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, donateFoodFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_getFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to choose food map fragment
                ChooseFoodMapFragment chooseFoodMapFragment = new ChooseFoodMapFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, chooseFoodMapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_registerElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to choose food map fragment
                ElderlyFragment elderlyFragment = new ElderlyFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, elderlyFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to choose deliver to elderly
                DeliverToElderlyFragment deliverToElderlyFragment = new DeliverToElderlyFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, deliverToElderlyFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}