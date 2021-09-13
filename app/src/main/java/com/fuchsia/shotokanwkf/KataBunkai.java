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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class KataBunkai extends AppCompatActivity {

    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private static InterstitialAd mInterstitialAd;
    private String appid;

    static KataBunkai instance;
    private String bannerid = "ca-app-pub-8700099206862921/2944855201";
    static String interstitialId = "ca-app-pub-8700099206862921/6692528520";

    private static final String COMMON_TAG = "OrintationChange";

    RecyclerView recview;
    KataBunkaiAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kata_bunkai);

        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        instance = this;

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("MyApp", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));
                }

                // Start loading ads here...
            }
        });

        progressBar = findViewById(R.id.progressbar);

        checkConnection();

        recview = (RecyclerView) findViewById(R.id.recyclek);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<KataBunkaimodel> options =
                new FirebaseRecyclerOptions.Builder<KataBunkaimodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Correction"), KataBunkaimodel.class)
                        .build();

        adapter = new KataBunkaiAdapter(options);
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


    public static KataBunkai getInstance() {
        return instance;
    }

    public void bannerAds(){

        View view= findViewById(R.id.bannerad);
        mAdView=new AdView(KataBunkai.this);
        ((RelativeLayout)view).addView(mAdView);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(bannerid);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //MediationTestSuite.launch(KataBunkai.this);

        InterstitialAd.load(KataBunkai.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                mInterstitialAd = interstitialAd;

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                mInterstitialAd = null;

            }
        });

    }



    public void showInterstitial() {


        if (mInterstitialAd != null) {

            mInterstitialAd.show(KataBunkai.this);

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {

                    AdRequest adRequest = new AdRequest.Builder().build();

                    InterstitialAd.load(KataBunkai.this, interstitialId, adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                            mInterstitialAd = interstitialAd;

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            mInterstitialAd = null;

                        }
                    });

                }

            });

        }

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