package com.reemzet.mycollege.Sbte.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.auth.User;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Models.VideoModel;
import com.reemzet.mycollege.Sbte.ViewHolders.VideoslistAdapter;

public class VideosListFrag extends Fragment {
    String folderref;
RecyclerView videoslisterecycleview;
FirebaseRecyclerAdapter<VideoModel, VideoslistAdapter> adapter;
DatabaseReference subjectdataref;
    NavController navController;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    LottieAnimationView animationView;
    User user;
    private DefaultTrackSelector trackSelector;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    TextView tvtimer,tvpreview1,tvpreview2;
    ShimmerFrameLayout shimmerFrameLayout;


    String aniurl="https://firebasestorage.googleapis.com/v0/b/live-classes-fc805.appspot.com/o/lottiefiles%2Fstudy.json?alt=media&token=5d913822-04e3-479a-b9c4-0f5c4868ecc4";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_videos_list, container, false);
       folderref= getArguments().getString("folder");
        animationView=view.findViewById(R.id.animationView);
        playerView = view.findViewById(R.id.exoPlayerView);
       videoslisterecycleview=view.findViewById(R.id.videoslistrecycler);
       tvtimer=view.findViewById(R.id.tvtimer);
       tvpreview1=view.findViewById(R.id.tvpreview1);
       tvpreview2=view.findViewById(R.id.tvpreview2);
       shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
       shimmerFrameLayout.startShimmer();
        subjectdataref=FirebaseDatabase.getInstance().getReference("MyCollege/Sbte/Learning").child(folderref);
        videoslisterecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));


        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        firebaseAuth=FirebaseAuth.getInstance();

        animationView.setAnimationFromUrl(aniurl);
        showloding();



        getvideosdata();



        return view;
    }

    public void getvideosdata() {
        FirebaseRecyclerOptions<VideoModel> options =
                new FirebaseRecyclerOptions.Builder<VideoModel>()
                        .setQuery(subjectdataref, VideoModel.class)
                        .build();

        adapter= new FirebaseRecyclerAdapter<VideoModel, VideoslistAdapter>(options){

            @NonNull
            @Override
            public VideoslistAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View rview = LayoutInflater.from(parent.getContext()).inflate(R.layout.videoslistlayout, parent, false);
                return new VideoslistAdapter(rview);
            }

            @Override
            protected void onBindViewHolder(@NonNull VideoslistAdapter holder, int position, @NonNull VideoModel model) {
                holder.lecnam.setText(model.getVideotitle());
               //holder.viewcount.setText(model.getVideoviews());

               if (true){
                   holder.lock.setImageDrawable(getResources().getDrawable(R.drawable.unlock));
               }else {
                   holder.lock.setImageDrawable(getResources().getDrawable(R.drawable.padlock));
               }
               holder.previewbtn.setOnClickListener(v -> {
                   releasePlayer();
                   loadpreview(model.getVideourl());

               });
                holder.itemView.setOnClickListener(v -> {
                    if (true){
                        Bundle bundle=new Bundle();
                        bundle.putString("videourl", model.getVideourl());
                        navController.navigate(R.id.player,bundle);
                    }else
                       Snackbar.make( v,"please contact 9100101210 to unlock", BaseTransientBottomBar.LENGTH_SHORT).show();

                });
                holder.vidlength.setText(model.getVideolength());
                holder.viddate.setText(model.getVideodesc());

               progressDialog.dismiss();
            }

        };

        videoslisterecycleview.setAdapter(adapter);
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        videoslisterecycleview.setVisibility(View.VISIBLE);
        adapter.startListening();


    }
  /*  public boolean authuser(){
        if (user.getAccounttype().equals("verified")||(user.getAccounttype().equals("Admin"))){
            return true;
        }else {
            return false;
        }
    }*/
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

}