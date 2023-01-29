package com.reemzet.mycollege.Sbte.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.reemzet.mycollege.R;

public class VideoslistAdapter extends RecyclerView.ViewHolder {

    public TextView lecnam,tvprice,vidlength,viddate;
    public ImageView thumbnail,buybtn,previewbtn;

    public VideoslistAdapter(@NonNull View itemView) {

        super(itemView);
    lecnam=itemView.findViewById(R.id.tvlecname);
    thumbnail=itemView.findViewById(R.id.thumbnail);
    tvprice=itemView.findViewById(R.id.tvprice);
    vidlength=itemView.findViewById(R.id.vidlength);
    viddate=itemView.findViewById(R.id.viddate);
    previewbtn=itemView.findViewById(R.id.previewbtn);
    buybtn=itemView.findViewById(R.id.buybtn);

    }
}
