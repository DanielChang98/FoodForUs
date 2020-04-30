package com.example.myfoodappv2.ui.getFood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.HomeActivity;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.SharedViewModel;
import com.example.myfoodappv2.ui.donateFood.ChooseLocationMapFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;

public class FoodListFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference foodDonationRef = db.collection("FoodDonation");
    private FoodListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        Button button_changeView = (Button) view.findViewById(R.id.button_listChangeView);

        button_changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //move to foodListFragment
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, new ChooseFoodMapFragment());
                fragmentTransaction.commit();
            }
        });
        setUpRecyclerView(view);
        return view;
    }

    //Function to load data from firebase and set to recyclerView
    private void setUpRecyclerView(View view) {
        Query query = foodDonationRef.whereGreaterThanOrEqualTo("amount", 1);
        FirestoreRecyclerOptions<FoodDonation> options = new FirestoreRecyclerOptions.Builder<FoodDonation>()
                .setQuery(query, FoodDonation.class)
                .build();

        adapter = new FoodListAdapter(options);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //pass info of food donation selected to GetGoodFragment through viewModel
                FoodDonation selectedDonation = documentSnapshot.toObject(FoodDonation.class);
                selectedDonation.setFoodDonationId(documentSnapshot.getId());
                SharedViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
                viewModel.setFoodDonation(selectedDonation);
                viewModel.getFoodDonation().setFoodDonationId(selectedDonation.getFoodDonationId());

                //call GetFoodFragment
                GetFoodFragment getFoodFragment = new GetFoodFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, getFoodFragment);
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
