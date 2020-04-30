package com.example.myfoodappv2.ui.forum;

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

public class ForumAdapter extends FirestoreRecyclerAdapter<ForumRowModel, ForumAdapter.myViewHolder>{

    public ForumAdapter(@NonNull FirestoreRecyclerOptions<ForumRowModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull ForumRowModel model) {
        holder.textViewTime.setText(model.getTime());
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewUserName.setText(model.getUserName());
        holder.textViewComments.setText(model.getComments());
        //String imageUri = model.getImageUri();
        //Picasso.get().load(imageUri).placeholder(R.drawable.ic_image_placeholder).fit().centerCrop().into(holder.imageView);
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_row,
                parent,false);
        return new myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView textViewComments;
        TextView textViewTime;
        TextView textViewUserName;
        ImageView imageView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.row_comments);
            textViewTime = itemView.findViewById(R.id.row_time);
            textViewComments = itemView.findViewById(R.id.row_description);
            textViewUserName = itemView.findViewById(R.id.row_userID);
            imageView = itemView.findViewById(R.id.row_imageView);
        }
    }
}
