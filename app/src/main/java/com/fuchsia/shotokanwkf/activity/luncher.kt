@file:Suppress("DEPRECATION")

package com.fuchsia.shotokanwkf.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryPurchasesParams
import com.fuchsia.shotokanwkf.Admob
import com.fuchsia.shotokanwkf.MainAd
import com.fuchsia.shotokanwkf.R
import java.util.concurrent.Executors

class luncher : MainAd() {

    private var billingClient: BillingClient? = null
    var proName= ""
    var isRemoveAds = false
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_luncher)

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()


        queryPurchase()

        hideNavigationbar()
        Handler().postDelayed({
            val i = Intent(this@luncher, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 2000)

    }

    private fun hideNavigationbar() {
        this.window.decorView.systemUiVisibility = (
                SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
    }



    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }


    fun queryPurchase(){
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val executorService = Executors.newSingleThreadExecutor()
                executorService.execute {
                    billingClient!!.queryPurchasesAsync(
                        QueryPurchasesParams.newBuilder()
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    ){billingResult,purchaseList ->
                        for(purchases in purchaseList){
                            if(purchases !=null && purchases.isAcknowledged){
                                isRemoveAds = true
                                proName ="Subscribed"
                            }
                            else{
                                proName ="Not Subscribed"
                                isRemoveAds = false
                            }
                        }

                    }

                }
                runOnUiThread {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    preferences = getSharedPreferences("subs", MODE_PRIVATE)
                    editor = preferences.edit()
                    editor.putBoolean("isPremium",isRemoveAds)
                    editor.apply()
                   // Toast.makeText(baseContext, isRemoveAds.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }


}