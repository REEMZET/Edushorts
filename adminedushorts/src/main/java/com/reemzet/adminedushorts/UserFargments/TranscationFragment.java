package com.reemzet.adminedushorts.UserFargments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.adminedushorts.Model.TranscationModel;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.ViewHolder.TranscationViewHolder;


public class TranscationFragment extends Fragment {
    RecyclerView transcationrecyclerview;
    FirebaseRecyclerAdapter<TranscationModel, TranscationViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference transcationref;
    FirebaseAuth mAuth;
     NavController navController;
     ShimmerFrameLayout shimmerFrameLayout;
     String useramount;
     TextView tvamount;
     Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transcation, container, false);
        database= FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        transcationref=database.getReference("MyCollege/Users").child(mAuth.getUid()).child("Transcation");
        transcationrecyclerview=view.findViewById(R.id.trannscationrecyclerview);
        tvamount=view.findViewById(R.id.tvamount);
        useramount=getArguments().getString("amount","0");
        tvamount.setText(useramount);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);
       transcationrecyclerview.setLayoutManager(linearLayoutManager);
         shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        settranscation();
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

  toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

    return view;
    }

    private void settranscation() {
        FirebaseRecyclerOptions<TranscationModel> options =
                new FirebaseRecyclerOptions.Builder<TranscationModel>()
                        .setQuery(transcationref, TranscationModel.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<TranscationModel, TranscationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TranscationViewHolder holder, int position, @NonNull TranscationModel model) {
           shimmerFrameLayout.stopShimmer();
                 shimmerFrameLayout.setVisibility(View.GONE);
                 transcationrecyclerview.setVisibility(View.VISIBLE);
                 if (model.getTrantype().equals("Debit")){
                    holder.tvtranamount.setTextColor(getResources().getColor(R.color.red));
                    holder.tranimg.setImageDrawable(getResources().getDrawable(R.drawable.deducted));
                 }else {
                     holder.tranimg.setImageDrawable(getResources().getDrawable(R.drawable.credit));
                     holder.tvtranamount.setTextColor(getResources().getColor(R.color.green));
                 }
                    holder.tvtrantitle.setText(model.getTrantitle());
                    holder.tvtranamount.setText("\u20B9"+model.getTranamount());
                    holder.tvtrantype.setText(model.getTrantype());
                    holder.tvtranid.setText("Trans_id- "+model.getTranid());

            }

            @NonNull
            @Override
            public TranscationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new TranscationViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.historylayout,parent,false));

            }
        };
        transcationrecyclerview.setAdapter(adapter);
        adapter.startListening();
    }

}