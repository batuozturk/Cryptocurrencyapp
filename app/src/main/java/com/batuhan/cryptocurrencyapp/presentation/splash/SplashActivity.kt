package com.batuhan.cryptocurrencyapp.presentation.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.WindowInsetsControllerCompat
import com.batuhan.cryptocurrencyapp.MainActivity
import com.batuhan.cryptocurrencyapp.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        firebaseAnalytics = Firebase.analytics
    }

    override fun onStart() {
        super.onStart()
        viewModel.run {
            routing.observe(this@SplashActivity) {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val base: Context
        val config = newBase.resources.configuration
        val sharedPreferences = newBase.getSharedPreferences("lang_pref", MODE_PRIVATE)
        var langCode = sharedPreferences.getString("lang", null)
        if(langCode == null){
            langCode = Locale.getDefault().language
            sharedPreferences.edit().putString("lang", langCode).apply()
        }
        val locales = LocaleList.forLanguageTags(langCode)
        config.setLocales(locales)
        base = newBase.createConfigurationContext(config)
        super.attachBaseContext(base)
    }
}
