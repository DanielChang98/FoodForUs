package com.example.myfoodappv2.ui.bonus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ViewVoucherAdapter extends FirestoreRecyclerAdapter<Voucher, ViewVoucherAdapter.ViewVoucherHolder> {
    private ViewVoucherAdapter.OnItemClickListener listener;

    public ViewVoucherAdapter(@NonNull FirestoreRecyclerOptions<Voucher> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewVoucherHolder holder, int position, @NonNull Voucher model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewLocation.setText(model.getAddress());
        holder.textViewExpiration.setText(model.getExpiredDate());
        holder.textViewValue.setText(model.getValue());
        holder.imageView.setImageResource(model.getImageId());
    }

    @NonNull
    @Override
    public ViewVoucherAdapter.ViewVoucherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.voucher_item,
                parent, false);
        return new ViewVoucherAdapter.ViewVoucherHolder(v);
    }

    class ViewVoucherHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewValue;
        TextView textViewLocation;
        TextView textViewExpiration;
        TextView textViewVoucherId;
        ImageView imageView;

        public ViewVoucherHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewValue = itemView.findViewById(R.id.textView_value);
            textViewExpiration = itemView.findViewById(R.id.textView_expiration);
            textViewLocation = itemView.findViewById(R.id.textView_address);
            textViewVoucherId = itemView.findViewById(R.id.textView_voucherId);
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
