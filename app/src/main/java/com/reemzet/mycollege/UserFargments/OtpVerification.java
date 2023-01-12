package com.reemzet.mycollege.UserFargments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.R;


public class OtpVerification extends Fragment {
    ProgressDialog progressDialog;
    String phoneno, otpid, otp;
    FirebaseAuth mAuth;
    TextView phonetv, tvresend;
    EditText otpet;
    Button otpsubmitbtn;
    NavController navController;
    FirebaseDatabase database;
    DatabaseReference useref;
    String deviceid;
    Toolbar toolbar;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        phoneno = getArguments().getString("phoneno");
        otpid = getArguments().getString("verificationid");
        phonetv = view.findViewById(R.id.phonetv);
        tvresend = view.findViewById(R.id.tvresend);
        otpet = view.findViewById(R.id.etotp);
        otpsubmitbtn = view.findViewById(R.id.otpsubmitbtn);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        useref = database.getReference("MyCollege/Users");
        deviceid = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        phonetv.setText("Verify:+91" + phoneno);
        otpet.requestFocus();

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        otpsubmitbtn.setOnClickListener(v -> {
            if (otpet.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "invalid otp", Toast.LENGTH_SHORT).show();


            } else if (otpet.getText().toString().length() < 6) {
                Toast.makeText(getActivity(), "invalid otp", Toast.LENGTH_SHORT).show();

            } else {
                showloding();
                otp = otpet.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
        tvresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
                navController.navigate(R.id.signup);
            }
        });


        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Otp Verification");


        return view;
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        checkregistration();
                    } else {
                        // Sign in failed, display a message and update the UI
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "failed to verify", Toast.LENGTH_SHORT).show();
                        tvresend.setVisibility(View.VISIBLE);

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

    public void checkregistration() {
        useref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    useref.child(mAuth.getUid()).child("userdeviceid").setValue(deviceid).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            navController.popBackStack();
                            navController.navigate(R.id.menuFrag);
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    navController.popBackStack();
                    navController.navigate(R.id.userreg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}