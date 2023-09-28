package com.fuchsia.shotokanwkf.activity

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fuchsia.shotokanwkf.Admob
import com.fuchsia.shotokanwkf.Admob.loadInter
import com.fuchsia.shotokanwkf.R
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RULL : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var img: ImageView? = null
    var video: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rull)
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        preferences = getSharedPreferences("subs", MODE_PRIVATE)
        editor = preferences.edit()


        //MediationTestSuite.launch(this);
        img = findViewById(R.id.wkf2)
        video = findViewById(R.id.wkf1)
        img?.setOnClickListener(View.OnClickListener {

            if (preferences.getBoolean("isPremium",true) ){

                val intent = Intent(this@RULL, wkfShotokan::class.java)
                intent.putExtra("katapic", "wkf")
                startActivity(intent)
            }
            else{
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd!!.show(this@RULL)
                    Admob.mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Admob.mInterstitialAd = null
                                loadInter(this@RULL)
                                val intent = Intent(this@RULL, wkfShotokan::class.java)
                                intent.putExtra("katapic", "wkf")
                                startActivity(intent)
                            }
                        }
                } else {
                    loadInter(this@RULL)
                    val intent = Intent(this@RULL, wkfShotokan::class.java)
                    intent.putExtra("katapic", "wkf")
                    startActivity(intent)
                }
            }


        })
        video?.setOnClickListener(View.OnClickListener {

            if (preferences.getBoolean("isPremium",true) ){

                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")
                    )
                )
            }
            else{
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd!!.show(this@RULL)
                    Admob.mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Admob.mInterstitialAd = null
                                loadInter(this@RULL)
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")
                                    )
                                )
                            }
                        }
                } else {
                    loadInter(this@RULL)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=c6r8JwEFowY")
                        )
                    )
                }

            }


        })
    }
}