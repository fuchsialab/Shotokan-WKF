package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RULL extends AppCompatActivity {

    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private String bannerid;
    private InterstitialAd mInterstitialAd;
    private String interstitialId;
    private InterstitialAd nInterstitialAd;
    private String appid;

    ImageButton img,video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rull);

        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mInterstitialAd=new InterstitialAd(RULL.this);
        nInterstitialAd=new InterstitialAd(RULL.this);


        img= findViewById(R.id.wkf2);
        video = findViewById(R.id.wkf1);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mInterstitialAd.isLoaded())
                {
                    mInterstitialAd.show();
                }
                else
                {
                    Intent intent=new Intent(RULL.this,wkfShotokan.class);
                    intent.putExtra("katapic","wkf");
                    startActivity(intent);
                }



            }
        });
        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                Intent intent = new Intent(RULL.this, wkfShotokan.class);
                intent.putExtra("katapic", "wkf");
                startActivity(intent);

            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nInterstitialAd.isLoaded())
                {
                    nInterstitialAd.show();
                }
                else
                {
                    Intent intent =new Intent(RULL.this, videoPlayer.class);
                    intent.putExtra("nam", "c6r8JwEFowY");
                    startActivity(intent);
                }



            }
        });
        nInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdClosed() {
                Intent intent =new Intent(RULL.this, videoPlayer.class);
                intent.putExtra("nam", "c6r8JwEFowY");
                startActivity(intent);

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

                appid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("appid").getValue()).toString());
                MobileAds.initialize(RULL.this,appid);


                mInterstitialAd.setAdUnitId(interstitialId);
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());
                nInterstitialAd.setAdUnitId(interstitialId);
                nInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
