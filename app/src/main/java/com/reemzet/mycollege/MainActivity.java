package com.reemzet.mycollege;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reemzet.mycollege.Model.UserModels;




public class MainActivity extends AppCompatActivity {
   DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    NavController navController;
    FirebaseDatabase database;
    DatabaseReference useref,inappUpdateref,manualupdateref;
    FirebaseAuth mAuth;
    String deviceid;
    LinearLayout profile,share,logout,checkupdate,aboutdev,aboutorg,chat,booknav,walletnav,chatnav,ll_mypurchase,ll_share,ll_developer,ll_aboutorg;
    TextView loginusername, loginuserphonno;
    ImageView loginuserpic;
    UserModels userModels;
    ImageView coinimage;
    TextView tvcoincount;
    public static int UPDATE_CODE =22;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar=findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        database=FirebaseDatabase.getInstance();
        useref = database.getReference("MyCollege/Users");
        inappUpdateref=database.getReference("MyCollege/InappUpadate");
        manualupdateref=database.getReference("MyCollege/ManualUpdate");

        deviceid= Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;

        navController = navHostFragment.getNavController();

        View headerView = navigationView.getHeaderView(0);
        loginusername = headerView.findViewById(R.id.loginusername);
        loginuserphonno = headerView.findViewById(R.id.loginuserphone);
        loginuserpic = headerView.findViewById(R.id.loginuserpic);
        profile=headerView.findViewById(R.id.ll_Profile);
        share=headerView.findViewById(R.id.ll_Share);
        logout=headerView.findViewById(R.id.ll_Logout);
        checkupdate=headerView.findViewById(R.id.ll_update);
        aboutdev=headerView.findViewById(R.id.ll_developer);
        aboutorg=headerView.findViewById(R.id.ll_organisation);
        chat=headerView.findViewById(R.id.ll_chat);
        coinimage=findViewById(R.id.coinimage);
        tvcoincount=findViewById(R.id.tvcoincount);
        booknav=headerView.findViewById(R.id.booknav);
        walletnav=headerView.findViewById(R.id.walletnav);
        chatnav=headerView.findViewById(R.id.chatnav);
        ll_mypurchase=headerView.findViewById(R.id.ll_mypurchase);
        ll_aboutorg=headerView.findViewById(R.id.ll_organisation);
        ll_developer=headerView.findViewById(R.id.ll_developer);

        checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUpdateChecker appUpdateChecker = new AppUpdateChecker(MainActivity.this);
                appUpdateChecker.checkForUpdate(true);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.user_Profile);
            }
        });
        booknav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("menu","library");
                navController.navigate(R.id.chooseStream,bundle);
            }
        });
        walletnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.wallet);
            }
        });
        ll_mypurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.myPurchase);
            }
        });
        ll_aboutorg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("url","https://sites.google.com/view/edushorts/home");
            navController.navigate(R.id.webViewPage,bundle);
            }
        });
        ll_developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.about_Devops);
            }
        });
        chatnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.groupChat);
            }
        });


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
                case R.id.player:
                        toolbar.setVisibility(View.GONE);
                    getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this ,R.color.black));
                   // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                default:
                    getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this , R.color.appcolor));
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    tvcoincount.setVisibility(View.GONE);
                    coinimage.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
            if (navDestination.getId()== R.id.home2){
                tvcoincount.setVisibility(View.VISIBLE);
                coinimage.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
         checkloginstatus();
        Setuserdata();
        navheaderclick();

        inappUpdateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Boolean.TRUE.equals(snapshot.getValue(Boolean.class))){
                    inAppUpdate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        manualupdateref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (Boolean.TRUE.equals(snapshot.getValue(Boolean.class))){
                      AppUpdateChecker appUpdateChecker = new AppUpdateChecker(MainActivity.this);
                      appUpdateChecker.checkForUpdate(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inAppUpdate() {
        appUpdateManager= AppUpdateManagerFactory.create(this);
       Task<AppUpdateInfo> task=appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,MainActivity.this,UPDATE_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        appUpdateManager.registerListener(listener);
    }
    InstallStateUpdatedListener listener=installState -> {
        if (installState.installStatus()== InstallStatus.DOWNLOADED){
            popup();
        }
    };

    private void popup() {
        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"new app ready",Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Reload", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        //snackbar.setTextColor(Color.parseColor("#FF000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==UPDATE_CODE){
            if (resultCode!=RESULT_OK){
                System.exit(0);
            }
        }
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

      public void Setuserdata(){
                 mAuth = FirebaseAuth.getInstance();
         if (mAuth.getCurrentUser() != null) {
             useref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     userModels=snapshot.getValue(UserModels.class);
                     loginusername.setText(userModels.getUsername());
                     loginuserphonno.setText(userModels.getUserphone());
                     Glide.with(getApplicationContext())
                             .load(userModels.getUserpic())
                             .placeholder(R.drawable.student)
                             .into(loginuserpic);
                     tvcoincount.setText(userModels.getAmount());
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });
         }
         }
         public void navheaderclick(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                navController.popBackStack(R.id.signup, true);
                navController.navigate(R.id.signup);
                Toast.makeText(MainActivity.this, "signout", Toast.LENGTH_SHORT).show();
            }
        });
        coinimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.wallet);
            }
        });
         }

}
