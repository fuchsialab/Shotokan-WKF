package com.fuchsia.shotokanwkf.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fuchsia.shotokanwkf.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class historyShotokan : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var you: Button? = null
    private var mAdView: AdView? = null
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_shotokan)
        you = findViewById(R.id.youtube_button)

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
        you?.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=KdOAdnfoepU")
                )
            )
        })
    }

    fun bannerAds() {
        MobileAds.initialize(this) { }
        val view = findViewById<View>(R.id.bannerad)
        mAdView = AdView(this@historyShotokan)
        (view as RelativeLayout).addView(mAdView)
        mAdView!!.setAdSize(AdSize.SMART_BANNER)
        mAdView!!.adUnitId = resources.getString(R.string.bannerid)
        val adRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        //MediationTestSuite.launch(basicKarate.this);
    }
}