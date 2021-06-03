package com.fuchsia.shotokanwkf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class historyShotokan extends AppCompatActivity {

    Button you;

    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private String bannerid;
    private String appid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_shotokan);

        you= findViewById(R.id.youtube_button);

        bannerAds();
        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=KdOAdnfoepU")));

            }
        });


    }

    public void bannerAds(){
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference().child("AdUnits");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {




            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("banner").getValue()).toString());
                appid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("appid").getValue()).toString());
                MobileAds.initialize(historyShotokan.this,appid);

                View view= findViewById(R.id.bannerad);
                mAdView=new AdView(historyShotokan.this);
                ((RelativeLayout)view).addView(mAdView);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerid);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
