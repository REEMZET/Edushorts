package com.reemzet.adminedushorts.Sbte.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Model.ItemModels;
import com.reemzet.adminedushorts.Sbte.ViewHolder.PdfListViewHolder;


import java.io.File;

public class QuestionlistFrag extends Fragment {
RecyclerView recyclerView;
String yearref;
FirebaseDatabase database;
DatabaseReference dbyearref;
FirebaseRecyclerAdapter<ItemModels, PdfListViewHolder> adapter;
ShimmerFrameLayout shimmerFrameLayout;
PdfListViewHolder vholder;
NavController navController;
 File folder;
 Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_questionlist, container, false);
        recyclerView=view.findViewById(R.id.questionlistrecycler);
        folder = getContext().getFilesDir();
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        yearref=getArguments().getString("year");
        database=FirebaseDatabase.getInstance();
        dbyearref=database.getReference("MyCollege/Sbte/QuesBank").child(yearref);
         NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        setRecyclerView();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("select subject");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    return  view;
    }

    public void setRecyclerView(){
         FirebaseRecyclerOptions<ItemModels> options =
                new FirebaseRecyclerOptions.Builder<ItemModels>()
                        .setQuery(dbyearref, ItemModels.class)
                        .build();

        adapter=new FirebaseRecyclerAdapter<ItemModels, PdfListViewHolder>(options){
            @NonNull
            @Override
            public PdfListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PdfListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.questionlistlayout,parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PdfListViewHolder holder, int position, @NonNull ItemModels model) {
                vholder=holder;
                holder.pdfname.setText(model.getItemtitle());
                holder.tvitemdesc.setText(model.getItemdesc());
                holder.tvitemprice.setText("\u20B9"+model.getItemprice());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                if (new File(folder.getAbsoluteFile(), "ques"+model.getItemtitle()).exists()) {
                    holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                } else {
                    holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.down));
                }
                holder.imgdownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new File(folder.getAbsoluteFile(), "ques"+model.getItemtitle()).exists()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Do you want to Delete?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            File file = (new File(folder.getAbsoluteFile(), "ques"+model.getItemtitle()));
                                            file.delete();
                                            Toast.makeText(getActivity(), "File Deleted", Toast.LENGTH_SHORT).show();
                                            holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.down));
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        } else {
                            //showloding();
                            holder.loadanim.setVisibility(View.VISIBLE);
                            downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "ques"+model.getItemtitle());


                        }

                    }
                });



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (new File(folder.getAbsoluteFile(), "ques"+model.getItemtitle()).exists()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", "ques"+model.getItemtitle());
                            navController.navigate(R.id.pdfViewer, bundle);
                        } else {
                            holder.loadanim.setVisibility(View.VISIBLE);
                            downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "ques"+model.getItemtitle());
                        }

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
 public void downloadNotes(String url, String dirPath, String fileName) {
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

                        if (QuestionlistFrag.this.isVisible()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", fileName);
                            vholder.loadanim.setVisibility(View.GONE);
                            vholder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                            navController.navigate(R.id.action_questionlistFrag_to_pdfViewer, bundle);
                        }


                    }

                    @Override
                    public void onError(Error error) {

                    }





                });
    }
}