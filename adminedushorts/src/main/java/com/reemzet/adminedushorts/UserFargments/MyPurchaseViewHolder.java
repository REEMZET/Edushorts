package com.reemzet.adminedushorts.UserFargments;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reemzet.adminedushorts.R;


public class MyPurchaseViewHolder extends RecyclerView.ViewHolder {
    public TextView tvprodcuctname,tvproductid,tvpurchasedate,tvvaildity,tvprice;
    public ImageView productimg;
    public MyPurchaseViewHolder(@NonNull View itemView) {
        super(itemView);
        tvprodcuctname=itemView.findViewById(R.id.tvproductname);
        tvprice=itemView.findViewById(R.id.tvproductprice);
        tvproductid=itemView.findViewById(R.id.tvproductid);
        tvvaildity=itemView.findViewById(R.id.tvvalidity);
        tvpurchasedate=itemView.findViewById(R.id.tvpurchasedate);
        productimg=itemView.findViewById(R.id.productimge);
    }
}
