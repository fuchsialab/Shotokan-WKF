package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class RULL extends AppCompatActivity {

    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private String bannerid;
    private InterstitialAd mInterstitialAd;
    private String interstitialId;

    ImageButton img,video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rull);

        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

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

        img=  findViewById(R.id.wkf2);
        video = findViewById(R.id.wkf1);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null) {

                    mInterstitialAd.show(RULL.this);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {

                            Intent intent=new Intent(RULL.this,wkfShotokan.class);
                            intent.putExtra("katapic","wkf");
                            startActivity(intent);

                            AdRequest adRequest = new AdRequest.Builder().build();

                            InterstitialAd.load(RULL.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                                    mInterstitialAd = interstitialAd;

                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                    mInterstitialAd = null;
                                    Intent intent=new Intent(RULL.this,wkfShotokan.class);
                                    intent.putExtra("katapic","wkf");
                                    startActivity(intent);

                                }
                            });

                        }

                    });

                }
                else {

                    Intent intent=new Intent(RULL.this,wkfShotokan.class);
                    intent.putExtra("katapic","wkf");
                    startActivity(intent);

                }



            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mInterstitialAd != null) {

                    mInterstitialAd.show(RULL.this);

                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")));

                            AdRequest adRequest = new AdRequest.Builder().build();

                            InterstitialAd.load(RULL.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
                                @Override
                                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                                    mInterstitialAd = interstitialAd;

                                }

                                @Override
                                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                                    mInterstitialAd = null;
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")));

                                }
                            });

                        }

                    });

                }
                else {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")));

                }


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
                mAdView=new AdView(RULL.this);
                ((RelativeLayout)view).addView(mAdView);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerid);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

                InterstitialAd.load(RULL.this,interstitialId, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        mInterstitialAd = null;

                    }
                });


                //MediationTestSuite.launch(RULL.this);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
