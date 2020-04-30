package com.example.myfoodappv2.ui.forum;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.FoodDonation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.myfoodappv2.R;
import com.google.firebase.firestore.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.example.myfoodappv2.Register.TAG;

public class ForumFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    private CollectionReference forumRef = db.collection("Forum");
    private ForumAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum,container,false);
        setUpRecyclerView(view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button notification_button = (Button) getView().findViewById(R.id.forum_addComment);
        LinearLayout bottomSheet = (LinearLayout) getView().findViewById(R.id.forum_bottomSheet);
        final EditText editTextComments = getView().findViewById(R.id.bottomSheet_editText);
        final EditText editTextTitle = getView().findViewById(R.id.bottomSheet_titleText);
        Button bottomSheet_addButton = (Button) getView().findViewById(R.id.bottomSheet_addButton);

        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        final LocalDateTime now = LocalDateTime.now();

        fAuth = FirebaseAuth.getInstance();

        final BottomSheetBehavior bottomSheet_behavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheet_behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheet_behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        //to enable scrolling for edit text in bottom sheet
        editTextComments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        //add the data into firebase
        bottomSheet_addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forumComment = editTextComments.getText().toString();
                String forumTitle = editTextTitle.getText().toString();
                String time = dtf.format(now);
                String userID = fAuth.getCurrentUser().getUid();
                String documentId = Long.toString(System.currentTimeMillis()) +userID;
                DocumentReference documentReference = db.collection("Forum").document(documentId);
                CollectionReference forumComments = db.collection("Forum");
                Map<String,Object> forum = new HashMap<>();
                forum.put("title",forumTitle);
                forum.put("comments", forumComment);
                forum.put("time",time);
                forum.put("userName", fAuth.getCurrentUser().getDisplayName());
                forum.put("userID", fAuth.getCurrentUser().getUid());

                documentReference.set(forum).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });

                bottomSheet_behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                editTextComments.setText("");
            }
        });
    }

    private void setUpRecyclerView(View view) {
        Query query = forumRef.orderBy("time", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<ForumRowModel> options = new FirestoreRecyclerOptions.Builder<ForumRowModel>()
                .setQuery(query, ForumRowModel.class)
                .build();

        adapter = new ForumAdapter(options);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.forum_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
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