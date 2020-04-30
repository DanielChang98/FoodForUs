package com.example.myfoodappv2.ui.getFood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.FoodDonation;
import com.example.myfoodappv2.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

//This is an adapter for the recyclerView in GetFoodFragment

public class FoodListAdapter extends FirestoreRecyclerAdapter<FoodDonation, FoodListAdapter.FoodDonationHolder> {
    private OnItemClickListener listener;

    public FoodListAdapter(@NonNull FirestoreRecyclerOptions<FoodDonation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodDonationHolder holder, int position, @NonNull FoodDonation model) {
        holder.textViewTitle.setText(model.getFood());
        holder.textViewDescription.setText(model.getDescription());
        String amountStr = Integer.toString(model.getAmount()) + " pax";
        holder.textViewAmount.setText(amountStr);
        holder.textViewLocation.setText(model.getDonorAddress());
        String imageUri = model.getImageUri();
        Picasso.get().load(imageUri).placeholder(R.drawable.ic_image_placeholder).fit().centerCrop().into(holder.imageView);
    }

    @NonNull
    @Override
    public FoodDonationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_donation_item,
                parent, false);
        return new FoodDonationHolder(v);
    }

    class FoodDonationHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewAmount;
        TextView textViewLocation;
        ImageView imageView;

        public FoodDonationHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewDescription = itemView.findViewById(R.id.textView_description);
            textViewAmount = itemView.findViewById(R.id.textView_amount);
            textViewLocation = itemView.findViewById(R.id.textView_address);
            imageView = itemView.findViewById(R.id.imageView_itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
