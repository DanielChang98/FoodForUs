package com.example.myfoodappv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>
{
    private ArrayList<NotificationCard> notificationCards;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView notificationIcon;
        public TextView notificationTitle;
        public TextView notificationBody;

        public NotificationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            notificationIcon = itemView.findViewById(R.id.nCardIcon);
            notificationTitle = itemView.findViewById(R.id.nCardTitle);
            notificationBody = itemView.findViewById(R.id.nCardBody);
        }
    }

    public NotificationAdapter(ArrayList<NotificationCard> notificationCards)
    {
        this.notificationCards = notificationCards;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notifications_2, parent, false);
        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(view);
        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationCard notificationCard = notificationCards.get(position);
        holder.notificationIcon.setImageResource(notificationCard.getNotificationCardIcon());
        holder.notificationTitle.setText(notificationCard.getNotificationCardTitle());
        holder.notificationBody.setText(notificationCard.getNotificationCardBody);
    }

    @Override
    public int getItemCount() {
        return notificationCards.size();
    }
}
