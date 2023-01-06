package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Adapters.ViewPagerAdapter;
import com.reemzet.mycollege.Sbte.Models.ViewpagerModel;

import java.util.ArrayList;


public class Home extends Fragment {

    LottieAnimationView learninganim;
      Handler slideHandler = new Handler();
    Handler offerslideHandler = new Handler();
    ViewPager2 viewPager2;
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<ViewpagerModel> viewpagerModelArrayList;
    FirebaseDatabase database;
    DatabaseReference slider;
    NavController navController;
    LinearLayout syllabus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        learninganim=view.findViewById(R.id.learninganim);
         viewPager2 = view.findViewById(R.id.sliderviewpager);
         syllabus=view.findViewById(R.id.syllabus);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        database=FirebaseDatabase.getInstance();
          slider = database.getReference("MyCollege/Sbte/Poster");


          NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();



        slidermethod();
        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_home2_to_chooseStream);
            }
        });







   return view;



    }

     public void slidermethod() {

        viewpagerModelArrayList = new ArrayList<>();
        slider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                viewpagerModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ViewpagerModel postermodel = snapshot1.getValue(ViewpagerModel.class);
                    viewpagerModelArrayList.add(postermodel);
                }
                viewPager2.setAdapter(new ViewPagerAdapter(getContext(), viewpagerModelArrayList, viewPager2));
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
     private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
}