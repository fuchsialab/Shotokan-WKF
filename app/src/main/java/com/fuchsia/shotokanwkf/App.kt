package com.fuchsia.shotokanwkf

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val defaultValue = HashMap<String, Any>()
        defaultValue[UpdateHelper.KEY_UPDATE_ENABLE] = false
        defaultValue[UpdateHelper.KEY_UPDATE_VERSION] = 1.0
        defaultValue[UpdateHelper.KEY_UPDATE_URL] =
            "https://play.google.com/store/apps/details?id=com.fuchsia.shotokanwkf"
        remoteConfig.setDefaultsAsync(defaultValue)
        remoteConfig.fetch(5)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remoteConfig.fetchAndActivate()
                }
            }
    }
}