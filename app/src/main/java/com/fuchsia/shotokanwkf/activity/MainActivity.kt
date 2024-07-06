@file:Suppress("DEPRECATION")

package com.fuchsia.shotokanwkf.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.media3.common.util.UnstableApi
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.fuchsia.shotokanwkf.Admob
import com.fuchsia.shotokanwkf.Admob.loadInter
import com.fuchsia.shotokanwkf.R
import com.fuchsia.shotokanwkf.UpdateHelper.Companion.with
import com.fuchsia.shotokanwkf.UpdateHelper.OnUpdateCheckListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import hotchemi.android.rate.AppRate
import java.util.Objects
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity(), OnUpdateCheckListener {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var mAdView: AdView? = null
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var layout: CardView? = null
    var kummitenew: CardView? = null
    var progressDialog: ProgressDialog? = null
    var timer: Timer? = null
    var history: Button? = null
    var wkf: Button? = null
    var basic: Button? = null
    var kata: Button? = null
    var kumite: Button? = null
    var nunc: Button? = null
    private var backPressTime: Long = 0
    var toolbar: ImageView? = null
    private var consentInformation: ConsentInformation? = null
    private var rewardedAd: RewardedAd? = null
    var remAds: CardView? = null

    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private val isMobileAdsInitializeCalled = AtomicBoolean(false)


    override fun onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressTime = System.currentTimeMillis()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(UnstableApi::class) @SuppressLint("CommitPrefEdits", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        remAds = findViewById(R.id.removeAds)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()
        if (preferences.getBoolean("isPremium",true) ){

            Toast.makeText(baseContext, "Premium", Toast.LENGTH_SHORT).show()
        }
        else{
            remAds?.visibility = View.VISIBLE
            bannerAds()
            loadRewardedAd()
        }

        // Set tag for under age of consent. false means users are not under age
        // of consent.
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        consentInformation?.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this,
                    OnConsentFormDismissedListener { loadAndShowError: FormError? ->

                        // Consent has been gathered.
                        if (consentInformation!!.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            { requestConsentError: FormError? -> })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation!!.canRequestAds()) {
            initializeMobileAdsSdk()
        }
        //remove ads
        remAds = findViewById(R.id.removeAds)

        remAds?.setOnClickListener {

            startActivity(Intent(this, Subs::class.java))

        }

        //bunkai btn
        layout = findViewById(R.id.linncu)

        kummitenew = findViewById(R.id.kummitenew)
        layout?.setOnClickListener(View.OnClickListener {

            if (preferences.getBoolean("isPremium",true) ){

                startActivity(Intent(this@MainActivity, KataBunkai::class.java))
            }
            else{
                if (rewardedAd != null) {
                    rewardedAd?.let { ad ->
                        ad.show(this, OnUserEarnedRewardListener { rewardItem ->

                            rewardedAd = null

                        })
                        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdClicked() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                startActivity(Intent(this@MainActivity, KataBunkai::class.java))
                                loadRewardedAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                loadRewardedAd()
                                startActivity(Intent(this@MainActivity, KataBunkai::class.java))
                            }

                            override fun onAdShowedFullScreenContent() {

                            }
                        }
                    } ?: run {
                        loadRewardedAd()
                        startActivity(Intent(this@MainActivity, KataBunkai::class.java))
                    }
                } else {
                    loadRewardedAd()
                    startActivity(Intent(this@MainActivity, KataBunkai::class.java))
                }

            }



        })


        toolbar = findViewById(R.id.tool_icon)

        toolbar?.setOnClickListener {

            val intent = Intent(this, NavBar::class.java)
            startActivity(intent)
            Animatoo.animateSwipeRight(this);


        }


        AppRate.with(this)
            .setInstallDays(0)
            .setLaunchTimes(5)
            .setRemindInterval(10)
            .setShowLaterButton(true)
            .setDebug(false)
            .setOnClickButtonListener { which ->
                Log.d(
                    MainActivity::class.java.name,
                    Integer.toString(which)
                )
            }
            .monitor()

        AppRate.showRateDialogIfMeetsConditions(this)

        with(this)
            .onUpdateCheck(this)
            .check()

        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                progressDialog!!.dismiss()
            }
        }, 3000)

        progressDialog = ProgressDialog(this@MainActivity)
        progressDialog!!.show()
        progressDialog!!.setContentView(R.layout.progress)
        Objects.requireNonNull(progressDialog!!.window)
            ?.setBackgroundDrawableResource(android.R.color.transparent)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase!!.keepSynced(true)

        history = findViewById(R.id.btnhistory)
        wkf = findViewById(R.id.btnwkfrull)
        basic = findViewById(R.id.btnbasic)
        kata = findViewById(R.id.btnkata)
        kumite = findViewById(R.id.btnkumite)
        nunc = findViewById(R.id.btnnun)
        history?.setOnClickListener(View.OnClickListener {


            if (preferences.getBoolean("isPremium",true) ){
                startActivity(Intent(this@MainActivity, historyShotokan::class.java))
            }else{

                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd!!.show(this@MainActivity)
                    Admob.mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Admob.mInterstitialAd = null
                                loadInter(this@MainActivity)
                                startActivity(Intent(this@MainActivity, historyShotokan::class.java))
                            }
                        }
                } else {
                    loadInter(this@MainActivity)
                    startActivity(Intent(this@MainActivity, historyShotokan::class.java))
                }



            }
        })
        wkf?.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this@MainActivity, PlayerActivity::class.java))

            if (preferences.getBoolean("isPremium",true) ){

                startActivity(Intent(this@MainActivity, RULL::class.java))

            }else{

                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd!!.show(this@MainActivity)
                    Admob.mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Admob.mInterstitialAd = null
                                loadInter(this@MainActivity)
                                startActivity(Intent(this@MainActivity, RULL::class.java))
                            }
                        }
                } else {
                    loadInter(this@MainActivity)
                    startActivity(Intent(this@MainActivity, RULL::class.java))
                }

            }


        })
        basic?.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this@MainActivity, basicKarate::class.java))

        })
        kata?.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this@MainActivity, kataList::class.java))


        })
        kumite?.setOnClickListener(View.OnClickListener {
            if (preferences.getBoolean("isPremium",true) ){
                startActivity(Intent(this@MainActivity, kumiteActivity::class.java))

            }else{
                if (rewardedAd != null) {
                    rewardedAd?.let { ad ->
                        ad.show(this, OnUserEarnedRewardListener { rewardItem ->

                            rewardedAd = null

                        })
                        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdClicked() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                startActivity(Intent(this@MainActivity, kumiteActivity::class.java))
                                loadRewardedAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                startActivity(Intent(this@MainActivity, kumiteActivity::class.java))
                                loadRewardedAd()
                            }

                            override fun onAdShowedFullScreenContent() {

                            }
                        }
                    } ?: run {
                        loadRewardedAd()

                        startActivity(Intent(this@MainActivity, kumiteActivity::class.java))
                    }
                } else {
                    loadRewardedAd()
                    startActivity(Intent(this@MainActivity, kumiteActivity::class.java))
                }
            }

        })
        nunc?.setOnClickListener(View.OnClickListener {

            if (preferences.getBoolean("isPremium",true) ){
                val intent = Intent(this@MainActivity, videoPlayer::class.java)
                intent.putExtra("nam", "fAEHQzfEN0g")
                startActivity(intent)

            }else{
                if (rewardedAd != null) {
                    rewardedAd?.let { ad ->
                        ad.show(this, OnUserEarnedRewardListener { rewardItem ->

                            rewardedAd = null

                        })
                        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdClicked() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                val intent = Intent(this@MainActivity, videoPlayer::class.java)
                                intent.putExtra("nam", "fAEHQzfEN0g")
                                startActivity(intent)
                                loadRewardedAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                val intent = Intent(this@MainActivity, videoPlayer::class.java)
                                intent.putExtra("nam", "fAEHQzfEN0g")
                                startActivity(intent)
                                loadRewardedAd()
                            }

                            override fun onAdShowedFullScreenContent() {

                            }
                        }
                    } ?: run {
                        loadRewardedAd()

                        val intent = Intent(this@MainActivity, videoPlayer::class.java)
                        intent.putExtra("nam", "fAEHQzfEN0g")
                        startActivity(intent)
                    }
                } else {
                    loadRewardedAd()
                    val intent = Intent(this@MainActivity, videoPlayer::class.java)
                    intent.putExtra("nam", "fAEHQzfEN0g")
                    startActivity(intent)
                }

            }

        })
        kummitenew?.setOnClickListener(View.OnClickListener {

            if (preferences.getBoolean("isPremium",true) ){
                startActivity(Intent(this@MainActivity, KumiteLong::class.java))

            }else{
                if (rewardedAd != null) {
                    rewardedAd?.let { ad ->
                        ad.show(this, OnUserEarnedRewardListener { rewardItem ->

                            rewardedAd = null

                        })
                        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdClicked() {

                            }

                            override fun onAdDismissedFullScreenContent() {
                                startActivity(Intent(this@MainActivity, KumiteLong::class.java))
                                loadRewardedAd()
                            }

                            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                                startActivity(Intent(this@MainActivity, KumiteLong::class.java))
                                loadRewardedAd()
                            }

                            override fun onAdShowedFullScreenContent() {

                            }
                        }
                    } ?: run {
                        loadRewardedAd()
                        startActivity(Intent(this@MainActivity, KumiteLong::class.java))
                    }
                } else {
                    loadRewardedAd()
                    startActivity(Intent(this@MainActivity, KumiteLong::class.java))
                }

            }


        })

    }

    override fun onUpdateCheckListener(urlApp: String?) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("New Version Available")
            .setMessage(" Please update for better experience")
            .setPositiveButton("UPDATE") { dialogInterface, i ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.jjjj))
                    )
                )
            }
            .setNegativeButton("NOT NOW") { dialogInterface, i -> dialogInterface.dismiss() }
            .create()
        alertDialog.show()
    }

    fun bannerAds() {
        MobileAds.initialize(this) { }
        val view = findViewById<View>(R.id.bannerad)
        mAdView = AdView(this)
        (view as RelativeLayout).addView(mAdView)
        mAdView!!.setAdSize(AdSize.SMART_BANNER)
        mAdView!!.adUnitId = resources.getString(R.string.bannerid)
          val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        // MediationTestSuite.launch(instance);
    }

    fun showInterstitial() {
        if (Admob.mInterstitialAd != null) {
            Admob.mInterstitialAd!!.show(this)
            Admob.mInterstitialAd!!.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Admob.mInterstitialAd = null
                        loadInter(this@MainActivity)
                    }
                }
        } else {
            loadInter(this@MainActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(COMMON_TAG, "MainActivity onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(COMMON_TAG, "MainActivity onSaveInstanceState")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(COMMON_TAG, "landscape")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(COMMON_TAG, "portrait")
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

        // TODO: Request an ad.
        // InterstitialAd.load(...);
    }

    companion object {
        private const val COMMON_TAG = "OrintationChange"
    }

    private fun loadRewardedAd() {

        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            this,
            getString(R.string.rewardId),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError.toString().let { Log.d(TAG, it) }
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedAd = ad
                }
            })
    }

}