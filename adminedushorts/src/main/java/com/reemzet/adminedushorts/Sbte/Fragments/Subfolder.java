package com.reemzet.adminedushorts.Sbte.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Adapter.SubFolderAdapter;
import com.reemzet.adminedushorts.Sbte.Model.SubFolderModels;


import java.util.ArrayList;


public class Subfolder extends Fragment {


RecyclerView subrecycler;
FirebaseDatabase database;
DatabaseReference subfolderref;
String subfolder;
ArrayList<SubFolderModels> list;
SubFolderAdapter adapter;
FloatingActionButton additemfloat;
NavController navController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subfolder, container, false);
        subrecycler=view.findViewById(R.id.subfolderrecycler);
        additemfloat=view.findViewById(R.id.additemfloat);
        database=FirebaseDatabase.getInstance();
        subfolder=getArguments().getString("subfolder");
        subfolderref=database.getReference("MyCollege/Sbte/Learning/").child(subfolder);
        list = new ArrayList<>();

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        additemfloat.setOnClickListener(view1 -> {
            Bundle bundle=new Bundle();
            bundle.putString("dbref","MyCollege/Sbte/Learning/"+subfolder);
            navController.navigate(R.id.action_subfolder_to_addItems,bundle);
        });
        setrecyclerview();


   return view;
    }

    private void setrecyclerview() {
        subfolderref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(new SubFolderModels(snapshot.getKey(),snapshot.getChildrenCount() + " items contain"));
                adapter=new SubFolderAdapter(list,subfolder);
                subrecycler.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}