package com.example.myfoodappv2.ui.forum;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoodappv2.R;

public class ForumViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView mComments, mTime;

    public ForumViewHolder(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.row_imageView);
        this.mComments = itemView.findViewById(R.id.row_comments);
        this.mTime = itemView.findViewById(R.id.row_time);
    }
}
