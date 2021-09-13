package com.fuchsia.shotokanwkf;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
    private String bannerid;
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
            case "a": {

                photoView.setImageResource(R.drawable.taiyoku_shodan);

                break;
            }
            case "b": {

                photoView.setImageResource(R.drawable.taiyokunidan);

                break;
            }
            case "c": {

                photoView.setImageResource(R.drawable.taiyokkusandan);

                break;
            }
            case "d": {

                photoView.setImageResource(R.drawable.hainshodan);

                break;
            }
            case "e": {

                photoView.setImageResource(R.drawable.haiannidan);

                break;
            }
            case "f": {

                photoView.setImageResource(R.drawable.haiansandan);

                break;
            }
            case "g": {

                photoView.setImageResource(R.drawable.haianyondan);

                break;
            }
            case "h": {

                photoView.setImageResource(R.drawable.haiangodan);

                break;
            }
            case "i": {

                photoView.setImageResource(R.drawable.tekkishodan);

                break;
            }
            case "j": {

                photoView.setImageResource(R.drawable.tekkinidan);

                break;
            }
            case "k": {

                photoView.setImageResource(R.drawable.tekkisandan);

                break;
            }
            case "l": {

                photoView.setImageResource(R.drawable.basaidai);

                break;
            }
            case "m": {

                photoView.setImageResource(R.drawable.bassaisho);

                break;
            }
            case "n": {

                photoView.setImageResource(R.drawable.kankudai);

                break;
            }
            case "o": {

                photoView.setImageResource(R.drawable.kankusho);

                break;
            }
            case "p": {

                photoView.setImageResource(R.drawable.empi);

                break;
            }
            case "q": {

                photoView.setImageResource(R.drawable.jion);

                break;
            }
            case "r": {

                photoView.setImageResource(R.drawable.gangaku);

                break;
            }
            case "s": {

                photoView.setImageResource(R.drawable.hangetsu);

                break;
            }
            case "t": {

                photoView.setImageResource(R.drawable.jitte);

                break;
            }
            case "u": {

                photoView.setImageResource(R.drawable.chinte);

                break;
            }
            case "v": {

                photoView.setImageResource(R.drawable.sochin);

                break;
            }
            case "w": {

                photoView.setImageResource(R.drawable.meikoyu);

                break;
            }
            case "x": {

                photoView.setImageResource(R.drawable.jiin);

                break;
            }
            case "y": {

                photoView.setImageResource(R.drawable.gojushihodai);

                break;
            }
            case "z": {

                photoView.setImageResource(R.drawable.gojushihosho);

                break;
            }
            case "aa": {

                photoView.setImageResource(R.drawable.nijushiho);

                break;
            }
            case "bb": {

                photoView.setImageResource(R.drawable.wankan);

                break;
            }
            case "cc": {

                photoView.setImageResource(R.drawable.unsu);

                break;
            }


        }


    }

    public void bannerAds(){
        DatabaseReference rootref=FirebaseDatabase.getInstance().getReference().child("AdUnits");
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bannerid=String.valueOf(Objects.requireNonNull(dataSnapshot.child("banner").getValue()).toString());

                View view= findViewById(R.id.bannerad);
                mAdView=new AdView(wkfShotokan.this);
                ((RelativeLayout)view).addView(mAdView);
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(bannerid);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);


                //MediationTestSuite.launch(wkfShotokan.this);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
