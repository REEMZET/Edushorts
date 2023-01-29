package com.reemzet.mycollege.Sbte.Fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Models.SyllabusModel;
import com.reemzet.mycollege.Sbte.ViewHolders.SyllabusViewHolder;

import java.io.File;
import java.util.ArrayList;


public class SubjectList extends Fragment {

    FirebaseDatabase database;
    DatabaseReference syllabusref,civil,ec1sem,cs2sem,eesem2,me2sem;
    RecyclerView syllabusrecyclerview;
    Query query;
    File folder;
    FirebaseRecyclerAdapter<SyllabusModel, SyllabusViewHolder> syllabusadapter;
    NavController navController;
    SyllabusViewHolder vholder;
    String streamkey;
    Toolbar toolbar;
  SyllabusModel syllabusModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
        syllabusrecyclerview = view.findViewById(R.id.syllabusrecyclerview);

        streamkey = getArguments().getString("streamkey");

        syllabusrecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        database = FirebaseDatabase.getInstance();
        syllabusref = database.getReference("MyCollege/Sbte/syllabus/").child(streamkey);
        civil=database.getReference("MyCollege/Sbte/syllabus/Semester-1Civil Engineering");
        ec1sem=database.getReference("MyCollege/Sbte/syllabus/Semester-1Electronics Engineering");
        cs2sem=database.getReference("MyCollege/Sbte/syllabus/Semester-2Computer Science Engineering");
        eesem2=database.getReference("MyCollege/Sbte/syllabus/Semester-2Electrical Engineering");
        me2sem=database.getReference("MyCollege/Sbte/syllabus/Semester-2Mechnical Engineering");
        PRDownloader.initialize(getActivity());
        folder = getContext().getFilesDir();
        loadsyllabus();





        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(streamkey);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });




        return view;
    }

    public void loadsyllabus() {
        query = syllabusref;
        FirebaseRecyclerOptions<SyllabusModel> options =
                new FirebaseRecyclerOptions.Builder<SyllabusModel>()
                        .setQuery(query, SyllabusModel.class)
                        .build();


        syllabusadapter = new FirebaseRecyclerAdapter<SyllabusModel, SyllabusViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SyllabusViewHolder holder, int position, @NonNull SyllabusModel model) {
                vholder = holder;
                Glide.with(getActivity())
                        .load(model.getSubimg())
                        .centerCrop()
                        .placeholder(R.drawable.pdfbook)
                        .into(holder.subimg);


                if (new File(folder.getAbsoluteFile(), model.getSubcode()).exists()) {
                    holder.imgdown.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                } else {
                    holder.imgdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                }
                holder.imgdown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new File(folder.getAbsoluteFile(), model.getSubcode()).exists()) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Do you want to Delete?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            File file = (new File(folder.getAbsoluteFile(), model.getSubcode()));
                                            file.delete();
                                            Toast.makeText(getActivity(), "File Deleted", Toast.LENGTH_SHORT).show();
                                            holder.imgdown.setImageDrawable(getResources().getDrawable(R.drawable.down));
                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        } else {
                            //showloding();
                            holder.loadpdf.setVisibility(View.VISIBLE);
                            downloadNotes(model.getSubpdf(), folder.getAbsolutePath(), model.getSubcode());


                        }

                    }
                });


                holder.tvsubname.setText(model.getSubname());
                holder.tvsubpm.setText("Pass marks-" + model.getSubpm());
                holder.tvsubfm.setText("Full marks-" + model.getSubfm());
                holder.tvsubname.setText(model.getSubname());
                holder.tvsubcode.setText("Subject code-" + model.getSubcode());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (new File(folder.getAbsoluteFile(), model.getSubcode()).exists()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", model.getSubcode());
                            navController.navigate(R.id.pdfViewer, bundle);
                        } else {
                            holder.loadpdf.setVisibility(View.VISIBLE);
                            downloadNotes(model.getSubpdf(), folder.getAbsolutePath(), model.getSubcode());
                        }

                    }
                });
            }

            @NonNull
            @Override
            public SyllabusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.syllabusitemlayout, parent, false);
                return new SyllabusViewHolder(view);
            }

        };
        syllabusrecyclerview.setAdapter(syllabusadapter);
        syllabusadapter.startListening();
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
                        Fragment fragment = new SubjectList();
                        if (SubjectList.this.isVisible()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("file", fileName);
                            vholder.loadpdf.setVisibility(View.GONE);
                            vholder.imgdown.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                            navController.navigate(R.id.action_subjectList_to_pdfViewer, bundle);
                        } //Toast.makeText(getContext(), "Download complete", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onError(Error error) {

                    }

                });
    }
}
