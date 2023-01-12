package com.reemzet.mycollege;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.Model.UserModels;
import com.reemzet.mycollege.UserFargments.Signup;

public class MainActivity extends AppCompatActivity {
   DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    FirebaseDatabase database;
    DatabaseReference useref;
    FirebaseAuth mAuth;
    String deviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar=findViewById(R.id.toolbar);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        database=FirebaseDatabase.getInstance();
        useref = database.getReference("MyCollege/Users");

         deviceid= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();

        View headerView = navigationView.getHeaderView(0);

        NavigationUI.setupWithNavController(navigationView, navController);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setItemIconTintList(null);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id= navDestination.getId();
            switch (id){
                case R.id.splashScreen:
                        toolbar.setVisibility(View.GONE);
                    break;
                default:
                    toolbar.setVisibility(View.VISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (navDestination.getId()== R.id.home2){

                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
         checkloginstatus();

    }
    public void checkloginstatus(){
        new CountDownTimer(5000, 1000) {

    public void onTick(long millisUntilFinished) {
    }
    public void onFinish() {
          mAuth = FirebaseAuth.getInstance();
         if (mAuth.getCurrentUser() != null) {
             useref.child(mAuth.getUid()).child("userdeviceid").addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     if (snapshot.exists()) {
                         String logid = snapshot.getValue(String.class);
                         if (!deviceid.equals(logid)) {
                             mAuth.signOut();
                             navController.popBackStack(R.id.signup, true);
                             navController.navigate(R.id.signup);
                             Toast.makeText(MainActivity.this, "signout", Toast.LENGTH_SHORT).show();
                         }
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
         }
    }
}.start();
         }
}