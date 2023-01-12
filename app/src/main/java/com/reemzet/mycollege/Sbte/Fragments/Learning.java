package com.reemzet.mycollege.Sbte.Fragments;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Adapters.LearningPosterAdapter;
import com.reemzet.mycollege.Sbte.Adapters.ViewPagerAdapter;
import com.reemzet.mycollege.Sbte.Models.LearningposterModel;
import com.reemzet.mycollege.Sbte.Models.SyllabusModel;
import com.reemzet.mycollege.Sbte.Models.VideoModel;
import com.reemzet.mycollege.Sbte.Models.ViewpagerModel;
import com.reemzet.mycollege.Sbte.ViewHolders.SyllabusViewHolder;
import com.reemzet.mycollege.Sbte.ViewHolders.VideoViewHolder;

import java.util.ArrayList;


public class Learning extends Fragment {
    RecyclerView recentrecyclerview;
    FirebaseDatabase database;
    DatabaseReference learningrecentvid,learningposterref;
    FirebaseRecyclerAdapter<VideoModel, VideoViewHolder> videoadapter;
    NavController navController;
    Query query;
    Handler slideHandler = new Handler();
    String streamkey;
    Toolbar toolbar;
    LinearLayout linearvideos;

    ViewPager2 viewPager2;
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<LearningposterModel> learningposterModelArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_learning, container, false);
        recentrecyclerview=view.findViewById(R.id.recentrecyclerview);
        viewPager2 = view.findViewById(R.id.learningviewpager);
        shimmerFrameLayout = view.findViewById(R.id.shimmerlearning);
        linearvideos=view.findViewById(R.id.layoutvideos);
        streamkey=getArguments().getString("streamkey");

        database=FirebaseDatabase.getInstance();
                NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        learningrecentvid=database.getReference("MyCollege/Sbte/Learning/").child(streamkey).child("videos").child("Maths-3");
        learningposterref=database.getReference("MyCollege/Sbte/Learning/poster");


        String id=learningrecentvid.push().getKey();
        VideoModel videoModel=new VideoModel(id,"Transformer","working,diagram,Construction","http://content.jwplatform.com/manifests/vM7nH0Kl.m3u8","0","04:00 min","https://i.ytimg.com/vi/UB8hN8QhCIw/maxresdefault.jpg","20");
        //learningrecentvid.child(id).setValue(videoModel);



        loadrecentlyadded();
        slidermethod();


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Learning");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });

        Bundle bundle=new Bundle();
        linearvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("streamkey",streamkey);
                bundle.putString("foldertype","videos");
                navController.navigate(R.id.action_learning_to_folders,bundle);
            }
        });






    return view;
    }

    public void loadrecentlyadded(){
          query = learningrecentvid;
        FirebaseRecyclerOptions<VideoModel> options =
                new FirebaseRecyclerOptions.Builder<VideoModel>()
                        .setQuery(query, VideoModel.class)
                        .build();

        videoadapter= new FirebaseRecyclerAdapter<VideoModel, VideoViewHolder>(options) {
            @NonNull
            @Override
            public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentlyaddedlayout, parent, false);
                return new VideoViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VideoViewHolder holder, int position, @NonNull VideoModel model) {
                holder.videotitle.setText(model.getVideotitle());
                holder.videodesc.setText(model.getVideodesc());
            }
        };
        recentrecyclerview.setAdapter(videoadapter);
        videoadapter.startListening();
    }


      public void slidermethod() {

       learningposterModelArrayList = new ArrayList<>();
        learningposterref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              learningposterModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                   LearningposterModel postermodel = snapshot1.getValue(LearningposterModel.class);
                   learningposterModelArrayList.add(postermodel);
                }
                viewPager2.setAdapter(new LearningPosterAdapter(getContext(), learningposterModelArrayList, viewPager2));
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                viewPager2.setVisibility(View.VISIBLE);
                viewPager2.setClipToPadding(false);
                viewPager2.setClipChildren(false);
                viewPager2.setOffscreenPageLimit(3);
                viewPager2.setCurrentItem(3, true);
                viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
                CompositePageTransformer transformer = new CompositePageTransformer();
                transformer.addTransformer(new ViewPager2.PageTransformer() {
                    @Override
                    public void transformPage(@NonNull View page, float position) {
                        float a = 1 - Math.abs(position);
                        page.setScaleY(0.85f + a * 0.15f);
                    }
                });
                viewPager2.setPageTransformer(transformer);
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        slideHandler.removeCallbacks(sliderRunnable);
                        slideHandler.postDelayed(sliderRunnable, 3000);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}