package com.batuhan.cryptocurrencyapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.batuhan.cryptocurrencyapp.databinding.ActivityMainBinding
import com.batuhan.cryptocurrencyapp.presentation.graph.GraphActivity
import com.batuhan.cryptocurrencyapp.presentation.splash.SplashActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // no-op
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        askNotificationPermission()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun attachBaseContext(newBase: Context) {
        val base: Context
        val config = newBase.resources.configuration
        val langCode =
            newBase.getSharedPreferences("lang_pref", MODE_PRIVATE)
                .getString("lang", Locale.getDefault().language)
        val locales = LocaleList.forLanguageTags(langCode)
        config.setLocales(locales)
        base = newBase.createConfigurationContext(config)
        super.attachBaseContext(base)
    }

    fun navigateToGraphScreen(currencyId: String) {
        val intent = Intent(this, GraphActivity::class.java)
        intent.putExtra("currencyId", currencyId)
        startActivity(intent)
    }

    fun endApplication() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@OnCompleteListener
                        }
                        val token = task.result
                        val msg = token
                        Log.d("token", msg)
                    }
                )
                Firebase.messaging.subscribeToTopic(getString(R.string.topic_subscribe))
                Firebase.messaging.unsubscribeFromTopic(getString(R.string.topic_unsubscribe_1))
                Firebase.messaging.unsubscribeFromTopic(getString(R.string.topic_unsubscribe_2))
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // no action is done
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            Firebase.messaging.subscribeToTopic(getString(R.string.topic_subscribe))
            Firebase.messaging.unsubscribeFromTopic(getString(R.string.topic_unsubscribe_1))
            Firebase.messaging.unsubscribeFromTopic(getString(R.string.topic_unsubscribe_2))
        }
    }
}
