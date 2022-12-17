package com.fuchsia.shotokanwkf;

import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class Admob {

   public  static InterstitialAd mInterstitialAd;

   public static  void loadInter(Context context){

       MobileAds.initialize(context, new OnInitializationCompleteListener() {
           @Override
           public void onInitializationComplete(InitializationStatus initializationStatus) {}
       });

       AdRequest adRequest = new AdRequest.Builder().build();

       InterstitialAd.load(context,context.getString(R.string.interstitialId), adRequest,
               new InterstitialAdLoadCallback() {
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
}
