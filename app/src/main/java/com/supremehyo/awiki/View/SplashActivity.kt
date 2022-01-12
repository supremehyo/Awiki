package com.supremehyo.awiki.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.supremehyo.awiki.MainActivity
import com.supremehyo.awiki.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //getWindow().setNavigationBarColor(getColor(R.color.colorSplashBackground))

        // 내비게이션바 없애기 ======================
        val uiOptions: Int = getWindow().getDecorView().getSystemUiVisibility()
        var newUiOptions = uiOptions
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY === uiOptions
        if (isImmersiveModeEnabled) {
            Log.d("off", "Turning immersive mode mode off. ")
        } else {
            Log.d("on", "Turning immersive mode mode on.")
        }
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions)
        // 내비게이션바 없애기 End ======================

        val hd = Handler()
        hd.postDelayed(splashHandler(), 2000) // 2초 후에 hd handler 실행
    }

    private inner class splashHandler : Runnable {
        override fun run() {
            startActivity(Intent(getApplication(), MainActivity::class.java)) //로딩이 끝난 후, HomeActivity 이동
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            this@SplashActivity.finish() // 로딩페이지 Activity stack에서 제거
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}