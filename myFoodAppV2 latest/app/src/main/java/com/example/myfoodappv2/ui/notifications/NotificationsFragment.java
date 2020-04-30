package com.example.myfoodappv2.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.NotificationAdapter;
import com.example.myfoodappv2.NotificationCard;
import com.example.myfoodappv2.R;
import com.example.myfoodappv2.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private RecyclerView.Adapter notificationAdapter;
    private RecyclerView.LayoutManager notificationLayoutManager;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final ArrayList<NotificationCard> notificationCards = new ArrayList<>();
        notificationCards.add(new NotificationCard(R.drawable.ic_receive_food, "Food Received", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_deliver_food, "Food Sent", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_reward_card, "Bonus point", "Thanks."));

        notificationCards.add(new NotificationCard(R.drawable.ic_receive_food, "Food Received", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_deliver_food, "Food Sent", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_reward_card, "Bonus point", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_receive_food, "Food Received", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_deliver_food, "Food Sent", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_reward_card, "Bonus point", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_receive_food, "Food Received", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_deliver_food, "Food Sent", "Thanks."));
        notificationCards.add(new NotificationCard(R.drawable.ic_reward_card, "Bonus point", "Thanks."));

        notificationRecyclerView = root.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setHasFixedSize(true);
        notificationLayoutManager = new LinearLayoutManager(getContext());
        notificationAdapter = new NotificationAdapter(notificationCards);
        notificationRecyclerView.setLayoutManager(notificationLayoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(notificationRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    notificationCards.remove(position);
                                    notificationAdapter.notifyItemRemoved(position);
                                }
                                notificationAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    notificationCards.remove(position);
                                    notificationAdapter.notifyItemRemoved(position);
                                }
                                notificationAdapter.notifyDataSetChanged();
                            }
                        });

        notificationRecyclerView.addOnItemTouchListener(swipeTouchListener);

        return root;
    }
}
