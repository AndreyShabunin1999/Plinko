package com.dial.plinko.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.dial.plinko.api.ApiUtilities
import com.dial.plinko.repository.InfoUrlRepository
import com.dial.plinko.viewmodel.InfoUrlViewModel
import com.dial.plinko.viewmodel.InfoUrlViewModelFactory
import com.dial.plinko.api.ApiInterface
import com.dial.plinko.databinding.ActivitySplashBinding
import com.onesignal.OneSignal
import java.util.Locale

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var infoUrlViewModel: InfoUrlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.imgLogoApp.load("http://5.78.108.148/3/logo.png")
        binding.imgBackground.load("http://5.78.108.148/3/background.jpg")

        Handler(Looper.getMainLooper()).postDelayed({

            val apiInterface = ApiUtilities.getInstance().create(ApiInterface::class.java)

            val infoIPRepository = InfoUrlRepository(apiInterface)

            infoUrlViewModel = ViewModelProvider(this, InfoUrlViewModelFactory(infoIPRepository,
                getSystemDetail(), Locale.getDefault().language, Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            )[InfoUrlViewModel::class.java]

            infoUrlViewModel.info.observe(this) {
                val intent = Intent(this@SplashActivity, WebViewActivity::class.java)

                when(it.url) {
                    "no" -> intent.putExtra("LINK", "file:///android_asset/game/index.html")
                    "nopush"->{
                        OneSignal.disablePush(true)
                        intent.putExtra("LINK", "file:///android_asset/game/index.html")
                    } else -> intent.putExtra("LINK", it.url)
                }
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    OneSignal.promptForPushNotifications(false) {
                        startActivity(intent)
                        finish()
                    }
                } else {
                    startActivity(intent)
                    finish()
                }
            }
        }, 3000)
    }

    @SuppressLint("HardwareIds")
    private fun getSystemDetail(): String {
        return Build.BRAND + " " + Build.MODEL
    }
}