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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Model.PdfModel;
import com.reemzet.adminedushorts.Sbte.ViewHolder.PdfListViewHolder;


import java.io.File;


public class LabPracticalList extends Fragment {

        FirebaseDatabase database;
    DatabaseReference librarypdfref;
   String streamkey;
   RecyclerView recyclerView;
     FirebaseRecyclerAdapter<PdfModel, PdfListViewHolder> adapter;
     NavController navController;
     ShimmerFrameLayout shimmerFrameLayout;
    PdfListViewHolder vholder;
    File folder;
    Toolbar toolbar;
    FloatingActionButton addlabtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lab_practical_list, container, false);

 streamkey=getArguments().getString("streamkey");
          folder = getContext().getFilesDir();
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
            database=FirebaseDatabase.getInstance();
            librarypdfref=database.getReference("MyCollege/Sbte/Lab").child(streamkey);
            recyclerView=view.findViewById(R.id.librarylistrecycler);
            addlabtn=view.findViewById(R.id.addlabbtn);
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        setRecyclerView();
    toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(streamkey);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });


        addlabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putString("dbref","MyCollege/Sbte/Lab/"+streamkey);
                navController.navigate(R.id.addItems,bundle);
            }
        });

    return view;
    }
     public void setRecyclerView(){
         FirebaseRecyclerOptions<PdfModel> options =
                new FirebaseRecyclerOptions.Builder<PdfModel>()
                        .setQuery(librarypdfref, PdfModel.class)
                        .build();

       adapter = new FirebaseRecyclerAdapter<PdfModel, PdfListViewHolder>(options) {
             @NonNull
             @Override
             public PdfListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 return new PdfListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.questionlistlayout, parent, false));
             }

             @Override
             protected void onBindViewHolder(@NonNull PdfListViewHolder holder, int position, @NonNull PdfModel model) {
                 vholder = holder;
                 holder.pdfname.setText(model.getItemtitle());
                 shimmerFrameLayout.stopShimmer();
                 shimmerFrameLayout.setVisibility(View.GONE);
                 recyclerView.setVisibility(View.VISIBLE);

                 if (new File(folder.getAbsoluteFile(), "lab"+model.getItemtitle()).exists()) {
                     holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                 } else {
                     holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.down));
                 }
                 holder.imgdownload.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (new File(folder.getAbsoluteFile(), "lab"+model.getItemtitle()).exists()) {
                             new AlertDialog.Builder(getActivity())
                                     .setTitle("Alert!")
                                     .setMessage("Do you want to Delete?")
                                     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int which) {
                                             File file = (new File(folder.getAbsoluteFile(), "lab"+model.getItemtitle()));
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
                             downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "lab"+model.getItemtitle());


                         }

                     }
                 });


                 holder.itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if (new File(folder.getAbsoluteFile(), "lab"+model.getItemtitle()).exists()) {
                             Bundle bundle = new Bundle();
                             bundle.putString("file","lab"+ model.getItemtitle());
                             navController.navigate(R.id.pdfViewer, bundle);
                         } else {
                             holder.loadanim.setVisibility(View.VISIBLE);
                             downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "lab"+model.getItemtitle());
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

                        if (LabPracticalList.this.isVisible()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", fileName);
                            vholder.loadanim.setVisibility(View.GONE);
                            vholder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                            navController.navigate(R.id.action_labPracticalList_to_pdfViewer, bundle);
                        }


                    }

                    @Override
                    public void onError(Error error) {

                    }


                });
    }
}