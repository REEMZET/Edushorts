package com.reemzet.adminedushorts.UserFargments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.adminedushorts.Model.UserModels;
import com.reemzet.adminedushorts.R;


public class SplashScreen extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference userref;
    FirebaseDatabase database;
    NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_screen, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userref = database.getReference("MyCollege/Users");
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    checkexiting();
                } else {
                    navController.popBackStack();
                    NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.signup);
                }
            }
        }, 3500);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    checkexiting();
                }
            }
        }, 3500);
    }

    private void checkexiting() {

        userref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserModels userModel = snapshot.getValue(UserModels.class);
                    if (userModel.getAccounttype().equals("Admin")) {
                     /*   navController.popBackStack();
                        NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.adminHome);*/
                    } else {
                        navController.popBackStack();
                        NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.menuFrag);
                    }
                } else {
                    navController.popBackStack();
                    NavHostFragment.findNavController(SplashScreen.this).navigate(R.id.action_splashScreen_to_userRegistration);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}