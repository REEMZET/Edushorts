package com.reemzet.adminedushorts.Sbte.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Adapter.QuesBankYearAdapter;
import com.reemzet.adminedushorts.Sbte.Model.QuesBankYearModel;


import java.util.ArrayList;


public class Quesbankyear extends Fragment {

    RecyclerView recyclerView;
    QuesBankYearAdapter adapter;
    FirebaseDatabase database;
    DatabaseReference quesyearref;
    String streamkey;
    NavController navController;
    ShimmerFrameLayout shimmerFrameLayout;
    Toolbar toolbar;
    FloatingActionButton additembtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quesbankyear, container, false);
        streamkey = getArguments().getString("streamkey");
        database = FirebaseDatabase.getInstance();
        quesyearref = database.getReference("MyCollege/Sbte/QuesBank").child(streamkey);
        recyclerView = view.findViewById(R.id.questionyearrecycler);
        additembtn=view.findViewById(R.id.additembtn);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        ArrayList<QuesBankYearModel> list = new ArrayList<>();
        quesyearref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(new QuesBankYearModel(snapshot.getKey(), snapshot.getChildrenCount() + " item contain"));
                adapter = new QuesBankYearAdapter(list, getContext(), streamkey+"/", navController);
                recyclerView.setAdapter(adapter);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
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
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(streamkey);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
        additembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putString("dbref","MyCollege/Sbte/QuesBank/"+streamkey);
                navController.navigate(R.id.addItems,bundle);
            }
        });

        return view;
    }
}