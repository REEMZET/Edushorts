package com.reemzet.mycollege.UserFargments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.Model.UserModels;
import com.reemzet.mycollege.R;


public class User_Profile extends Fragment {


        FirebaseDatabase database;
        DatabaseReference userref;
        FirebaseAuth mAuth;
        UserModels usermodels;
        TextView tvname,tvphone,tvcollege,tvsem,tvbranch,tvamount,tvreferal,tvpurchase,tvcity;
        Button updateprofilebtn;
        Toolbar toolbar;
        NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user__profile, container, false);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userref=database.getReference("MyCollege/Users").child(mAuth.getUid());
        tvname=view.findViewById(R.id.tvprofilename);
        tvphone=view.findViewById(R.id.tvphonenumber);
        tvsem=view.findViewById(R.id.tvsemname);
        tvcollege=view.findViewById(R.id.tvcollegename);
        tvamount=view.findViewById(R.id.tvbalance);
        tvreferal=view.findViewById(R.id.tvreferal);
        tvpurchase=view.findViewById(R.id.tvpurchase);
        tvbranch=view.findViewById(R.id.tvbranchname);
        tvcity=view.findViewById(R.id.tvcityname);
        updateprofilebtn=view.findViewById(R.id.updateprofilebtn);


        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        updateprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "this features is now unavailable", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });
        getpurchasecount();
        getuserData();



        return view;

    }
    public void getuserData(){
        userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usermodels=snapshot.getValue(UserModels.class);
                tvname.setText(usermodels.getUsername());
                tvreferal.setText(usermodels.getReferalcode());
                tvamount.setText("\u20B9"+usermodels.getAmount());
                tvbranch.setText("Branch_Name- "+usermodels.getUserbranch());
                tvsem.setText("Semester- "+usermodels.getUsersem());
                tvcollege.setText("College- "+usermodels.getUsercollege());
                tvphone.setText("Phone:- "+usermodels.getUserphone());
                tvcity.setText("City- "+usermodels.getUsercity());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void getpurchasecount(){
        userref.child("MyPurchases").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    tvpurchase.setText(String.valueOf(snapshot.getChildrenCount())+" items");
                }else {
                    tvpurchase.setText("0 item");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}