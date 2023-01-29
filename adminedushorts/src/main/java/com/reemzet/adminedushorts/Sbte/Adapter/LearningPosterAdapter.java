package com.reemzet.adminedushorts.Sbte.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Model.LearningposterModel;

import java.util.ArrayList;

public class LearningPosterAdapter extends RecyclerView.Adapter<LearningPosterAdapter.Myviewholder> {
    Context context;
    ArrayList<LearningposterModel> learningposterModelArrayList;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            learningposterModelArrayList.addAll(learningposterModelArrayList);
            notifyDataSetChanged();
        }
    };
    ViewPager2 viewPager2;

    public LearningPosterAdapter(Context context, ArrayList<LearningposterModel> learningposterModelArrayList, ViewPager2 viewPager2) {
        this.context = context;
        this.learningposterModelArrayList = learningposterModelArrayList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.learningposter, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        LearningposterModel model = learningposterModelArrayList.get(position);
        Glide.with(context).load(model.getImgurl())
                .into(holder.imageView);
        holder.learningmsg.setText(model.getPostermsg());
        holder.learningmsg.setTextColor(Color.parseColor(model.getTextcolor()));
        holder.cardView.setCardBackgroundColor(Color.parseColor(model.getBgcolor()));
        if (position == learningposterModelArrayList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return learningposterModelArrayList.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView learningmsg;
        CardView cardView;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.learningimg);
            learningmsg = itemView.findViewById(R.id.learningmsg);
            cardView = itemView.findViewById(R.id.learningbg);


        }
    }

}