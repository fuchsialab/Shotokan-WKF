package com.fuchsia.shotokanwkf;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
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


public class wkfShotokan extends AppCompatActivity {

    private AdView mAdView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    PhotoView photoView;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wkf_shotokan);

        photoView = findViewById(R.id.photo_view);

        bannerAds();
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        bundle= getIntent().getExtras();
        if(bundle !=null){
            String name= bundle.getString("katapic");
            assert name != null;
            setUp(name);

        }

    }

    private void setUp( String name) {

        switch (name) {
            case "belt": {

                photoView.setImageResource(R.drawable.belt);

                break;
            }
            case "fall": {

                photoView.setImageResource(R.drawable.falling);

                break;
            }
            case "kick": {

                photoView.setImageResource(R.drawable.geri);

                break;
            }
            case "uke": {

                photoView.setImageResource(R.drawable.uke);

                break;
            }
            case "dachi": {

                photoView.setImageResource(R.drawable.dachi);
//                photoView.setImageBitmap(imageLoder.
//                        decodeSampledBitmapFromResource(getResources(), R.drawable.dachi, 500, 500));

                break;
            }
            case "greeting": {

                photoView.setImageResource(R.drawable.somman);

                break;
            }
            case "wapon": {

                photoView.setImageResource(R.drawable.wapon);

                break;
            }
            case "wkf": {

                photoView.setImageResource(R.drawable.wkfr);

                break;
            }
            case "uchi": {

                photoView.setImageResource(R.drawable.uchi);

                break;
            }


        }


    }

    public void bannerAds(){

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        View view= findViewById(R.id.bannerad);
        mAdView=new AdView(this);
        ((RelativeLayout)view).addView(mAdView);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(getResources().getString(R.string.bannerid));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //MediationTestSuite.launch(basicKarate.this);



    }

}
