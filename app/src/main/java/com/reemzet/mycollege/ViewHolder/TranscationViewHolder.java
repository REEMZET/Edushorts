package com.reemzet.mycollege.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.mycollege.R;

public class TranscationViewHolder extends RecyclerView.ViewHolder {
    public ImageView tranimg;
    public TextView tvtrantitle,tvtranid,tvtranamount,tvtrantype,tvtrandate;
    public TranscationViewHolder(@NonNull View itemView) {
        super(itemView);
        tranimg=itemView.findViewById(R.id.tranimg);
        tvtrantitle=itemView.findViewById(R.id.tvtantitle);
        tvtranid=itemView.findViewById(R.id.tvtanid);
        tvtrantype=itemView.findViewById(R.id.tvtrantype);
        tvtranamount=itemView.findViewById(R.id.tvtranamount);
        tvtrandate=itemView.findViewById(R.id.tvtrandate);

    }
}
