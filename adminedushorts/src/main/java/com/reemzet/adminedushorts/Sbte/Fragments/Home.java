package com.reemzet.adminedushorts.Sbte.Fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.adminedushorts.Model.UserModels;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Adapter.ViewPagerAdapter;
import com.reemzet.adminedushorts.Sbte.Model.ViewpagerModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Home extends Fragment {
 private static Uri saveImage(Bitmap image, Context context) {

        File imageFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;
        try {
            imageFolder.mkdirs();
            File file = new File(imageFolder, "shared_images.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(Objects.requireNonNull(context.getApplicationContext()),
                    "com.reemzet.mycollege" + ".provider", file);

        } catch (IOException e) {

        }
        return uri;
    }
    LottieAnimationView learninganim;
    Handler slideHandler = new Handler();

    ViewPager2 viewPager2;
    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<ViewpagerModel> viewpagerModelArrayList;
    FirebaseDatabase database;
    DatabaseReference slider,userref;
    NavController navController;
    LinearLayout syllabus,lllearning,quesbanklayout,librarylayout,layoutlab;
    ScrollView scrollView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    TextView callbtn;
    TextView tvcoin;
    FirebaseAuth mAuth;
    LinearLayout chat;
    NavigationView navigationView;
     BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    ImageView imageshareview;
    UserModels userModels;
    LinearLayout invite,linearchat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        callbtn=view.findViewById(R.id.tvcallbtn);
        learninganim = view.findViewById(R.id.learninganim);
        viewPager2 = view.findViewById(R.id.sliderviewpager);
        syllabus = view.findViewById(R.id.syllabus);
        scrollView = view.findViewById(R.id.scrollview);
        lllearning=view.findViewById(R.id.lllearning);
        quesbanklayout=view.findViewById(R.id.Quebanklayout);
        librarylayout=view.findViewById(R.id.librarylayout);
        layoutlab=view.findViewById(R.id.layoutlab);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        database = FirebaseDatabase.getInstance();
        slider = database.getReference("MyCollege/Sbte/Poster");
        userref=database.getReference("MyCollege/Users");
        mAuth=FirebaseAuth.getInstance();
        drawerLayout=getActivity().findViewById(R.id.drawer);

        navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        chat=headerView.findViewById(R.id.ll_chat);
        imageshareview=view.findViewById(R.id.imageshare);
        invite=view.findViewById(R.id.invite);
        linearchat=view.findViewById(R.id.linearchat);


        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        scrollView.fullScroll(View.FOCUS_DOWN);
        scrollView.setSmoothScrollingEnabled(true);
        slidermethod();
        Bundle bundle=new Bundle();
        syllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("menu","syllabus");
                navController.navigate(R.id.action_home3_to_chooseStream,bundle);
            }
        });
        lllearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 bundle.putString("menu","learning");
                navController.navigate(R.id.action_home3_to_chooseStream,bundle);
            }
        });
        quesbanklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bundle.putString("menu","quesbank");
                navController.navigate(R.id.action_home3_to_chooseStream,bundle);
            }
        });
        librarylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 bundle.putString("menu","library");
                navController.navigate(R.id.action_home3_to_chooseStream,bundle);
            }
        });
        layoutlab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 bundle.putString("menu","lab");
                navController.navigate(R.id.action_home3_to_chooseStream,bundle);
            }
        });
        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent my_callIntent = new Intent(Intent.ACTION_DIAL);
                    my_callIntent.setData(Uri.parse("tel:"+"9525581574"));
                    startActivity(my_callIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "Error in your phone call"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.groupChat);
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
        linearchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  navController.navigate(R.id.groupChat);
            }
        });

        checkuserexist();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setNavigationIcon(null);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        getuserbalance();
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
    public void checkuserexist() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            navController.popBackStack();
            navController.navigate(R.id.signup);
        }
    }
    public void getuserbalance(){
        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModels=snapshot.getValue(UserModels.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void invite() {
        imageshareview.setImageDrawable(getResources().getDrawable(R.drawable.refer));
        bitmapDrawable = (BitmapDrawable) imageshareview.getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri bmpuri;
        int happy = 0x1F929;
        int hand = 0x1F449;
        String text = "Download *Edushorts* App for your Study and get some free credit by using my referal code " + getEmojiByUnicode(hand) + " *" +
                userModels.getReferalcode() + "*" + " this app is useful for AKU/SBTE/BSEB/CBSE boards"
                + getEmojiByUnicode(hand) + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + "&hl=it";
        bmpuri = saveImage(bitmap, getActivity());
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM, bmpuri);
        share.putExtra(Intent.EXTRA_SUBJECT, "share App");
        share.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(share, "Share to"));
    }
    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

}