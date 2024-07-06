package com.fuchsia.shotokanwkf.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.fuchsia.shotokanwkf.R
import com.fuchsia.shotokanwkf.adapter.kumiteAdapter
import com.fuchsia.shotokanwkf.model.kumitemodel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class kumiteActivity : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var mAdView: AdView? = null
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var recview: RecyclerView? = null
    var adapter: kumiteAdapter? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kumite)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase!!.keepSynced(true)
        instance = this

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()

        if (preferences.getBoolean("isPremium",true) ){


        }
        else{

            bannerAds()

        }
        progressBar = findViewById(R.id.progressbar)
        checkConnection()
        recview = findViewById<View>(R.id.recyclekum) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(this)
        val options = FirebaseRecyclerOptions.Builder<kumitemodel>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("Kumitekt"),
                kumitemodel::class.java
            )
            .build()
        adapter = kumiteAdapter(options)
        adapter!!.startListening()
        recview!!.adapter = adapter
    }

    private fun checkConnection(): String? {
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
            }
        } else {
            Toast.makeText(applicationContext, "No Internet Connection!", Toast.LENGTH_LONG).show()
        }
        return null
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

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var instance: kumiteActivity? = null
        private const val COMMON_TAG = "OrintationChange"
    }
}