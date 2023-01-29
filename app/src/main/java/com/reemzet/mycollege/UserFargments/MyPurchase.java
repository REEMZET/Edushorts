package com.reemzet.mycollege.UserFargments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.mycollege.Model.PurchaseModel;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Fragments.LibraryList;
import com.reemzet.mycollege.Sbte.Models.ItemModels;
import com.reemzet.mycollege.ViewHolder.MyPurchaseViewHolder;

import java.io.File;


public class MyPurchase extends Fragment {

    FirebaseDatabase database;
    DatabaseReference mypurchaseref;
    FirebaseAuth mAuth;
    RecyclerView mypurchaserecyclerview;
    FirebaseRecyclerAdapter<PurchaseModel, MyPurchaseViewHolder> adapter;
    Toolbar toolbar;
    NavController navController;
    File folder;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_my_purchase, container, false);
        folder = getContext().getFilesDir();
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mypurchaseref=database.getReference("MyCollege/Users").child(mAuth.getUid()).child("MyPurchases");
        mypurchaserecyclerview=view.findViewById(R.id.mypurchaserecycelerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mypurchaserecyclerview.setLayoutManager(linearLayoutManager);
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        setMypurchaselist();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("MyPurchase");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    return view;
    }

    private void setMypurchaselist() {
        FirebaseRecyclerOptions<PurchaseModel> options =
                new FirebaseRecyclerOptions.Builder<PurchaseModel>()
                        .setQuery(mypurchaseref, PurchaseModel.class)
                        .build();
        adapter=new FirebaseRecyclerAdapter<PurchaseModel, MyPurchaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyPurchaseViewHolder holder, int position, @NonNull PurchaseModel model) {
                holder.tvproductid.setText("id"+model.getProductid());
                holder.tvpurchasedate.setText("purchase date-"+model.getPurchasedate());
                holder.tvprice.setText("Price-"+model.getProductprice());
                holder.tvprodcuctname.setText(model.getProductname());
                holder.tvvaildity.setText("vaild:-"+model.getProductvalidity());
               if (model.getProducttype().equals("Video")){
                   holder.productimg.setImageDrawable(getResources().getDrawable(R.drawable.video));
               }else  holder.productimg.setImageDrawable(getResources().getDrawable(R.drawable.pdfbook));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle=new Bundle();
                        if (model.getProducttype().equals("Video")){
                            bundle.putString("videourl", model.getProducturl());
                            navController.navigate(R.id.player,bundle);
                        }else if (model.getProducttype().equals("Pdf")){
                            if (new File(folder.getAbsoluteFile(), "mypur" + model.getProductname()).exists()) {
                                bundle.putString("file", "mypur" +model.getProductname());
                                navController.navigate(R.id.pdfViewer, bundle);
                            } else {
                                downloadNotes(model.getProducturl(), folder.getAbsolutePath(), "mypur" + model.getProductname());
                            }
                        }
                    }
                });

            }

            @NonNull
            @Override
            public MyPurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyPurchaseViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.mypurchaselayout,parent,false));
            }
        };
        mypurchaserecyclerview.setAdapter(adapter);
        adapter.startListening();



    }
    public void downloadNotes(String url, String dirPath, String fileName) {
        showloding();
        int downloadId = PRDownloader.download(url, dirPath, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                            progressDialog.dismiss();
                        if (MyPurchase.this.isVisible()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", fileName);
                            navController.navigate(R.id.pdfViewer, bundle);
                        }


                    }

                    @Override
                    public void onError(Error error) {
                    }
                });
    }
    public void showloding() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialoprogress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
    }
}