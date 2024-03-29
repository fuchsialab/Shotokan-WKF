package com.fuchsia.shotokanwkf.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.fuchsia.shotokanwkf.Security
import com.fuchsia.shotokanwkf.databinding.ActivitySubsBinding
import com.fuchsia.shotokanwkf.itemDs
import com.fuchsia.shotokanwkf.adapter.ItmAdapter
import java.io.IOException
import java.util.concurrent.Executors

class Subs : AppCompatActivity() {
    private lateinit var itemArraylist:ArrayList<itemDs>
    var isSuccess =false
    var productId = 0
    private var billingClient:BillingClient? = null
    lateinit var binding: ActivitySubsBinding

    var remAds: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.revItems.layoutManager= LinearLayoutManager(this)
        binding.revItems.hasFixedSize()
        itemArraylist = arrayListOf<itemDs>()

        billingClient =BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        show_list()

    }

    private val purchasesUpdatedListener= PurchasesUpdatedListener {
            billingResult, purchases ->
        if(billingResult.responseCode== BillingClient.BillingResponseCode.OK && purchases != null){
            for(purchase in purchases){
                handlePurchase(purchase)
                Toast.makeText(this,"Successfully Subscribed. Please restart the app.", Toast.LENGTH_SHORT).show()
            }
        }else if(billingResult.responseCode ==BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
            Toast.makeText(this,"Already Subscribed", Toast.LENGTH_SHORT).show()
            isSuccess = true
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED) {
            Toast.makeText(this,"FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show()
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this,"USER_CANCELED", Toast.LENGTH_SHORT).show()
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
            Toast.makeText(this,"BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show()
        }else if (billingResult.responseCode == BillingClient.BillingResponseCode.NETWORK_ERROR) {
            Toast.makeText(this,"NETWORK_ERROR", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }

    }
    fun handlePurchase(purchase: Purchase){
        val consumeParams =ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        val listener = ConsumeResponseListener{billingResult, s ->
            if(billingResult.responseCode == BillingClient.BillingResponseCode.OK){

            }
        }
        billingClient!!.consumeAsync(consumeParams,listener)
        if(purchase.purchaseState == Purchase.PurchaseState.PURCHASED){
            if(!verifyValidSignature(purchase.originalJson,purchase.signature)){
                Toast.makeText(this,"Invalid Purchase",Toast.LENGTH_SHORT).show()
                return
            }
            if(!purchase.isAcknowledged){
                val acknowledgePurchaseParams =AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
                billingClient!!.acknowledgePurchase(
                    acknowledgePurchaseParams,acknowledgePurchaseResponseListener)
                isSuccess = true
            }else{
                Toast.makeText(this,"Already Subscribe",Toast.LENGTH_SHORT).show()
            }
        }else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            Toast.makeText(this,"PENDING",Toast.LENGTH_SHORT).show()
        } else if (purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
            Toast.makeText(this,"UNSPECIFIED STATE",Toast.LENGTH_SHORT).show()
        }
    }

    var acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener{billingResult ->
        if(billingResult.responseCode ==BillingClient.BillingResponseCode.OK){
            isSuccess = true
        }
    }
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
            val security = Security()
            val base64Key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiD/R6SZWw6VLH7oRLO8LZEZB6bnNctDaphT+JoQ07Y6YuHyyF9jDl7cyf9QjqzE9RqujZCSaZW933GWiN4AdHi39PUr2gdLZf5o5Ael9P3Uoz+1SHBHq4IbWQevcd4zH32pg06fgBIue4qMeoheQGQv3nmqVuq8mDYPBGrttyh1RgGAPsIJx56WAl4/pCqiqGDgiV4so0WymsMilLir1UsJoKwWmqo5MnmHpcIN57jFvo6Fmv2t82M3GtaQm4sp+Ws9v84iVpjVC2vVdgMPgIPEUFg0lN2NBcPyxv+iZk4i/LfWGlOCKIYBVdZ2N3qRUbkwyUrqMQpZPI/1xsZ3dXQIDAQAB"
            security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }


    private  fun show_list(){
        billingClient!!.startConnection(object :BillingClientStateListener{
            override fun onBillingServiceDisconnected() {
                TODO("Not yet implemented")
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                val executorService = Executors.newSingleThreadExecutor()
                executorService.execute{
                    val productList = listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("shotokan_subs")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                    val params = QueryProductDetailsParams.newBuilder()
                        .setProductList(productList)
                    billingClient!!.queryProductDetailsAsync(params.build()){
                            billingResult,productDetailsList ->
                        for(productDetails in productDetailsList){
                            if(productDetails.subscriptionOfferDetails != null){
                                for (i in 0 until productDetails.subscriptionOfferDetails!!.size) {
                                    var subsName:String = productDetails.name
                                    var index:Int = i
                                    var phases =""
                                    var formattedPrice: String =productDetails.subscriptionOfferDetails?.get(i)
                                        ?.pricingPhases?.pricingPhaseList?.get(0)?.formattedPrice.toString()
                                    var billingPeriod: String =productDetails.subscriptionOfferDetails?.get(i)
                                        ?.pricingPhases?.pricingPhaseList?.get(0)?.billingPeriod.toString()
                                    var recurrenceMode:String =productDetails.subscriptionOfferDetails?.get(i)
                                        ?.pricingPhases?.pricingPhaseList?.get(0)?.recurrenceMode.toString()
                                    if(recurrenceMode == "2"){
                                        when(billingPeriod){
                                            "P1M"-> billingPeriod =" For 1 Month"
                                            "P3M"-> billingPeriod =" For 3 Month"
                                            "P6M"-> billingPeriod =" For 6 Month"


                                        }
                                    }else{
                                        when(billingPeriod){
                                            "P1M"-> billingPeriod =" / Month"
                                            "P3M"-> billingPeriod =" / 3 Month"
                                            "P6M"-> billingPeriod =" / 6 Month"


                                        }
                                    }
                                    phases ="$formattedPrice$billingPeriod"
                                    for (j in 0 until (productDetails.subscriptionOfferDetails!![i]?.pricingPhases?.pricingPhaseList?.size!!)) {
                                        if(j>0){
                                            var period: String = productDetails.subscriptionOfferDetails?.get(i)?.pricingPhases
                                                ?.pricingPhaseList?.get(j)?.billingPeriod.toString()
                                            var price: String = productDetails.subscriptionOfferDetails?.get(i)?.pricingPhases
                                                ?.pricingPhaseList?.get(j)?.formattedPrice.toString()
                                            when(period){
                                                "P1M"-> period =" / Month"
                                                "P3M"-> period =" / 3 Month"
                                                "P6M"-> period =" / 6 Month"


                                            }
                                            subsName +="\n"+productDetails.subscriptionOfferDetails?.get(i)?.offerId.toString()
                                            phases += "\n$price$period"
                                        }
                                    }
                                    val tmpItm = itemDs(subsName,phases,index)
                                    itemArraylist.add(tmpItm)
                                }

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
                    var adapter = ItmAdapter(itemArraylist)
                    binding.revItems.adapter = adapter
                    adapter.setOnItemClickListener(object : ItmAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val cItem =itemArraylist[position]
                            productId =cItem.planIndex
                            subscribeProduct()
                        }

                    })

                }
            }

        })
    }
    fun subscribeProduct(){
        billingClient!!.startConnection(object :BillingClientStateListener{
            override fun onBillingServiceDisconnected() {
            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if(billingResult.responseCode== BillingClient.BillingResponseCode.OK){
                    val productList = listOf(
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId("shotokan_subs")
                            .setProductType(BillingClient.ProductType.SUBS)
                            .build()
                    )
                    val params = QueryProductDetailsParams.newBuilder()
                        .setProductList(productList)
                    billingClient!!.queryProductDetailsAsync(params.build()) {
                            billingResult, productDetailsList ->
                        for (productDetails in productDetailsList) {
                            val offerToken = productDetails.subscriptionOfferDetails?.get(productId)?.offerToken
                            val productDetailsParamsList = listOf(
                                offerToken?.let {
                                    BillingFlowParams.ProductDetailsParams.newBuilder()
                                        .setProductDetails(productDetails)
                                        .setOfferToken(it)
                                        .build()
                                }
                            )
                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build()
                            val billingResult = billingClient!!.launchBillingFlow(this@Subs,billingFlowParams)

                        }
                    }

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