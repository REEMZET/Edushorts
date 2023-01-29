package com.reemzet.mycollege.Sbte.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.reemzet.mycollege.Model.TranscationModel;
import com.reemzet.mycollege.Model.UserModels;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Models.ItemModels;
import com.reemzet.mycollege.Model.PurchaseModel;
import com.reemzet.mycollege.Sbte.ViewHolders.PdfListViewHolder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuestionlistFrag extends Fragment {
    RecyclerView recyclerView;
    String yearref;
    FirebaseDatabase database;
    DatabaseReference dbyearref, userref, admintransref;
    FirebaseRecyclerAdapter<ItemModels, PdfListViewHolder> adapter;
    ShimmerFrameLayout shimmerFrameLayout;
    PdfListViewHolder vholder;
    NavController navController;
    File folder;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    ArrayList<String> purchasearraylist;
    ProgressDialog progressDialog;
    UserModels userModels;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_questionlist, container, false);
        recyclerView = view.findViewById(R.id.questionlistrecycler);
        folder = getContext().getFilesDir();
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        yearref = getArguments().getString("year");
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userref = database.getReference("MyCollege/Users").child(mAuth.getUid());
        dbyearref = database.getReference("MyCollege/Sbte/QuesBank").child(yearref);
        admintransref = FirebaseDatabase.getInstance().getReference("MyCollege/Admin/Transaction");
        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        purchasearraylist = new ArrayList<>();
        getUserData();
        getPurchaseItem();


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("select subject");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });


        return view;
    }

    public void setRecyclerView() {
        FirebaseRecyclerOptions<ItemModels> options =
                new FirebaseRecyclerOptions.Builder<ItemModels>()
                        .setQuery(dbyearref, ItemModels.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<ItemModels, PdfListViewHolder>(options) {
            @NonNull
            @Override
            public PdfListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PdfListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.questionlistlayout, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PdfListViewHolder holder, int position, @NonNull ItemModels model) {
                vholder = holder;
                if (purchasearraylist.contains(model.getItemid())) {
                    holder.tvitemprice.setText("purchased");
                    holder.tvbuybtn.setVisibility(View.INVISIBLE);
                } else {
                    holder.tvitemprice.setText("\u20B9" + model.getItemprice());
                    holder.tvbuybtn.setVisibility(View.VISIBLE);
                }
                if (model.getItemprice().equals("0")) {
                    holder.tvitemprice.setText("free");
                    holder.tvbuybtn.setVisibility(View.INVISIBLE);
                }
                holder.pdfname.setText(model.getItemtitle());
                holder.tvitemdesc.setText(model.getItemdesc());

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (new File(folder.getAbsoluteFile(), "ques" + model.getItemtitle()).exists()) {
                    holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.delete));
                } else {
                    holder.imgdownload.setImageDrawable(getResources().getDrawable(R.drawable.down));
                }

                holder.imgdownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.tvitemprice.getText().equals("free") || holder.tvitemprice.getText().equals("purchased")) {
                            if (new File(folder.getAbsoluteFile(), "ques" + model.getItemtitle()).exists()) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Alert!")
                                        .setMessage("Do you want to Delete?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                File file = (new File(folder.getAbsoluteFile(), "ques" + model.getItemtitle()));
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
                                downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "ques" + model.getItemtitle());
                            }

                        } else {
                            Snackbar.make(v, "please purchase first", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }


                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.tvitemprice.getText().equals("free") || holder.tvitemprice.getText().equals("purchased")) {
                            if (new File(folder.getAbsoluteFile(), "ques" + model.getItemtitle()).exists()) {
                                Bundle bundle = new Bundle();
                                bundle.putString("file", "ques" + model.getItemtitle());
                                navController.navigate(R.id.pdfViewer, bundle);
                            } else {
                                holder.loadanim.setVisibility(View.VISIBLE);
                                downloadNotes(model.getItemurl(), folder.getAbsolutePath(), "ques" + model.getItemtitle());
                            }
                        } else {
                            Snackbar.make(view, "please purchase first", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }
                });
                holder.imgpreview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.loadanim.setVisibility(View.VISIBLE);
                        downloadPreview(model.getItemurl(), folder.getAbsolutePath(), "preview" + model.getItemtitle());
                    }
                });

                holder.tvbuybtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DialogPlus dialog = DialogPlus.newDialog(getContext())
                                        .setContentHolder(new ViewHolder(R.layout.purchaselayout))
                                        .setGravity(Gravity.CENTER)
                                        .setCancelable(true)
                                        .create();
                                dialog.show();
                                View myview = dialog.getHolderView();
                                TextView tvpurchasemsg=myview.findViewById(R.id.tvpurchasemsg);
                                TextView tvcancelpurchase=myview.findViewById(R.id.tvcancelpurchase);
                                TextView tvconfirmpurchase=myview.findViewById(R.id.tvconfirmpurchase);
                                tvpurchasemsg.setText("you will charges rs."+model.getItemprice()+" for this item?");
                                tvconfirmpurchase.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int remainbalance = Integer.parseInt(userModels.getAmount()) - Integer.parseInt(model.getItemprice());
                                        if (remainbalance < 0) {
                                            Toast.makeText(getContext(), "please recharge wallet", Toast.LENGTH_SHORT).show();
                                        } else {
                                            showloding();
                                            userref.child("amount").setValue(String.valueOf(remainbalance)).addOnSuccessListener(
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Date d = new Date();
                                                            CharSequence s = DateFormat.format("MMM d, yyyy ", d.getTime());
                                                            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                                            TranscationModel transcationModel = new TranscationModel("Purchase video", "Debit", model.getItemprice(), model.getItemid(), (String) s, currentTime, mAuth.getUid());
                                                            userref.child("Transcation").push().setValue(transcationModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    admintransref.push().setValue(transcationModel);
                                                                    PurchaseModel purchaseModel = new PurchaseModel(model.getItemid(), model.getItemtitle(), model.getItemurl(),model.getItemtype(),(String) s, "6 months", model.getItemprice());
                                                                    userref.child("MyPurchases").child(model.getItemid()).setValue(purchaseModel).addOnSuccessListener(
                                                                            new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    holder.tvbuybtn.setVisibility(View.INVISIBLE);
                                                                                    holder.tvitemprice.setText("purchased");
                                                                                    progressDialog.dismiss();
                                                                                    dialog.dismiss();
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
                                });
                                tvcancelpurchase.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });

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
    public void downloadPreview(String url, String dirPath, String fileName) {
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
                            navController.navigate(R.id.preViewPdfViewer, bundle);
                        }


                    }

                    @Override
                    public void onError(Error error) {

                    }


                });
    }
    private void getPurchaseItem() {
        userref.child("MyPurchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchasearraylist.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PurchaseModel model = snapshot1.getValue(PurchaseModel.class);
                    purchasearraylist.add(model.getProductid());
                }

                setRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getUserData() {
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModels = snapshot.getValue(UserModels.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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