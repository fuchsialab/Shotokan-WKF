package com.fuchsia.shotokanwkf;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class kumiteActivity extends AppCompatActivity {


    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private String bannerid;
    private String interstitialId;
    private static InterstitialAd mInterstitialAd;
    private String appid;

    private static final String COMMON_TAG = "OrintationChange";

    RecyclerView recview;
    kumiteAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kumite);

        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        mInterstitialAd=new InterstitialAd(kumiteActivity.this);

        progressBar = findViewById(R.id.progressbar);

        checkConnection();

        recview = (RecyclerView) findViewById(R.id.recyclekum);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<kumitemodel> options =
                new FirebaseRecyclerOptions.Builder<kumitemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Kumite"), kumitemodel.class)
                        .build();

        adapter = new kumiteAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }

    private String checkConnection() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

            }
        } else {
            Toast.makeText(getApplicationContext(),"No Internet Connection!", Toast.LENGTH_LONG).show();

        }
        return null;
    }

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

    public void bannerAds(){
        DatabaseReference rootref= FirebaseDatabase.getInstance().getReference().child("AdUnits");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("banner").getValue()).toString());
                appid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("appid").getValue()).toString());
                MobileAds.initialize(kumiteActivity.this,appid);
                View view= findViewById(R.id.bannerad);
                mAdView=new AdView(kumiteActivity.this);
                ((RelativeLayout)view).addView(mAdView);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerid);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

                interstitialId=String.valueOf(Objects.requireNonNull(dataSnapshot.child("Interstitial").getValue()).toString());
                mInterstitialAd.setAdUnitId(interstitialId);
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }


        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());

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