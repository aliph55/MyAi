package com.myai

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.LoadAdError

class Ads(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String {
        return "Ads"
    }

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    @ReactMethod
    fun initializeAdMob(promise: Promise) {
        MobileAds.initialize(reactApplicationContext) {
            promise.resolve("AdMob initialized")
        }
    }

    @ReactMethod
    fun loadInterstitialAd(adUnitId: String, promise: Promise) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(reactApplicationContext, adUnitId, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    promise.resolve("Interstitial ad loaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    promise.reject("AD_LOAD_ERROR", adError.message)
                }
            })
    }

    @ReactMethod
    fun showInterstitialAd(promise: Promise) {
        val ad = interstitialAd
        val activity = currentActivity
        if (ad != null && activity != null) {
            try {
                ad.show(activity)
                interstitialAd = null
                promise.resolve("Interstitial ad shown")
            } catch (e: Exception) {
                promise.reject("AD_SHOW_ERROR", "Failed to show interstitial ad: ${e.message}")
            }
        } else {
            promise.reject("AD_NOT_LOADED", "Interstitial ad not loaded or activity is null")
        }
    }

    @ReactMethod
    fun loadRewardedAd(adUnitId: String, promise: Promise) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(reactApplicationContext, adUnitId, adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    promise.resolve("Rewarded ad loaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    promise.reject("AD_LOAD_ERROR", adError.message)
                }
            })
    }

    @ReactMethod
    fun showRewardedAd(promise: Promise) {
        val ad = rewardedAd
        val activity = currentActivity
        if (ad != null && activity != null) {
            try {
                ad.show(activity) { rewardItem ->
                    rewardedAd = null
                    promise.resolve("Reward earned: ${rewardItem.amount} ${rewardItem.type}")
                }
            } catch (e: Exception) {
                promise.reject("AD_SHOW_ERROR", "Failed to show rewarded ad: ${e.message}")
            }
        } else {
            promise.reject("AD_NOT_LOADED", "Rewarded ad not loaded or activity is null")
        }
    }
}