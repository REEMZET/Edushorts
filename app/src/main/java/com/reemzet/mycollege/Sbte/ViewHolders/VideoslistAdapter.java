package com.reemzet.mycollege.Sbte.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.reemzet.mycollege.R;

public class VideoslistAdapter extends RecyclerView.ViewHolder {

    public TextView lecnam,viewcount,vidlength,viddate;
    public ImageView thumbnail,lock,previewbtn;

    public VideoslistAdapter(@NonNull View itemView) {

        super(itemView);
    lecnam=itemView.findViewById(R.id.tvlecname);

    thumbnail=itemView.findViewById(R.id.thumbnail);
    lock=itemView.findViewById(R.id.imglock);
    previewbtn=itemView.findViewById(R.id.previewbtn);
    vidlength=itemView.findViewById(R.id.vidlength);
    viddate=itemView.findViewById(R.id.viddate);


    }
}
