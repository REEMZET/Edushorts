package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Adapters.SubFolderAdapter;
import com.reemzet.mycollege.Sbte.Models.SubFolderModels;
import com.reemzet.mycollege.Sbte.Models.SubjectFolderModels;

import java.util.ArrayList;


public class Subfolder extends Fragment {


RecyclerView subrecycler;
FirebaseDatabase database;
DatabaseReference subfolderref;
String subfolder;
ArrayList<SubFolderModels> list;
SubFolderAdapter adapter;
ShimmerFrameLayout shimmerFrameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subfolder, container, false);
        subrecycler=view.findViewById(R.id.subfolderrecycler);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        database=FirebaseDatabase.getInstance();
        subfolder=getArguments().getString("subfolder");
        subfolderref=database.getReference("MyCollege/Sbte/Learning/").child(subfolder);
        list = new ArrayList<>();
        setrecyclerview();
        shimmerFrameLayout.startShimmer();



   return view;
    }

    private void setrecyclerview() {
        subfolderref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(new SubFolderModels(snapshot.getKey(),snapshot.getChildrenCount() + " items contain"));
                adapter=new SubFolderAdapter(list,subfolder);
                subrecycler.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                subrecycler.setVisibility(View.VISIBLE);
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