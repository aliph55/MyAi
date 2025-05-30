package com.projeadi

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import android.widget.LinearLayout
import com.facebook.react.bridge.ReactContext

class AdMobModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "AdMobModule"
    }

    @ReactMethod
    fun initializeAdMob(appId: String, promise: Promise) {
        MobileAds.initialize(reactApplicationContext) {
            promise.resolve("AdMob initialized")
        }
    }

    @ReactMethod
    fun showBannerAd(adUnitId: String, promise: Promise) {
        val adView = AdView(reactApplicationContext)
        adView.setAdSize(AdSize.BANNER)
        adView.adUnitId = adUnitId

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // Reklamı ekranda göstermek için bir LinearLayout kullanıyoruz
        val activity = currentActivity
        if (activity != null) {
            val layout = LinearLayout(activity)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(adView)
            activity.addContentView(layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
            promise.resolve("Banner ad loaded")
        } else {
            promise.reject("ACTIVITY_NOT_FOUND", "Activity is null")
        }
    }
}