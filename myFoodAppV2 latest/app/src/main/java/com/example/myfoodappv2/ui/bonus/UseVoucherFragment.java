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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.VoucherViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UseVoucherFragment extends Fragment {
    private static final String TAG = "Use Voucher";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference UsedVoucher = db.collection("UsedVoucher");
    private FirebaseAuth fAuth;
    private VoucherViewModel voucherViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_use_voucher, container, false);
        voucherViewModel = ViewModelProviders.of(getActivity()).get(VoucherViewModel.class);
        final Voucher voucher = voucherViewModel.getVoucher();
        fAuth = FirebaseAuth.getInstance();

        TextView textView_address= (TextView) view.findViewById(R.id.textView_getVoucher_address);
        TextView textView_value= (TextView) view.findViewById(R.id.textView_getVoucher_amount);
        TextView textView_expiration= (TextView) view.findViewById(R.id.textView_expiration);
        TextView textView_voucherId= (TextView) view.findViewById(R.id.textView_voucherId);
        TextView textView_name = (TextView) view.findViewById(R.id.textView_getVoucher_voucher);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_getVoucher);
        Button button_submit = (Button)view.findViewById(R.id.button_get);
        Button button_cancel = (Button)view.findViewById(R.id.button_cancel);

        textView_address.setText(voucher.getAddress());
        textView_value.setText(voucher.getValue());
        textView_expiration.setText(voucher.getExpiredDate());
        textView_voucherId.setText(voucher.getVoucherId());
        textView_name.setText(voucher.getName());
        imageView.setImageResource(voucher.getImageId());

        button_submit.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                String voucherId = voucherViewModel.getVoucher().getVoucherId();
                Log.d(TAG, "Voucher Id: " + voucherId);
                //String currentDocument = "Voucher/" + voucherId;
                //db.document(currentDocument).update("status", "used");
                voucher.setStatus("used");
                UsedVoucher.add(voucher);

                db.collection("Voucher").document(voucherId)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Voucher successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });

                ((HomeActivity)getActivity()).clearViewModel();
                showToast();
                //Back to home fragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new BonusFragment());
                fragmentTransaction.commit();
            }
        });

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
        toastText.setText("Voucher Used");
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.FILL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
