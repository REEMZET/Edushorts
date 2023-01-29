package com.reemzet.adminedushorts.Sbte.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reemzet.adminedushorts.R;
import com.reemzet.adminedushorts.Sbte.Model.ItemModels;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AddItems extends Fragment {

    ArrayList<String> tag;
    FirebaseFirestore db;
    String keyword = "";
    ItemModels itemDetailswithtag;
    ItemModels itemDetails;
    String itemname, itemfolder, itemdesc, itemurl, itemlength, itemprice, itemtype, itemid, itemthumnail;
    DatabaseReference itemref;
    FirebaseDatabase database;
    Button submitbtn;
    EditText etitemname, etitemdesc, etitemprice, etitemlength, etitemurl, etthumbnail;
    RadioButton rdVideo, rdpdf;
    AutoCompleteTextView autocatfolder;
    boolean fieldboolean;
    RadioGroup rdgroup;
    Toolbar toolbar;
    NavController navController;
    ImageView imgokbtn, itembookimg, thumbnailbtn;
    String gdrivecode = "https://drive.google.com/uc?export=download&id=";
    String exculdecode1 = "https://drive.google.com/file/d/";
    String exculdecode2 = "/view?usp=sharing";
    String exculdecode3 = "/view?usp=drivesdk";
    ArrayList<String> folderlist;
    ProgressDialog progressDialog;
    String incomingref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_items, container, false);
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        submitbtn = view.findViewById(R.id.submititem);
        etitemname = view.findViewById(R.id.etitemname);
        etitemdesc = view.findViewById(R.id.etitemdesc);
        etitemprice = view.findViewById(R.id.etitemprice);
        etitemlength = view.findViewById(R.id.etitemlength);
        etitemurl = view.findViewById(R.id.etitemurl);
        rdVideo = view.findViewById(R.id.rdvideo);
        rdpdf = view.findViewById(R.id.rdpdf);
        autocatfolder = view.findViewById(R.id.autocat1folder);
        etthumbnail = view.findViewById(R.id.etthumbnail);
        thumbnailbtn = view.findViewById(R.id.thumbaniloktn);
        incomingref = getArguments().getString("dbref");

        rdgroup = view.findViewById(R.id.rdgroup);
        imgokbtn = view.findViewById(R.id.imgokbtn);
        itembookimg = view.findViewById(R.id.itembookimg);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        itemref = database.getReference(incomingref);

        itemDetailswithtag = new ItemModels();
        itemDetails = new ItemModels();

        tag = new ArrayList<>();
        folderlist = new ArrayList<>();

        Toast.makeText(getActivity(), incomingref, Toast.LENGTH_SHORT).show();

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view1 -> {
            navController.popBackStack();
        });

        thumbnailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etthumbnail.getText().toString().isEmpty()) {
                    String imgurl1 = etthumbnail.getText().toString().replace(exculdecode1, gdrivecode);
                    String imgurl2 = imgurl1.replace(exculdecode2, "");
                    String finalimgurl = imgurl2.replace(exculdecode3, "");
                    etthumbnail.setText(finalimgurl);
                    itemthumnail = finalimgurl;
                    Glide.with(getActivity())
                            .load(finalimgurl)
                            .centerCrop()
                            .placeholder(R.drawable.video)
                            .into(itembookimg);
                }
            }
        });


        imgokbtn.setOnClickListener(view12 -> {
            if (!etitemurl.getText().toString().isEmpty()) {
                String imgurl1 = etitemurl.getText().toString().replace(exculdecode1, gdrivecode);
                String imgurl2 = imgurl1.replace(exculdecode2, "");
                String finalimgurl = imgurl2.replace(exculdecode3, "");
                etitemurl.setText(finalimgurl);
                itemurl = finalimgurl;
            }
        });
        submitbtn.setOnClickListener(view13 -> {
            itemid = (itemref.push().getKey());
            if (checkandgetdatafromfield()) {
                showloding();
                taggenerator(itemname);
                setdatatomodel();
                uploadtorealtimedb();
                uplloadtofirestore();


            }
        });
        itemref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    folderlist.add(ds.getKey());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, folderlist);
                    autocatfolder.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private boolean checkandgetdatafromfield() {
        if (etitemname.getText().toString().isEmpty()) {
            etitemname.setError("Can't be empty");
            etitemname.requestFocus();
            fieldboolean = false;
        } else if (etitemdesc.getText().toString().isEmpty()) {
            etitemdesc.setError("Can't be empty");
            etitemdesc.requestFocus();
            fieldboolean = false;
        } else if (etthumbnail.getText().toString().isEmpty()) {
            etthumbnail.setError("cant'be empty");
            etthumbnail.requestFocus();
            fieldboolean = false;
        } else if (etitemlength.getText().toString().isEmpty()) {
            etitemlength.setError("Can't be empty");
            etitemlength.requestFocus();
            fieldboolean = false;
        } else if (etitemprice.getText().toString().isEmpty()) {
            etitemprice.setError("Can't be empty");
            etitemprice.requestFocus();
            fieldboolean = false;
        } else if (etitemurl.getText().toString().isEmpty()) {
            etitemurl.setError("Can't be empty");
            etitemurl.requestFocus();
            fieldboolean = false;
        } else if (autocatfolder.getText().toString().isEmpty()) {
            autocatfolder.setError("Can't be empty");
            autocatfolder.requestFocus();
            fieldboolean = false;
        } else {
            itemname = etitemname.getText().toString();
            itemdesc = etitemdesc.getText().toString();
            itemthumnail = etthumbnail.getText().toString();
            itemlength = etitemlength.getText().toString();
            itemprice = etitemprice.getText().toString();
            itemfolder = autocatfolder.getText().toString();
            if (rdgroup.getCheckedRadioButtonId() == R.id.rdvideo) {
                itemtype = "Video";
            } else itemtype = "Pdf";
            fieldboolean = true;
        }
        return fieldboolean;
    }

    private void uplloadtofirestore() {
        db.collection("Items")
                .document(itemid)
                .set(itemDetailswithtag)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        etitemname.getText().clear();
                        etitemdesc.getText().clear();
                        etitemprice.getText().clear();
                        etitemlength.getText().clear();
                        etitemurl.getText().clear();
                    }
                });
    }

    private void uploadtorealtimedb() {
        if (itemfolder.equals("null")) {
            itemref.child(itemid).setValue(itemDetails);
        } else {
            itemref.child(itemfolder).child(itemid).setValue(itemDetails);
        }
    }

    private void setdatatomodel() {
        Date d = new Date();
        CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
        itemDetails = new ItemModels(itemname, itemurl, itemdesc, itemlength, itemprice, itemid, itemtype, (String) s, "0", itemthumnail);
        itemDetailswithtag = new ItemModels(itemname, itemurl, itemdesc, itemlength, itemprice, itemid, itemtype, (String) s, "0", itemthumnail, tag);


    }

    private void taggenerator(String str) {

        String[] word = str.split(" ");

        for (int n = 0; n < word.length; n++) {
            for (int i = 0; i < word[n].length(); i++) {
                for (int j = 0; j <= i; j++) {
                    keyword = keyword + (word[n].toLowerCase(Locale.ROOT).charAt(j));
                }
                tag.add(keyword);
                keyword = "";
            }
        }

    }

    public void showloding() {
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.show();
            progressDialog.setContentView(R.layout.dialoprogress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }
}