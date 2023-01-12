package com.reemzet.mycollege.Sbte.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Models.SubjectFolderModels;

import java.util.ArrayList;

public class SubjectfolderAdapter extends RecyclerView.Adapter<SubjectfolderAdapter.subjectfolderviewholder> {

    ArrayList<SubjectFolderModels> list;
    Context context;
    String folderref;
    NavController navController;

    public SubjectfolderAdapter(ArrayList<SubjectFolderModels> list, Context context, String folderref) {
        this.list = list;
        this.context = context;
        this.folderref = folderref;
    }

    @NonNull
    @Override
    public subjectfolderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.folderlayout, parent, false);
        return new subjectfolderviewholder(rview);
    }

    @Override
    public void onBindViewHolder(@NonNull subjectfolderviewholder holder, int position) {
        NavHostFragment navHostFragment =
                (NavHostFragment) ((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        SubjectFolderModels model = list.get(position);
        holder.foldername.setText(model.getFoldername());
        holder.itemcount.setText(model.getNocontent());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle=new Bundle();

           bundle.putString("folder",folderref+"/"+model.getFoldername());
            navController.navigate(R.id.videosListFrag,bundle);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class subjectfolderviewholder extends RecyclerView.ViewHolder
    {
        public    TextView foldername,itemcount;
        public subjectfolderviewholder(@NonNull View itemView)
        {
            super(itemView);
            foldername=itemView.findViewById(R.id.foldernametv);
            itemcount=itemView.findViewById(R.id.itemcount);
        }
    }
}
