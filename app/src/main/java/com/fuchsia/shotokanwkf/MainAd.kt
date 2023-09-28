package com.fuchsia.shotokanwkf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fuchsia.shotokanwkf.Admob.loadInter

open class MainAd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInter(this@MainAd)
    }
}