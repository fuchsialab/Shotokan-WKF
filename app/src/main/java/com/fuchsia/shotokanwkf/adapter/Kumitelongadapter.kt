package com.fuchsia.shotokanwkf.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.fuchsia.shotokanwkf.Admob
import com.fuchsia.shotokanwkf.Admob.loadInter
import com.fuchsia.shotokanwkf.R
import com.fuchsia.shotokanwkf.activity.KumiteLong.Companion.instance
import com.fuchsia.shotokanwkf.model.Kumitelongmodel
import com.fuchsia.shotokanwkf.activity.videoPlayer
import com.google.android.gms.ads.FullScreenContentCallback

class Kumitelongadapter(options: FirebaseRecyclerOptions<Kumitelongmodel?>?) :
    FirebaseRecyclerAdapter<Kumitelongmodel, Kumitelongadapter.myviewholder>(
        options!!
    ) {

    var context: Context? = null
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.newkumite, parent, false)
        return myviewholder(view)
    }
    override fun onBindViewHolder(holder: myviewholder, position: Int, model: Kumitelongmodel) {

        holder.textView.text = model.name
        holder.cardView.setOnClickListener { view ->

            val activity = view.context as AppCompatActivity

            preferences = activity.getSharedPreferences("subs", AppCompatActivity.MODE_PRIVATE)
            editor = preferences.edit()

            if (preferences.getBoolean("isPremium",true) ){
                val intent = Intent(activity, videoPlayer::class.java)
                intent.putExtra("nam", model.url)
                activity.startActivity(intent)

            }else{
                if (Admob.mInterstitialAd != null) {
                    Admob.mInterstitialAd!!.show(instance!!)
                    Admob.mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                val intent = Intent(activity, videoPlayer::class.java)
                                intent.putExtra("nam", model.url)
                                activity.startActivity(intent)
                                Admob.mInterstitialAd = null
                                loadInter(instance!!)
                            }
                        }
                } else {
                    val intent = Intent(activity, videoPlayer::class.java)
                    intent.putExtra("nam", model.url)
                    activity.startActivity(intent)
                    loadInter(instance!!)
                }

            }


        }
    }

    inner class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.kataname)
            cardView = itemView.findViewById(R.id.cardview)
        }
    }
}