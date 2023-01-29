package com.reemzet.mycollege.Sbte.Adapters;

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
import com.reemzet.mycollege.Sbte.Models.SubFolderModels;

import java.util.ArrayList;

public class SubFolderAdapter extends RecyclerView.Adapter<SubFolderAdapter.subfolderviewholder> {


    ArrayList<SubFolderModels>subFolderModelsArrayList;
    NavController navController;
    String subfolder;

    public SubFolderAdapter(ArrayList<SubFolderModels> subFolderModelsArrayList, String subfolder) {
        this.subFolderModelsArrayList = subFolderModelsArrayList;
        this.subfolder = subfolder;
    }

    @NonNull
    @Override
    public subfolderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new subfolderviewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.subfolderlayout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull subfolderviewholder holder, int position) {
        NavHostFragment navHostFragment =
                (NavHostFragment) ((FragmentActivity)holder.tvsubfoldername.getContext()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        SubFolderModels models=subFolderModelsArrayList.get(position);
        holder.tvitemcount.setText(models.getNocontent());
        holder.tvsubfoldername.setText(models.getFoldername());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("subfolder",subfolder+"/"+models.getFoldername());
                navController.navigate(R.id.videosListFrag,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subFolderModelsArrayList.size();
    }

    public class subfolderviewholder extends RecyclerView.ViewHolder{

        TextView tvsubfoldername,tvitemcount;
        public subfolderviewholder(@NonNull View itemView) {
            super(itemView);
            tvsubfoldername=itemView.findViewById(R.id.tvsubfoldername);
            tvitemcount=itemView.findViewById(R.id.tvitemcount);

        }
    }
}
