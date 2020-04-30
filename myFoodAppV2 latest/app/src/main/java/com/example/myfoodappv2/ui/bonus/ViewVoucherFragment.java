package com.example.myfoodappv2.ui.bonus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.R;
import com.example.myfoodappv2.VoucherViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewVoucherFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference voucherRef = db.collection("Voucher");
    private ViewVoucherAdapter adapter;
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private String userId = fAuth.getCurrentUser().getUid();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_voucher, container, false);
        setUpRecyclerView(view);
        return view;
    }

    //Function to load data from firebase and set to recyclerView
    private void setUpRecyclerView(View view) {
        Query query = voucherRef.whereEqualTo("userId", userId);
        FirestoreRecyclerOptions<Voucher> options = new FirestoreRecyclerOptions.Builder<Voucher>()
                .setQuery(query, Voucher.class)
                .build();

        adapter = new ViewVoucherAdapter(options);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ViewVoucherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Voucher selectedVoucher = documentSnapshot.toObject(Voucher.class);
                selectedVoucher.setVoucherId(documentSnapshot.getId());
                VoucherViewModel viewModel = ViewModelProviders.of(getActivity()).get(VoucherViewModel.class);
                viewModel.setVoucher(selectedVoucher);
                viewModel.getVoucher().setVoucherId(selectedVoucher.getVoucherId());

                //call UseVoucherFragment
                UseVoucherFragment useVoucherFragment = new UseVoucherFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, useVoucherFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
