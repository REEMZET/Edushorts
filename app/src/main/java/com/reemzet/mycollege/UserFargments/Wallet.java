package com.reemzet.mycollege.UserFargments;

import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;



public class Wallet extends Fragment {

    Toolbar toolbar;
    NavController navController;
    FirebaseDatabase database;
    DatabaseReference admintransref, useref;
    FirebaseAuth mAuth;
    TextView tvamount, tvreferal, tvapply, tvhistroy, tvsharebtn;
    EditText etreferalcode;
    UserModels userModels;
    ProgressDialog progressDialog;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    ImageView imageshareview,imgvdeoads;
    TranscationModel transcationModel;
    CharSequence s;
    String currentTime;
    TextView tvrecharge;
    private DefaultTrackSelector trackSelector;
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        useref = database.getReference("MyCollege/Users");
        admintransref=database.getReference("MyCollege/Admin/Transaction");
        tvamount = view.findViewById(R.id.tvamount);
        tvreferal = view.findViewById(R.id.tvreferalcode);
        etreferalcode = view.findViewById(R.id.etreferalcode);
        tvapply = view.findViewById(R.id.tvapply);
        tvhistroy = view.findViewById(R.id.tvhistroy);
        imageshareview = view.findViewById(R.id.shareimageview);
        tvsharebtn = view.findViewById(R.id.sharebtnbtn);
        imgvdeoads=view.findViewById(R.id.imgvideoplay);
        tvrecharge=view.findViewById(R.id.tvrecharge);


        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Wallet");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
        getuserdata();

        tvapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etreferalcode.getText().toString().equals(userModels.getReferalcode())) {
                    Toast.makeText(getActivity(), "can't apply own code", Toast.LENGTH_SHORT).show();
                } else if (etreferalcode.getText().toString().length() < 6) {
                    Toast.makeText(getActivity(), "invalid code", Toast.LENGTH_SHORT).show();
                } else {
                    showloding();
                    appliedcode(etreferalcode.getText().toString());
                }
            }
        });
        tvreferal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(userModels.getReferalcode());
                Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
            }
        });
        tvhistroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("amount", userModels.getAmount());
                navController.navigate(R.id.transcationFragment, bundle);
            }
        });
        tvsharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invite();
            }
        });
        imgvdeoads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Coming soon please wait for next update", BaseTransientBottomBar.LENGTH_SHORT).show();

            }
        });
        Date d = new Date();
        s = DateFormat.format("MMM d, yyyy ", d.getTime());
        currentTime =new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());



        tvrecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            DialogPlus dialog = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(R.layout.paymentvideolayout))
                        .setGravity(Gravity.CENTER)
                    .setCancelable(false)
                        .create();
                dialog.show();
                View myview = dialog.getHolderView();
                ImageView closebtn=myview.findViewById(R.id.closebtn);
                playerView=myview.findViewById(R.id.exoPlayerView);
                trackSelector = new DefaultTrackSelector(getActivity());
                simpleExoPlayer = new SimpleExoPlayer.Builder(getActivity()).setTrackSelector(trackSelector).build();
                playerView.setPlayer(simpleExoPlayer);
                playerView.setVisibility(View.VISIBLE);
                playerView.setUseController(false);
                MediaItem mediaItem = MediaItem.fromUri("https://firebasestorage.googleapis.com/v0/b/college-b02f3.appspot.com/o/Pay.mp4?alt=media&token=791c712f-2453-4998-a511-4fe71a2bea14");
                simpleExoPlayer.addMediaItem(mediaItem);
                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        releasePlayer();
                        dialog.dismiss();
                    }
                });
            }
        });



        return view;
    }

    private void appliedcode(String rfcode) {
        useref.orderByChild("referalcode").startAt(rfcode).endAt(rfcode + "\uf8ff")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            useref.child(mAuth.getUid()).child("amount").setValue(String.valueOf(Integer.parseInt(userModels.getAmount()) + 10))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                              @Override
                                                              public void onSuccess(Void unused) {
                                                                  useref.child(mAuth.getUid()).child("appliedreferal").setValue(rfcode);
                                                                  referalupdate(String.valueOf(snapshot.child("useruid").getValue(String.class)));
                                                                  setTranscation(mAuth.getUid());
                                                                  Toast.makeText(getActivity(), "Code Applied successfully", Toast.LENGTH_SHORT).show();
                                                              }
                                                          }
                                    ).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), "Referal not found", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Referal not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Referal not found", Toast.LENGTH_SHORT).show();
                    }
                });
        useref.orderByChild("referalcode").startAt(rfcode).endAt(rfcode + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Referal code not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTranscation(String uid) {
        String tranid = useref.child(uid).child("Transcation").push().getKey();
        transcationModel = new TranscationModel("Added to wallet by referal", "credit", "10", tranid, (String) s, currentTime, uid);
        useref.child(uid).child("Transcation").child(tranid).setValue(transcationModel);
        admintransref.child(tranid).setValue(transcationModel);

    }

    public void referalupdate(String referuid) {
        useref.child(referuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModels referuserModel = snapshot.getValue(UserModels.class);
                if (Integer.parseInt(referuserModel.getReferalcount()) < 4) {
                    useref.child(referuid).child("amount").setValue(String.valueOf(Integer.parseInt(referuserModel.getAmount()) + 10));
                    useref.child(referuid).child("referalcount").setValue(String.valueOf(Integer.parseInt(referuserModel.getReferalcount()) + 1));
                        setTranscation(referuid);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getuserdata() {
        useref.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModels = snapshot.getValue(UserModels.class);
                tvreferal.setText("Referal Code-" + userModels.getReferalcode());
                tvamount.setText(userModels.getAmount());
                if (!userModels.getAppliedreferal().equals("null")) {
                    etreferalcode.setEnabled(false);
                    etreferalcode.setText(userModels.getAppliedreferal());
                    tvapply.setEnabled(false);
                    tvapply.setBackgroundColor(Color.LTGRAY);
                }
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

    public void invite() {
        imageshareview.setImageDrawable(getResources().getDrawable(R.drawable.shareref));
        bitmapDrawable = (BitmapDrawable) imageshareview.getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        Uri bmpuri;

        int hand = 0x1F449;
        String text = "Download *Edushorts* App for your Study and get some free credit by using my referal code " + getEmojiByUnicode(hand) + " *" +
                userModels.getReferalcode() + "*" + " this app is useful for AKU/SBTE/BSEB/CBSE board's Students"
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

    protected void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
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