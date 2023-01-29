package com.reemzet.adminedushorts.UserFargments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.reemzet.adminedushorts.R;


import java.util.concurrent.TimeUnit;

public class Signup extends Fragment {
    Button btnsubmit;
    EditText etphone;
    String mobileno;
    FirebaseAuth mAuth;
    NavController navController;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        btnsubmit = view.findViewById(R.id.btnsubmit);
        etphone = view.findViewById(R.id.etphone);
        mAuth = FirebaseAuth.getInstance();

        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etphone.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "please enter phone no", Toast.LENGTH_SHORT).show();
                } else if (etphone.getText().toString().length() < 10) {
                    Toast.makeText(getActivity(), "Incorrect phone no", Toast.LENGTH_SHORT).show();
                } else {
                    mobileno = etphone.getText().toString();
                    otpsent(mobileno);

                    showloding();

                }
            }
        });

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Signup");


        return view;
    }

    public void otpsent(String phone) {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getActivity(), "failed" + e.getMessage(), Toast.LENGTH_LONG).show();

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Bundle bundle = new Bundle();
                bundle.putString("phoneno", phone);
                bundle.putString("verificationid", verificationId);
                progressDialog.dismiss();
                navController.popBackStack();
                navController.navigate(R.id.otpVerification, bundle);


            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void showloding() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialoprogress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
    }

}