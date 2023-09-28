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
import com.fuchsia.shotokanwkf.adapter.Kumitelongadapter
import com.fuchsia.shotokanwkf.model.Kumitelongmodel
import com.fuchsia.shotokanwkf.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class KumiteLong : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var mAdView: AdView? = null
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var recview: RecyclerView? = null
    var adapter: Kumitelongadapter? = null
    var progressBar: ProgressBar? = null
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kumite_long)

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()

        if (preferences.getBoolean("isPremium",true) ){


        }
        else{

            bannerAds()

        }
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase!!.keepSynced(true)
        instance = this
        MobileAds.initialize(this) { initializationStatus ->
            val statusMap = initializationStatus.adapterStatusMap
            for (adapterClass in statusMap.keys) {
                val status = statusMap[adapterClass]
                Log.d(
                    "MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status!!.description, status.latency
                    )
                )
            }

            // Start loading ads here...
        }
        progressBar = findViewById(R.id.progressbar)
        checkConnection()
        recview = findViewById<View>(R.id.recyclek) as RecyclerView
        recview!!.layoutManager = LinearLayoutManager(this)
        val options = FirebaseRecyclerOptions.Builder<Kumitelongmodel>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("SpecialKumitekt"),
                Kumitelongmodel::class.java
            )
            .build()
        adapter = Kumitelongadapter(options)
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
        var instance: KumiteLong? = null
        private const val COMMON_TAG = "OrintationChange"
    }
}