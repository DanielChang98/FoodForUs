package com.example.myfoodappv2.ui.bonus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myfoodappv2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.myfoodappv2.user.LocationService.bonusPoint;

public class BonusFragment extends Fragment {
    private BonusViewModel dashboardViewModel;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_bonus, container, false);

        Button button_redeem = view.findViewById(R.id.button_redeem);
        Button button_viewVoucher = view.findViewById(R.id.button_viewVoucher);
        TextView bonusText = view.findViewById(R.id.text_bonus);

        String bonus = String.valueOf(bonusPoint);
        bonusText.setText(bonus);
        button_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedeemFragment redeemFragment = new RedeemFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, redeemFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        button_viewVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewVoucherFragment viewVoucherFragment = new ViewVoucherFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, viewVoucherFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
