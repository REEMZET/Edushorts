package com.reemzet.mycollege.Sbte.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.Model.UserModels;
import com.reemzet.mycollege.R;
import com.reemzet.mycollege.Sbte.Adapters.MessageAdapter;
import com.reemzet.mycollege.Sbte.Models.MessageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class GroupChat extends Fragment {
    FirebaseDatabase database;
    MessageAdapter messageAdapter;

    ArrayList<MessageModel> messageModelArrayList;
    EditText etmsg;
    ImageView msgsend;
    String msg, senderuid, timestamp;
    RecyclerView chatrecyclerview;
    List<String> offensiveword;
    SimpleDateFormat simpleDateFormat;
    NavController navController;
    String date;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    UserModels model;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_group_chat, container, false);

 senderuid = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        msgsend = view.findViewById(R.id.msgsend);
        etmsg = view.findViewById(R.id.etmsg);
        mAuth= FirebaseAuth.getInstance();
        chatrecyclerview = view.findViewById(R.id.chatrecyclerView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        offensiveword = Arrays.asList("madharchod", "behanchod", "randi", "randiwala", "boorkebaal", "bosri", "muth", "juji", "laudalasun", "laudalasan", "lauda lasoon", "lauda lasan", "chod", "Gay,", "Transsexual", "Kuttiya", "Bitch", "sex", "Paad", "FART", "HOOKER", "Saala kutta", "Bloody dog", "Saali kutti", "Bloody bitch",
                "MOTHERFUCKER", "Bhosadike", "fucker", "PUSSY", "Bhen chod", "Beti chod", "Bhadhava", "fuck", "Chutiya", "Gaand", "ASS", "Bakland", "Gaandu", "Asshole", "Gadha", "Lauda", "Lund", "Penis", "dick", "cock",
                "Hijra", "boor", "bsdk", "bc", "i love you", "143", "i miss you", "bur", "kiss", "like you", "abe", "Chut", "choot", "Kamina", "laude", "Bhonsri", "Chudai", "Jhat ke baal", "jhat", "bhosri", "gaad", "jhaat", "mut", "moot");

        etmsg.setLinksClickable(true);
        etmsg.setAutoLinkMask(Linkify.WEB_URLS);

//If the edit text contains previous text with potential links
        Linkify.addLinks(etmsg, Linkify.WEB_URLS);
        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                Linkify.addLinks(s, Linkify.WEB_URLS);

            }
        });
        toolbar=getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("Chat");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               navController.popBackStack();

            }
        });

        messageModelArrayList = new ArrayList<>();

//chat fetch

checkaccounttype();

//sendbtnonclick
        msgsend.setOnClickListener(v -> {
            if (etmsg.getText().toString().isEmpty()) {

            } else {
                checkoffensive();
                Calendar calander = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("hh:mm a");
                date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                timestamp = simpleDateFormat.format(calander.getTime());
                String msgId = database.getReference().push().getKey();
                MessageModel messageModel = new MessageModel(msg, model.getUsername(), msgId, model.getAccounttype(), model.getUseruid() ,timestamp, date);

                    database.getReference().child("MyCollege/Sbte/Chat").child(msgId).setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }});
                etmsg.setText("");

            }
        });


        messageAdapter = new MessageAdapter(getContext(), messageModelArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        chatrecyclerview.setHasFixedSize(true);
        chatrecyclerview.setAdapter(messageAdapter);
        chatrecyclerview.setLayoutManager(linearLayoutManager);

        return view;

    }

    public void checkoffensive() {
        msg = etmsg.getText().toString();

        for (String offensive : offensiveword) {
            Pattern rx = Pattern.compile("\\b" + offensive + "\\b", Pattern.CASE_INSENSITIVE);
            msg = rx.matcher(msg).replaceAll(new String(new char[offensive.length()]).replace('\0', '*'));
        }
    }
    public void checkaccounttype(){

       database.getReference("MyCollege/Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
             model =snapshot.getValue(UserModels.class);
               if (model.getAccounttype().equals("Admin")){
                   uid=getArguments().getString("uid");


               }else {
                   uid=mAuth.getUid();

               }
               loadchat();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    public void loadchat(){
        database.getReference().child("MyCollege/Sbte/Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    messageModelArrayList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        MessageModel messageModel = snapshot1.getValue(MessageModel.class);
                        messageModelArrayList.add(messageModel);
                    }
                    messageAdapter.notifyDataSetChanged();
                    chatrecyclerview.smoothScrollToPosition(messageAdapter.getItemCount());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();

    }
}
