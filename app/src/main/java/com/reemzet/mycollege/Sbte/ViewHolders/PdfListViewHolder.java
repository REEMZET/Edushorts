package com.reemzet.mycollege.Sbte.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.reemzet.mycollege.R;

public class PdfListViewHolder extends RecyclerView.ViewHolder {
    public TextView pdfname,tvitemdesc,tvitemprice;
    public ImageView imgdownload,tvbuybtn,imgpreview;
    public LottieAnimationView loadanim;

    public PdfListViewHolder(@NonNull View itemView) {
        super(itemView);
        pdfname=itemView.findViewById(R.id.tvpdfname);
        imgdownload=itemView.findViewById(R.id.imgdownload);
        loadanim=itemView.findViewById(R.id.loadlottie);
        tvitemdesc=itemView.findViewById(R.id.tvitemdesc);
        tvitemprice=itemView.findViewById(R.id.tvitemprice);
        tvbuybtn=itemView.findViewById(R.id.imgbuy);
        imgpreview=itemView.findViewById(R.id.imgpereview);
    }
}
