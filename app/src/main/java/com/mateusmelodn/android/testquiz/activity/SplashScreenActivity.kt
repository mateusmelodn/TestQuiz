package com.mateusmelodn.android.testquiz.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.mateusmelodn.android.testquiz.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        It can be used to fetch data or similar work
        GlobalScope.async {
            delay(2000) // Fake some work or just show logo
            launchMainActivity()
        }
    }

    // Start MainActivity and finish current
    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}