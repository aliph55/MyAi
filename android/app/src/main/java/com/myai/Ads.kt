package com.myai

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.LoadAdError
import java.util.Arrays

class Ads(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    override fun getName(): String = "Ads"

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null

    private val TAG = "AdsModule"

    @ReactMethod
    fun initializeAdMob(promise: Promise) {
        Log.d(TAG, "initializeAdMob called")
        try {
            // Test Cihazı ID'sini ekle (Logcat'ten aldığınız ID ile değiştirin)
            val testDeviceIds = Arrays.asList("YOUR_TEST_DEVICE_ID") // Logcat'ten aldığınız ID'yi buraya yazın, örneğin "33BE2250B43518CCDA7DE426D04EE231"
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
            MobileAds.setRequestConfiguration(configuration)

            MobileAds.initialize(reactApplicationContext) {
                Log.d(TAG, "AdMob initialized successfully")
                promise.resolve("AD_INITIALIZED")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing AdMob: ${e.message}")
            promise.reject("AD_INIT_ERROR", "Failed to initialize AdMob: ${e.message}")
        }
    }

    @ReactMethod
    fun loadInterstitialAd(adUnitId: String, promise: Promise) {
        Log.d(TAG, "loadInterstitialAd called with adUnitId: $adUnitId")
        currentActivity?.runOnUiThread {
            try {
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    reactApplicationContext,
                    adUnitId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(ad: InterstitialAd) {
                            Log.d(TAG, "Interstitial ad loaded successfully")
                            interstitialAd = ad
                            promise.resolve("Interstitial ad loaded successfully")
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.e(TAG, "Interstitial ad failed to load: Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}")
                            interstitialAd = null
                            val errorMessage = "Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}"
                            promise.reject("AD_LOAD_ERROR", errorMessage)
                        }
                    })
            } catch (e: Exception) {
                Log.e(TAG, "Error loading Interstitial ad: ${e.message}")
                promise.reject("AD_LOAD_ERROR", "Failed to load Interstitial ad: ${e.message}")
            }
        } ?: run {
            Log.e(TAG, "Activity is null during loadInterstitial")
            promise.reject("AD_ERROR", "Activity is null during ad loadInterstitial")
        }
    }

    @ReactMethod
    fun showInterstitialAd(promise: Promise) {
        Log.d(TAG, "showInterstitialAd called")
        currentActivity?.runOnUiThread {
            try {
                val ad = interstitialAd
                val activity = currentActivity
                if (ad != null && activity != null) {
                    ad.show(activity)
                    Log.d(TAG, "Interstitial ad shown")
                    interstitialAd = null
                    promise.resolve("Interstitial ad shown successfully")
                } else {
                    val errorMessage = when {
                        ad == null -> "Interstitial ad is null"
                        activity == null -> "Activity is null"
                        else -> "Unknown error"
                    }
                    Log.e(TAG, "showInterstitialAd failed: $errorMessage") // Fazladan } kaldırıldı
                    promise.reject("AD_NOT_LOADED", "Interstitial ad not loaded or activity is null: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to show Interstitial ad: ${e.message}")
                promise.reject("AD_SHOW_ERROR", "Failed to show Interstitial ad: ${e.message}")
            }
        } ?: run {
            Log.e(TAG, "Activity is null during showInterstitial")
            promise.reject("AD_ERROR", "Activity is null during ad show")
        }
    }

    @ReactMethod
    fun loadRewardedAd(adUnitId: String, promise: Promise) {
        Log.d(TAG, "loadRewardedAd called with adUnitId: $adUnitId")
        currentActivity?.runOnUiThread {
            try {
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(
                    reactApplicationContext,
                    adUnitId,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdLoaded(ad: RewardedAd) { // loadOnAdLoaded -> onAdLoaded olarak düzeltildi
                            Log.d(TAG, "Rewarded ad loaded successfully")
                            rewardedAd = ad
                            promise.resolve("Rewarded ad loaded successfully")
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) { // loadOnAdFailedToLoad -> onAdFailedToLoad olarak düzeltildi
                            Log.e(TAG, "Rewarded ad failed to load: Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}")
                            rewardedAd = null
                            val errorMessage = "Error Code: ${error.code}, Message: ${error.message}, Domain: ${error.domain}, Cause: ${error.cause?.toString() ?: "Unknown"}"
                            promise.reject("AD_LOAD_ERROR", errorMessage)
                        }
                    })
            } catch (e: Exception) {
                Log.e(TAG, "Error loading Rewarded ad: ${e.message}")
                promise.reject("AD_LOAD_ERROR", "Failed to load Rewarded ad: ${e.message}")
            }
        } ?: run {
            Log.e(TAG, "Activity is null during loadRewarded")
            promise.reject("AD_ERROR", "Activity is null during ad load")
        }
    }

    @ReactMethod
    fun showRewardedAd(promise: Promise) {
        Log.d(TAG, "showRewardedAd called")
        currentActivity?.runOnUiThread {
            try {
                val ad = rewardedAd
                val activity = currentActivity
                if (ad != null && activity != null) {
                    ad.show(activity) { rewardItem ->
                        Log.d(TAG, "Reward earned: ${rewardItem.amount} ${rewardItem.type}")
                        rewardedAd = null
                        promise.resolve("Reward earned: ${rewardItem.amount} ${rewardItem.type}")
                    }
                    Log.d(TAG, "Rewarded ad shown")
                } else {
                    val errorMessage = when {
                        ad == null -> "Rewarded ad is null"
                        activity == null -> "Activity is null"
                        else -> "Unknown error"
                    }
                    Log.e(TAG, "showRewardedAd failed: $errorMessage") // Fazladan } kaldırıldı
                    promise.reject("AD_NOT_LOADED", "Rewarded ad not loaded or activity is null: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to show Rewarded ad: ${e.message}")
                promise.reject("AD_SHOW_ERROR", "Failed to show Rewarded ad: ${e.message}")
            }
        } ?: run {
            Log.e(TAG, "Activity is null during showRewarded")
            promise.reject("AD_ERROR", "Activity is null during ad show")
        }
    }
}