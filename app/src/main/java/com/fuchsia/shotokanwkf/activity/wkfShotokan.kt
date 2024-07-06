package com.fuchsia.shotokanwkf.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fuchsia.shotokanwkf.R
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class wkfShotokan : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var mAdView: AdView? = null
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var photoView: PhotoView? = null
    var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wkf_shotokan)
        photoView = findViewById(R.id.photo_view)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark


        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()

        if (preferences.getBoolean("isPremium",true) ){


        }
        else{

            bannerAds()

        }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        bundle = intent.extras
        if (bundle != null) {
            val name = bundle!!.getString("katapic")!!
            setUp(name)
        }
    }

    private fun setUp(name: String?) {
        when (name) {
            "belt" -> {
                photoView!!.setImageResource(R.drawable.belt)
            }
            "fall" -> {
                photoView!!.setImageResource(R.drawable.falling)
            }
            "kick" -> {
                photoView!!.setImageResource(R.drawable.geri)
            }
            "uke" -> {
                photoView!!.setImageResource(R.drawable.uke)
            }
            "dachi" -> {
                photoView!!.setImageResource(R.drawable.dachi)
            }
            "greeting" -> {
                photoView!!.setImageResource(R.drawable.somman)
            }
            "wapon" -> {
                photoView!!.setImageResource(R.drawable.wapon)
            }
            "wkf" -> {
                photoView!!.setImageResource(R.drawable.wkfr)
            }
            "uchi" -> {
                photoView!!.setImageResource(R.drawable.uchi)
            }
        }
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

        //MediationTestSuite.launch(basicKarate.this);
    }
}