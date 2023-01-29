package com.reemzet.adminedushorts.Sbte.Fragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Adapter.SubjectfolderAdapter;
import com.reemzet.adminedushorts.Sbte.Model.SubjectFolderModels;


import java.util.ArrayList;


public class Folders extends Fragment {
    RecyclerView recyclerView;
    NavController navController;
    SubjectfolderAdapter adapter;
    String streamkey,foldertype;
    DatabaseReference folderref;
    ShimmerFrameLayout shimmerFrameLayout;
    FloatingActionButton additems;
    ImageView courseimg,editcourseimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_folders, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.folderrecyclerview);
        courseimg=view.findViewById(R.id.courseimg);
        editcourseimg=view.findViewById(R.id.editcourseimg);
        streamkey=getArguments().getString("streamkey");
        foldertype=getArguments().getString("foldertype");
        additems=view.findViewById(R.id.additemsbtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        shimmerFrameLayout.startShimmer();
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       folderref=database.getReference("MyCollege/Sbte/Learning/").child(streamkey).child(foldertype);


        ArrayList<SubjectFolderModels> list = new ArrayList<>();
            folderref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getKey().equals("imageurl")){
                        Glide.with(getContext()).load(snapshot.getValue(String.class))
                                .into(courseimg);
                    }else {
                        list.add(new SubjectFolderModels(snapshot.getKey(), snapshot.getChildrenCount() + " Items contain"));
                        adapter = new SubjectfolderAdapter(list, getContext(),streamkey+"/"+foldertype);
                        recyclerView.setAdapter(adapter);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }


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

            additems.setOnClickListener(view1 -> {
                DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(R.layout.createfolderlayout))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .create();
                dialog.show();
                View myview = dialog.getHolderView();
                EditText etfoldername=myview.findViewById(R.id.etfoldername);
                Button createbtn=myview.findViewById(R.id.createfolder);
                createbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!etfoldername.getText().toString().isEmpty()){
                            folderref.child(etfoldername.getText().toString()).setValue("");
                            dialog.dismiss();
                        }else {
                            etfoldername.setError("cant be empty");
                            etfoldername.requestFocus();
                        }
                    }
                });

            });

            editcourseimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogPlus dialog = DialogPlus.newDialog(getContext())
                            .setContentHolder(new ViewHolder(R.layout.changecourseimg))
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .create();
                    dialog.show();
                    View myview = dialog.getHolderView();
                    EditText eturl=myview.findViewById(R.id.eturl);
                    Button createbtn=myview.findViewById(R.id.changeimgbtn);
                    createbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!eturl.getText().toString().isEmpty()){
                                folderref.child("imageurl").setValue(eturl.getText().toString());
                                dialog.dismiss();
                            }else {
                                eturl.setError("cant be empty");
                                eturl.requestFocus();
                            }
                        }
                    });
                }
            });

        return view;
    }

}