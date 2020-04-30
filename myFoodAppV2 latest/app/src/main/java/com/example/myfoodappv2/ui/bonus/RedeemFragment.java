package com.example.myfoodappv2.ui.bonus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfoodappv2.R;

public class RedeemFragment extends Fragment {
    public static String name;
    public static String address;
    public static int pic;
    public static String value;
    public static String voucherId;
    public static int pointsNeeded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redeem, container, false);

        Button button_mcd = view.findViewById(R.id.button_mcd);
        Button button_laksa = view.findViewById(R.id.button_laksa);
        Button button_roticanai = view.findViewById(R.id.button_roticanai);
        Button button_burger = view.findViewById(R.id.button_burger);

        button_mcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = "McRonald";
                address = "77, Lalaland 5, 14100 Simpang Ampat, Penang.";
                pic = R.drawable.mcd2;
                value = "RM 10";
                voucherId = "mcronald77514100";
                pointsNeeded = 100;
                RedeemVoucher redeemVoucher = new RedeemVoucher();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, redeemVoucher);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            });

        button_laksa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = "The Penang Laksa";
                address = "98, Neverland 2, 14000 Bukit Mertajam, Penang.";
                pic = R.drawable.laksalogo;
                value = "RM 15";
                voucherId = "tplaksa98214000";
                pointsNeeded = 150;
                RedeemVoucher redeemVoucher = new RedeemVoucher();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, redeemVoucher);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_roticanai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = "Mr Roti Canai";
                address = "520, Switzland 99, 11900 Bayan Lepas, Penang.";
                pic = R.drawable.roticanai;
                value = "RM 10";
                voucherId = "mrotic5209911900";
                pointsNeeded = 100;
                RedeemVoucher redeemVoucher = new RedeemVoucher();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, redeemVoucher);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_burger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = "Hamly Burger";
                address = "3, Gland 8, 11600 Jelutong, Penang.";
                pic = R.drawable.ramlyb;
                value = "RM 15";
                voucherId = "hamlyb3811600";
                pointsNeeded = 150;
                RedeemVoucher redeemVoucher = new RedeemVoucher();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, redeemVoucher);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
