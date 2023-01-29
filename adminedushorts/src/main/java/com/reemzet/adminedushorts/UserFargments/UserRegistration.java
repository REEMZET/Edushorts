package com.reemzet.adminedushorts.UserFargments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.reemzet.adminedushorts.Model.UserModels;
import com.reemzet.adminedushorts.R;


import java.util.Date;


public class UserRegistration extends Fragment {
    AutoCompleteTextView autocity, autocollege, autobranch, autosem;
    TextView etname;
    String name, city, college, branch, sem;
    TextView sumbit;
    FirebaseDatabase database;
    DatabaseReference useref;
    FirebaseAuth mAuth;
    String phone;
    NavController navController;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);
        String[] city = getResources().getStringArray(R.array.array_bihar_districts);
        String[] colleges = getResources().getStringArray(R.array.colleges);
        String[] branch = getResources().getStringArray(R.array.branch);
        String[] sem = getResources().getStringArray(R.array.semester);

        autocity = view.findViewById(R.id.autocity);
        autobranch = view.findViewById(R.id.autobranch);
        autocollege = view.findViewById(R.id.autocollegs);
        autosem = view.findViewById(R.id.autosem);
        etname = view.findViewById(R.id.etname);
        sumbit = view.findViewById(R.id.submit);
        database = FirebaseDatabase.getInstance();
        useref = database.getReference("MyCollege/Users");
        mAuth = FirebaseAuth.getInstance();

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, city);
        ArrayAdapter<String> branchadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, branch);
        ArrayAdapter<String> collegeadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, colleges);
        ArrayAdapter<String> semadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, sem);
        autosem.setThreshold(1);
        autosem.setAdapter(semadapter);
        autocity.setThreshold(1);
        autocity.setAdapter(adapter);
        autocollege.setThreshold(1);
        autobranch.setThreshold(1);
        autobranch.setAdapter(branchadapter);
        autocollege.setAdapter(collegeadapter);

        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkinvalid();
            }
        });
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Signup");
        return view;
    }

    public void checkinvalid() {
        if (etname.getText().toString().isEmpty()) {
            etname.setError("can't be empty");
            etname.requestFocus();

        } else if (autocity.getText().toString().isEmpty()) {
            autocity.setError("can't be empty");
            autocity.requestFocus();
        } else if (autocollege.getText().toString().isEmpty()) {
            autocollege.setError("can't be empty");
            autocollege.requestFocus();

        } else if (autobranch.getText().toString().isEmpty()) {
            autobranch.setError("can't be empty");
            autobranch.requestFocus();
        } else if (autosem.getText().toString().isEmpty()) {
            autosem.setError("can't be empty");
            autosem.requestFocus();
        } else {
            showloding();
            name = etname.getText().toString();
            city = autocity.getText().toString();
            college = autocollege.getText().toString();
            branch = autobranch.getText().toString();
            sem = autosem.getText().toString();
            String deviceid = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Date d = new Date();
            CharSequence s = DateFormat.format("MMMM d, yyyy ", d.getTime());
            UserModels userModels = new UserModels(name, city, college, sem, branch, mAuth.getUid(), "pic.jpg", deviceid, "normal", mAuth.getCurrentUser().getPhoneNumber(), (String) s, mAuth.getUid().substring(0,2)+mAuth.getUid().substring(6,10),"null","0","0");
            useref.child(mAuth.getUid()).setValue(userModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    navController.popBackStack();
                    navController.navigate(R.id.menuFrag);
                }
            });
        }
    }

    public void showloding() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialoprogress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
    }

}