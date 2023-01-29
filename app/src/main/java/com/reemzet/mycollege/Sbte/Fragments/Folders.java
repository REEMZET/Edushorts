package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Adapters.SubjectfolderAdapter;
import com.reemzet.mycollege.Sbte.Models.SubjectFolderModels;

import java.util.ArrayList;


public class Folders extends Fragment {
    RecyclerView recyclerView;
    NavController navController;
    SubjectfolderAdapter adapter;
    String streamkey,foldertype;
    DatabaseReference folderref;
    ShimmerFrameLayout shimmerFrameLayout;
    ImageView courseimage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_folders, container, false);

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        recyclerView = view.findViewById(R.id.folderrecyclerview);
        courseimage=view.findViewById(R.id.courseimage);

        streamkey=getArguments().getString("streamkey");
        foldertype=getArguments().getString("foldertype");
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
                                .into(courseimage);
                    }else {
                        list.add(new SubjectFolderModels(snapshot.getKey(), snapshot.getChildrenCount() + " items contain"));
                        adapter = new SubjectfolderAdapter(list, getContext(),streamkey,foldertype);
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



        return view;
    }

}