package com.reemzet.adminedushorts.Sbte.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.adminedushorts.R;


public class VideoViewHolder extends RecyclerView.ViewHolder {
    public TextView videotitle,videodesc;
    public ImageView videoimg;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        videotitle=itemView.findViewById(R.id.videotitle);
        videodesc=itemView.findViewById(R.id.videodesc);
        videoimg=itemView.findViewById(R.id.videoimg);

    }
}
