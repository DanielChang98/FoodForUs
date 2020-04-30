package com.example.myfoodappv2.ui.elderly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.R;
import com.example.myfoodappv2.ui.getFood.FoodListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ElderlyListAdapter extends FirestoreRecyclerAdapter<Elderly, ElderlyListAdapter.ElderlyHolder> {
    private ElderlyListAdapter.OnItemClickListener listener;

    public ElderlyListAdapter(@NonNull FirestoreRecyclerOptions<Elderly> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ElderlyListAdapter.ElderlyHolder holder, int position, @NonNull Elderly model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewGender.setText(model.getGender());
        holder.textViewLocation.setText(model.getAddress());
        String imageUri = model.getImageUri();
        Picasso.get().load(imageUri).placeholder(R.drawable.ic_image_placeholder).fit().centerCrop().into(holder.imageView);
    }

    @NonNull
    @Override
    public ElderlyListAdapter.ElderlyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_donation_item,
                parent, false);
        return new ElderlyListAdapter.ElderlyHolder(v);
    }

    class ElderlyHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewGender;
        TextView textViewLocation;
        ImageView imageView;

        public ElderlyHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewDescription = itemView.findViewById(R.id.textView_description);
            textViewGender = itemView.findViewById(R.id.textView_gender);
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

    public void setOnItemClickListener(ElderlyListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
