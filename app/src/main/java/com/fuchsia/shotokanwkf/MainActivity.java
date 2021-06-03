package com.fuchsia.shotokanwkf;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {


    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    CardView layout;

    private String bannerid;
    private InterstitialAd mInterstitialAd;
    private InterstitialAd nInterstitialAd;
    private static InterstitialAd sInterstitialAd;
    private InterstitialAd qInterstitialAd;
    private InterstitialAd rInterstitialAd;
    private String appid;

    ProgressDialog progressDialog;
    Timer timer;

    private static final String COMMON_TAG = "OrintationChange";

    String interstitialId;

    Button history,wkf, basic, kata, kumite,nunc;

    private long backPressTime;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;



    @Override
    public void onBackPressed() {


        if (backPressTime+2000>System.currentTimeMillis()){

            super.onBackPressed();
            return;

        }else {
            Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }

        backPressTime= System.currentTimeMillis();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout= findViewById(R.id.linncu);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,KataBunkai.class));
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout= findViewById(R.id.container);
        navigationView= findViewById(R.id.navdrawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuHome:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;

                    case R.id.menuprivacy:
                        Intent browse =new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.lkmkm)));
                        startActivity(browse);
                        drawerLayout.closeDrawer(GravityCompat.START);


                        return true;


                    case R.id.menurate:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }

                        return true;

                    case R.id.menuwhatsapp:

                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.fuchsia.saver")));


                        return true;

                    case R.id.menumoreapp:
                        Intent browses =new Intent(Intent.ACTION_VIEW, Uri.parse(("https://play.google.com/store/apps/collection/cluster?clp=igM4ChkKEzUzNjIwODY3OTExNjgyNTA2MTkQCBgDEhkKEzUzNjIwODY3OTExNjgyNTA2MTkQCBgDGAA%3D:S:ANO1ljJMw2s&gsr=CjuKAzgKGQoTNTM2MjA4Njc5MTE2ODI1MDYxORAIGAMSGQoTNTM2MjA4Njc5MTE2ODI1MDYxORAIGAMYAA%3D%3D:S:ANO1ljI3U6g")));
                        startActivity(browses);
                        drawerLayout.closeDrawer(GravityCompat.START);

                        return true;

                    case R.id.menushare:

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "Download Shotokan Karate App and Learn.  https://play.google.com/store/apps/details?id=com.fuchsia.shotokanwkf";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shotokan WKF");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        drawerLayout.closeDrawer(GravityCompat.START);

                        return true;

                    case R.id.menuexit:

                        finish();
                        System.exit(0);

                        return true;
                }
                return false;


            }
        });



        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(5)
                .setRemindInterval(10)
                .setShowLaterButton(true)
                .setDebug(false)
                .setOnClickButtonListener(new OnClickButtonListener() {
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);


        UpdateHelper.with(this)
                    .onUpdateCheck(this)
                    .check();


            timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    progressDialog.dismiss();

                }
            },2000);

            progressDialog =new ProgressDialog(MainActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);


        mInterstitialAd=new InterstitialAd(MainActivity.this);
        mInterstitialAd=new InterstitialAd(MainActivity.this);
        nInterstitialAd=new InterstitialAd(MainActivity.this);
        qInterstitialAd=new InterstitialAd(MainActivity.this);
        rInterstitialAd=new InterstitialAd(MainActivity.this);
        sInterstitialAd=new InterstitialAd(MainActivity.this);
        

        history = findViewById(R.id.btnhistory);
        wkf = findViewById(R.id.btnwkfrull);
        basic = findViewById(R.id.btnbasic);
        kata = findViewById(R.id.btnkata);
        kumite = findViewById(R.id.btnkumite);
        nunc = findViewById(R.id.btnnun);

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this,historyShotokan.class));
                    }

                }

            });
            mInterstitialAd.setAdListener(new AdListener(){

                @Override
                public void onAdClosed() {
                    startActivity(new Intent(MainActivity.this, historyShotokan.class));

                }
            });
            wkf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(nInterstitialAd.isLoaded())
                    {
                        nInterstitialAd.show();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this,RULL.class));
                    }


                }
            });
            nInterstitialAd.setAdListener(new AdListener(){

                @Override
                public void onAdClosed() {
                    startActivity(new Intent(MainActivity.this, RULL.class));
                    nInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                }
            });
            basic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        startActivity(new Intent(MainActivity.this,basicKarate.class));

                }
            });

            kata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        startActivity(new Intent(MainActivity.this,kataList.class));
                    }
            });

            kumite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(qInterstitialAd.isLoaded())
                    {
                        qInterstitialAd.show();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this,kumiteActivity.class));
                    }

                }
            });
            qInterstitialAd.setAdListener(new AdListener(){

                @Override
                public void onAdClosed() {
                    startActivity(new Intent(MainActivity.this, kumiteActivity.class));
                    qInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                }
            });


            nunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(rInterstitialAd.isLoaded())
                {
                    rInterstitialAd.show();
                }
                else
                {
                    Intent intent =new Intent(MainActivity.this, videoPlayer.class);
                    intent.putExtra("nam", "fAEHQzfEN0g");
                    startActivity(intent);
                }
            }
            });
            rInterstitialAd.setAdListener(new AdListener(){

                @Override
                public void onAdClosed() {
                    Intent intent =new Intent(MainActivity.this, videoPlayer.class);
                    intent.putExtra("nam", "fAEHQzfEN0g");
                    startActivity(intent);
                }
            });


    }

    @Override
    public void onUpdateCheckListener(String urlApp) {

        AlertDialog alertDialog=new AlertDialog.Builder(this)
                .setTitle("New Version Available")
                .setMessage(" Please update for better experience")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.jjjj))));

                    }
                }).setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    public static void showInterstitial() {
        if (sInterstitialAd.isLoaded()) {
            sInterstitialAd.show();
        }


        sInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                sInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

            }
        });

    }



    public void bannerAds(){
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference().child("AdUnits");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {




            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("banner").getValue()).toString());
                interstitialId=String.valueOf(Objects.requireNonNull(dataSnapshot.child("Interstitial").getValue()).toString());
                View view= findViewById(R.id.bannerad);
                mAdView=new AdView(MainActivity.this);
                ((RelativeLayout)view).addView(mAdView);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerid);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                appid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("appid").getValue()).toString());
                MobileAds.initialize(MainActivity.this,appid);


                mInterstitialAd.setAdUnitId(interstitialId);
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                nInterstitialAd.setAdUnitId(interstitialId);
                nInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                qInterstitialAd.setAdUnitId(interstitialId);
                qInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                rInterstitialAd.setAdUnitId(interstitialId);
                rInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                sInterstitialAd.setAdUnitId(interstitialId);
                sInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation ==Configuration.ORIENTATION_LANDSCAPE){
            Log.i(COMMON_TAG, "landscape");
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            Log.i(COMMON_TAG, "portrait");
        }
    }

 }
