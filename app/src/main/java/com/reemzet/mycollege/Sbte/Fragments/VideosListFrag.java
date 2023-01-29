package com.reemzet.mycollege.Sbte.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.reemzet.mycollege.Model.TranscationModel;
import com.reemzet.mycollege.Model.UserModels;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Models.ItemModels;
import com.reemzet.mycollege.Model.PurchaseModel;
import com.reemzet.mycollege.Sbte.ViewHolders.VideoslistAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class VideosListFrag extends Fragment {
    String folderref;
    RecyclerView videoslisterecycleview;
    FirebaseRecyclerAdapter<ItemModels, VideoslistAdapter> adapter;
    DatabaseReference subjectdataref,userref,admintransref;
    NavController navController;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    LottieAnimationView animationView;
    private DefaultTrackSelector trackSelector;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView tvtimer,tvpreview1,tvpreview2;
    ShimmerFrameLayout shimmerFrameLayout;
    UserModels userModels;
    FirebaseAuth mAuth;
    ArrayList<String>purchaseModelArrayList;
    String urlStream;

    String aniurl="https://firebasestorage.googleapis.com/v0/b/live-classes-fc805.appspot.com/o/lottiefiles%2Fstudy.json?alt=media&token=5d913822-04e3-479a-b9c4-0f5c4868ecc4";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_videos_list, container, false);
        folderref= getArguments().getString("subfolder");
        animationView=view.findViewById(R.id.animationView);
        playerView = view.findViewById(R.id.exoPlayerView);
         videoslisterecycleview=view.findViewById(R.id.videoslistrecycler);
        tvtimer=view.findViewById(R.id.tvtimer);
        tvpreview1=view.findViewById(R.id.tvpreview1);
        tvpreview2=view.findViewById(R.id.tvpreview2);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        mAuth=FirebaseAuth.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        subjectdataref=FirebaseDatabase.getInstance().getReference("MyCollege/Sbte/Learning").child(folderref);
        userref=FirebaseDatabase.getInstance().getReference("MyCollege/Users").child(mAuth.getUid());
        admintransref=FirebaseDatabase.getInstance().getReference("MyCollege/Admin/Transaction");
        videoslisterecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));


        purchaseModelArrayList=new ArrayList<>();
        String id=subjectdataref.push().getKey();
        ItemModels models=new ItemModels("Introduction to Digital Electronics","http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4","Introduction to digital electronics","10:00","0",id,"Video","25 jan 2023","0","no");
        //subjectdataref.child(id).setValue(models);

        getUserData();

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        animationView.setAnimationFromUrl(aniurl);

        showloding();
        getPurchaseItem();
        return view;
    }

    private void getPurchaseItem() {
        userref.child("MyPurchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchaseModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PurchaseModel model = snapshot1.getValue(PurchaseModel.class);
                    purchaseModelArrayList.add(model.getProductid());
                }

                getvideosdata();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getvideosdata() {
        FirebaseRecyclerOptions<ItemModels> options =
                new FirebaseRecyclerOptions.Builder<ItemModels>()
                        .setQuery(subjectdataref, ItemModels.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<ItemModels, VideoslistAdapter>(options){

            @NonNull
            @Override
            public VideoslistAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.videoslistlayout, parent, false);
                return new VideoslistAdapter(rview);
            }

            @Override
            protected void onBindViewHolder(@NonNull VideoslistAdapter holder, int position, @NonNull ItemModels model) {

                    if (purchaseModelArrayList.contains(model.getItemid())){
                        holder.tvprice.setText("purchased");
                        holder.buybtn.setVisibility(View.INVISIBLE);
                    }else {
                        holder.tvprice.setText("\u20B9"+model.getItemprice());
                        holder.buybtn.setVisibility(View.VISIBLE);
                    }
                    if (model.getItemprice().equals("0")){
                        holder.tvprice.setText("free");
                        holder.buybtn.setVisibility(View.INVISIBLE);
                    }
                holder.lecnam.setText(model.getItemtitle());
                holder.previewbtn.setOnClickListener(v -> {
                    releasePlayer();
                    loadpreview(model.getItemurl());


                });
                holder.itemView.setOnClickListener(v -> {
                    if (holder.tvprice.getText().equals("free") || holder.tvprice.getText().equals("purchased")){
                        Bundle bundle=new Bundle();
                        bundle.putString("videourl",model.getItemurl());
                        navController.navigate(R.id.player,bundle);
                    }else {
                        Snackbar.make( v,"please purchase first", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
                    holder.buybtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Purchase Video")
                                    .setMessage("Do you want to purchase this item? Rs"+model.getItemprice()+ "will debit from wallet.")
                                    .setPositiveButton("buy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            int remainbalance=Integer.parseInt(userModels.getAmount())-Integer.parseInt(model.getItemprice());
                                            if (remainbalance<0){
                                                Toast.makeText(getContext(), "please recharge wallet", Toast.LENGTH_SHORT).show();
                                            }else {
                                                showloding();
                                                userref.child("amount").setValue(String.valueOf(remainbalance)).addOnSuccessListener(
                                                        new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Date d = new Date();
                                                                CharSequence s = DateFormat.format("MMM d, yyyy ", d.getTime());
                                                               String currentTime =new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                                                TranscationModel transcationModel=new TranscationModel("Purchase video","Debit", model.getItemprice(), model.getItemid(),(String) s,currentTime,mAuth.getUid());
                                                                userref.child("Transcation").push().setValue(transcationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        admintransref.push().setValue(transcationModel);
                                                                        PurchaseModel purchaseModel = new PurchaseModel(model.getItemid(), model.getItemtitle(), model.getItemurl(),model.getItemtype(),(String) s, "6 months", model.getItemprice());
                                                                        userref.child("MyPurchases").child(model.getItemid()).setValue(purchaseModel).addOnSuccessListener(
                                                                                new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void unused) {
                                                                                        holder.buybtn.setVisibility(View.INVISIBLE);
                                                                                        holder.tvprice.setText("purchased");
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                }
                                                                        );
                                                                    }
                                                                });

                                                            }
                                                        }
                                                );

                                            }
                                        }
                                    }).setNegativeButton("No", null).show();
                        }
                    });


                holder.vidlength.setText(model.getItemlength());
                holder.viddate.setText(model.getItemdesc());

                Glide.with(getActivity()).load(model.getItemthumbnail())
                        .placeholder(R.drawable.video)
                        .into(holder.thumbnail);
               progressDialog.dismiss();
            }

        };

        videoslisterecycleview.setAdapter(adapter);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        videoslisterecycleview.setVisibility(View.VISIBLE);
        adapter.startListening();


    }

    public void showloding() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialoprogress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public void loadpreview(String url){
        animationView.setVisibility(View.GONE);
        tvtimer.setVisibility(View.VISIBLE);
        tvpreview2.setVisibility(View.VISIBLE);
        tvpreview1.setVisibility(View.VISIBLE);
        trackSelector = new DefaultTrackSelector(getActivity());
        simpleExoPlayer = new SimpleExoPlayer.Builder(getActivity()).setTrackSelector(trackSelector).build();
        playerView.setPlayer(simpleExoPlayer);
        playerView.setVisibility(View.VISIBLE);
        playerView.setUseController(false);
        MediaItem mediaItem = MediaItem.fromUri(url);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
        simpleExoPlayer.seekTo(6000);
        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying){
                    new CountDownTimer(60000,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tvtimer.setText(millisUntilFinished / 1000+"sec");
                        }

                        @Override
                        public void onFinish() {
                            releasePlayer();
                            animationView.setVisibility(View.VISIBLE);
                            playerView.setVisibility(View.GONE);
                            tvtimer.setVisibility(View.GONE);
                            tvpreview1.setVisibility(View.GONE);
                            tvpreview2.setVisibility(View.GONE);
                        }
                    }.start();
                }
            }
        });



        }

    protected void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer.stop();
            simpleExoPlayer = null;
            trackSelector = null;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
    public void getUserData(){
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModels=snapshot.getValue(UserModels.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}