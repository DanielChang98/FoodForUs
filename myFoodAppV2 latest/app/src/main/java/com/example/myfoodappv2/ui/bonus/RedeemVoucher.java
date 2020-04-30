package com.example.myfoodappv2.ui.bonus;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.VoucherViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.myfoodappv2.ui.bonus.RedeemFragment.pic;
import static com.example.myfoodappv2.ui.bonus.RedeemFragment.name;
import static com.example.myfoodappv2.ui.bonus.RedeemFragment.address;
import static com.example.myfoodappv2.ui.bonus.RedeemFragment.pointsNeeded;
import static com.example.myfoodappv2.ui.bonus.RedeemFragment.value;
import static com.example.myfoodappv2.ui.bonus.RedeemFragment.voucherId;
import static com.example.myfoodappv2.user.LocationService.bonusPoint;

public class RedeemVoucher extends Fragment {
    private static final String TAG = "RedeemVoucher";
    private VoucherViewModel voucherViewModel;
    private Voucher voucher;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference voucherRef = db.collection("Voucher");
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String userId = fAuth.getCurrentUser().getUid();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        voucherViewModel = ViewModelProviders.of(getActivity()).get(VoucherViewModel.class);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        c.add(Calendar.DATE, 14);  // number of days to add
        final String expirationDate = df.format(c.getTime());

        View view = inflater.inflate(R.layout.fragment_confirm_redeem, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_getVoucher);
        TextView textViewValue = (TextView) view.findViewById(R.id.textView_getValue);
        TextView textViewAddress = (TextView) view.findViewById(R.id.textView_getVoucher_address);
        TextView textVoucherId = (TextView) view.findViewById(R.id.textView_voucherId);
        TextView textExpiration = (TextView) view.findViewById(R.id.textView_expiration);
        TextView textName = (TextView) view.findViewById(R.id.textView_getVoucher_voucher);
        TextView textPoint = (TextView) view.findViewById(R.id.textView_point);

        final String status = "available";
        textViewValue.setText(value);
        textViewAddress.setText(address);
        textName.setText(name);
        textExpiration.setText(expirationDate);
        imageView.setImageResource(pic);
        String pointsNeed = String.valueOf(pointsNeeded);
        textPoint.setText(pointsNeed);

        Button button_submit = (Button)view.findViewById(R.id.button_get);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if(bonusPoint<pointsNeeded)
                    showToast2();

                else if (bonusPoint>pointsNeeded)
                {
                    voucher = new Voucher(voucherId, userId, name, expirationDate, pic, address, status, value);
                    voucherRef.add(voucher);
                    voucherViewModel.setVoucher(voucher);
                    minusBonus();
                    showToast();
                }

                ((HomeActivity)getActivity()).clearViewModel();
                //Back to home fragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new BonusFragment());
                fragmentTransaction.commit();
            }
        });

        Button button_cancel = (Button)view.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).clearViewModel();
                //Back to home fragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new BonusFragment());
                fragmentTransaction.commit();
            }
        });
        return view;
    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) getActivity().findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText("Redeemed Successful");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void showToast2() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout2, (ViewGroup) getActivity().findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);
        toastText.setText("Not enough points!\n" + pointsNeeded + " points are needed for this voucher...");

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void minusBonus() {
        //minus the points used
        String currentDocument = "Users/" + userId;
        bonusPoint = bonusPoint - pointsNeeded;
        Log.d(TAG, "BONUS TOTAL: " + bonusPoint );
        db.document(currentDocument).update("bonusPoint", bonusPoint);
    }
}
