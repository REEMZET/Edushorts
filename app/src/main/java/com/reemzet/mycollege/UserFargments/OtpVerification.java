package com.reemzet.mycollege.UserFargments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.reemzet.mycollege.R;


public class OtpVerification extends Fragment {
ProgressDialog progressDialog;
        String phoneno,otpid,otp;
        FirebaseAuth mAuth;
        TextView phonetv,tvresend;
        EditText otpet;
        Button otpsubmitbtn;
        NavController navController;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_otp_verification, container, false);
        phoneno=getArguments().getString("phoneno");
        otpid=getArguments().getString("verificationid");
        phonetv=view.findViewById(R.id.phonetv);
        tvresend=view.findViewById(R.id.tvresend);
        otpet=view.findViewById(R.id.etotp);
        otpsubmitbtn=view.findViewById(R.id.otpsubmitbtn);

        mAuth = FirebaseAuth.getInstance();


        phonetv.setText("Verify:+91"+phoneno);
        otpet.requestFocus();

        NavHostFragment navHostFragment =
                (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        otpsubmitbtn.setOnClickListener(v -> {
            if (otpet.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),"invalid otp",Toast.LENGTH_SHORT).show();


            }else if (otpet.getText().toString().length()<6){
                Toast.makeText(getActivity(),"invalid otp",Toast.LENGTH_SHORT).show();

            }else {
                showloding();
                otp=otpet.getText().toString();
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









        return view;
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(),"success",Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("phoneno",phoneno);
                        navController.popBackStack();
                       navController.navigate(R.id.action_otpverification_to_userreg,bundle);
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


}