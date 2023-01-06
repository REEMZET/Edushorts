package com.reemzet.mycollege.Sbte.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.reemzet.mycollege.R;

public class SyllabusViewHolder extends RecyclerView.ViewHolder {
   public TextView tvsubcode,tvsubname,tvsubfm,tvsubpm;
   public ImageView subimg,imgdown;
   public LottieAnimationView loadpdf;

    public SyllabusViewHolder(@NonNull View itemView) {
        super(itemView);
        tvsubcode=itemView.findViewById(R.id.tvsubcode);
        tvsubname=itemView.findViewById(R.id.tvsubname);
        tvsubfm=itemView.findViewById(R.id.tvsubfm);
        tvsubpm=itemView.findViewById(R.id.tvsubpm);
        subimg=itemView.findViewById(R.id.imgsub);
        imgdown=itemView.findViewById(R.id.imgdown);
        loadpdf=itemView.findViewById(R.id.loadlottie);


    }
}
