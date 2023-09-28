package com.fuchsia.shotokanwkf

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class UpdateHelper(
    private val context: Context,
    private val onUpdateCheckListener: OnUpdateCheckListener?
) {
    interface OnUpdateCheckListener {
        fun onUpdateCheckListener(urlApp: String?)

    }

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            val currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION)
            val appVersion = getAppVersion(context)
            val updateURL = remoteConfig.getString(KEY_UPDATE_URL)
            if (!TextUtils.equals(
                    currentVersion,
                    appVersion
                ) && onUpdateCheckListener != null
            ) onUpdateCheckListener.onUpdateCheckListener(updateURL)
        }
    }

    private fun getAppVersion(context: Context): String {
        var result = ""
        try {
            result = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    class Builder(private val context: Context) {
        private var onUpdateCheckListener: OnUpdateCheckListener? = null
        fun onUpdateCheck(onUpdateCheckListener: OnUpdateCheckListener?): Builder {
            this.onUpdateCheckListener = onUpdateCheckListener
            return this
        }

        fun build(): UpdateHelper {
            return UpdateHelper(context, onUpdateCheckListener)
        }

        fun check(): UpdateHelper {
            val updateHelper = build()
            updateHelper.check()
            return updateHelper
        }
    }

    companion object {
        var KEY_UPDATE_ENABLE = "is_update"
        var KEY_UPDATE_VERSION = "version"
        var KEY_UPDATE_URL = "update_url"
        @JvmStatic
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }
}