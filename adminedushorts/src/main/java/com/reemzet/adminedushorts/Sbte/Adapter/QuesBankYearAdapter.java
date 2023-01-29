package com.reemzet.adminedushorts.Sbte.Adapter;

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

import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Model.QuesBankYearModel;

import java.util.ArrayList;

public class QuesBankYearAdapter extends RecyclerView.Adapter<QuesBankYearAdapter.QuesyearViewHolder> {

    ArrayList<QuesBankYearModel> list;
    Context context;
    String yearref;
    NavController navController;

    public QuesBankYearAdapter(ArrayList<QuesBankYearModel> list, Context context, String yearref, NavController navController) {
        this.list = list;
        this.context = context;
        this.yearref = yearref;
        this.navController = navController;
    }

    @NonNull
    @Override
    public QuesyearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuesyearViewHolder(LayoutInflater.from(context).inflate(R.layout.queyearlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuesyearViewHolder holder, int position) {
        NavHostFragment navHostFragment =
                (NavHostFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        QuesBankYearModel model = list.get(position);
        holder.tvyear.setText(model.getYearname());
        holder.tvnoofcontent.setText(model.getNoofcontent());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();

            bundle.putString("year", yearref + "/" + model.getYearname());
            navController.navigate(R.id.questionlistFrag, bundle);

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuesyearViewHolder extends RecyclerView.ViewHolder {
        TextView tvyear, tvnoofcontent;

        public QuesyearViewHolder(@NonNull View itemView) {
            super(itemView);
            tvyear = itemView.findViewById(R.id.tvyear);
            tvnoofcontent = itemView.findViewById(R.id.tvnoofcontent);

        }
    }
}
